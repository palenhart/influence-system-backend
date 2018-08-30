package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.IllegalBidException;
import com.thecorporateer.influence.exceptions.RepositoryNotFoundException;
import com.thecorporateer.influence.objects.Auction;
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.repositories.AuctionRepository;

@Service
public class AuctionService {

	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private InfluenceHandlingService influenceHandlingService;
	@Autowired
	private ObjectService objectService;
	@Autowired
	private AuctionRepository auctionRepository;

	private boolean validateAuction(String beginningTimestamp, String endingTimestamp, String title, String description,
			Authentication creator, Division usableInfluenceDivision, Long minBid, Long minStep) {

		// values must not be null
		if (null == beginningTimestamp || null == endingTimestamp || null == title || null == description
				|| null == creator || null == usableInfluenceDivision || null == minBid || null == minStep) {
			return false;
		}

		// ending date must be in the future
		if (Instant.parse(endingTimestamp).isBefore(Instant.now())) {
			return false;
		}

		// if beginning was in the past, set it to now
		if (Instant.parse(beginningTimestamp).isBefore(Instant.now())) {
			beginningTimestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
		}

		// beginning must not be after ending
		if (Instant.parse(beginningTimestamp).isAfter(Instant.parse(endingTimestamp))) {
			return false;
		}

		// beginningTimestamp
		// endingTimestamp

		// title my not be empty or too long
		// TODO
		if (title.trim().isEmpty() || title.length() > 1) {
			return false;
		}

		// description my not be empty or too long
		// TODO
		if (description.trim().isEmpty() || description.length() > 1) {
			return false;
		}

		// creator
		// usableInfluenceDivision

		// minBid must be non-negative
		if (minBid < 0) {
			return false;
		}
		// minStep must be positive
		if (minStep < 1) {
			return false;
		}

		return true;
	}

	// create auction with all customizable properties
	public void createAuction(String beginningTimestamp, String endingTimestamp, String title, String description,
			Authentication creator, Division usableInfluenceDivision, Long minBid, Long minStep) {

		validateAuction(beginningTimestamp, endingTimestamp, title, description, creator, usableInfluenceDivision,
				minBid, minStep);

		Auction auction = new Auction();
		auction.setBeginningTimestamp(beginningTimestamp);
		auction.setEndingTimestamp(endingTimestamp);
		auction.setTitle(title);
		auction.setDescription(description);
		auction.setHighestBidder(corporateerHandlingService.getCorporateerByName("Tag-Bot"));
		auction.setCreator(userHandlingService.getUserByName(creator.getName()).getCorporateer());
		auction.setUsableInfluenceDivision(usableInfluenceDivision);
		auction.setMinBid(minBid);
		auction.setMinStep(minStep);
		auction.setHighestBid(0L);
		auction.setCurrentBid(0L);

		auctionRepository.save(auction);
	}

	public void createAuction(String beginningTimestamp, String endingTimestamp, String title, String description,
			Authentication creator, String division, String department) {
		createAuction(beginningTimestamp, endingTimestamp, title, description, creator, objectService.getDivisionByNameAndDepartment(
				division, objectService.getDepartmentByName(department)), 1L,
				1L);
	}

	public boolean bidOnAuction(Long id, Authentication authentication, Long bid) {

		Auction auction = auctionRepository.findById(id)
				.orElseThrow(() -> new RepositoryNotFoundException("Auction not found."));

		Corporateer bidder = userHandlingService.getUserByName(authentication.getName()).getCorporateer();

		validateBid(auction, bidder, bid);

		if (bid >= auction.getHighestBid() + auction.getMinStep()) {

			// deduct influence from successful bidder
			Influence bidderInfluence = influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(bidder,
					auction.getUsableInfluenceDivision(), objectService.getInfluenceTypeByName("INFLUENCE"));

			bidderInfluence.setAmount(bidderInfluence.getAmount() - bid.intValue());
			influenceHandlingService.updateInfluence(bidderInfluence);
			bidder.setTotalInfluence(corporateerHandlingService.getTotalInfluence(bidder));
			corporateerHandlingService.updateCorporateer(bidder);

			// refund previous highest bidder
			Influence previousBidderInfluence = influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(
					auction.getHighestBidder(), auction.getUsableInfluenceDivision(),
					objectService.getInfluenceTypeByName("INFLUENCE"));

			previousBidderInfluence.setAmount(previousBidderInfluence.getAmount() + auction.getHighestBid().intValue());
			influenceHandlingService.updateInfluence(previousBidderInfluence);
			auction.getHighestBidder()
					.setTotalInfluence(corporateerHandlingService.getTotalInfluence(auction.getHighestBidder()));
			corporateerHandlingService.updateCorporateer(auction.getHighestBidder());

			// set new values for auction
			auction.setHighestBidder(bidder);
			auction.setCurrentBid(auction.getHighestBid() + auction.getMinStep());
			auction.setHighestBid(bid);

			auctionRepository.save(auction);

			return true;
		}

		else {
			auction.setCurrentBid(bid);
			auctionRepository.save(auction);
			return false;
		}

	}

	private void validateBid(Auction auction, Corporateer bidder, Long bid) {

		// Auction is open
		if (Instant.now().isBefore(Instant.parse(auction.getBeginningTimestamp()))
				&& Instant.now().isAfter(Instant.parse(auction.getBeginningTimestamp()))) {
			throw new IllegalBidException("Auction is not open, you cannot bid.");
		}

		// Bid is high enough
		if (bid < auction.getCurrentBid() + auction.getMinStep()) {
			throw new IllegalBidException("Your bid is too small, you need to bit at least " + auction.getCurrentBid()
					+ auction.getMinStep() + ".");
		}

		// Corporateer is able to bid
		if (influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(bidder,
				auction.getUsableInfluenceDivision(), objectService.getInfluenceTypeByName("INFLUENCE"))
				.getAmount() < bid) {
			throw new IllegalBidException("You do not have enough influence to bid this amount.");
		}

		// The creator may not bid on his own auction
		// if (auction.getCreator().getId().equals(bidder.getId())) {
		// throw new IllegalBidException("The creator of an auction is not able to
		// bid.");
		// }

	}

	public List<Auction> getAllAuctions() {
		return auctionRepository.findAll();
	}

	public List<Auction> getBiddableAuctions() {
		List<Auction> allAuctions = auctionRepository.findAll().stream()
				.filter(auction -> Instant.parse(auction.getBeginningTimestamp()).isBefore(Instant.now()))
				.filter(auction -> Instant.parse(auction.getEndingTimestamp()).isAfter(Instant.now()))
				.collect(Collectors.toList());
		return allAuctions;
	}

	public void resolveAuction(Long id) {

		Auction auction = auctionRepository.findById(id)
				.orElseThrow(() -> new RepositoryNotFoundException("Auction not found."));

		// get influence to refund
		Long excessInfluence = auction.getHighestBid() - auction.getCurrentBid();

		// refund winner
		Influence winningBidderInfluence = influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(
				auction.getHighestBidder(), auction.getUsableInfluenceDivision(),
				objectService.getInfluenceTypeByName("INFLUENCE"));

		winningBidderInfluence.setAmount(winningBidderInfluence.getAmount() + excessInfluence.intValue());
		influenceHandlingService.updateInfluence(winningBidderInfluence);
		auction.getHighestBidder()
				.setTotalInfluence(corporateerHandlingService.getTotalInfluence(auction.getHighestBidder()));
		corporateerHandlingService.updateCorporateer(auction.getHighestBidder());

		// reset highest bid
		auction.setHighestBid(auction.getCurrentBid());
		auctionRepository.save(auction);
	}

}
