package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.exceptions.IllegalBidException;
import com.thecorporateer.influence.objects.Auction;
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Division;
import com.thecorporateer.influence.repositories.AuctionRepository;

@Service
public class AuctionService {

	@Autowired
	private UserHandlingService userHandlingService;
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
		auction.setCreator(userHandlingService.getUserByName(creator.getName()).getCorporateer());
		auction.setUsableInfluenceDivision(usableInfluenceDivision);
		auction.setMinBid(minBid);
		auction.setMinStep(minStep);
		auction.setHighestBid(0L);
		auction.setCurrentBid(0L);

		auctionRepository.save(auction);
	}

	public void createAuction(String beginningTimestamp, String endingTimestamp, String title, String description,
			Authentication creator, Division usableInfluenceDivision) {
		createAuction(beginningTimestamp, endingTimestamp, title, description, creator, usableInfluenceDivision, 1L,
				1L);
	}

	public void bidOnAuction(Auction auction, Authentication authentication, Long bid) {

		Corporateer bidder = userHandlingService.getUserByName(authentication.getName()).getCorporateer();

		validateBid(auction, bidder, bid);

		auction.setHighestBidder(bidder);
		auction.setCurrentBid(auction.getHighestBid() + auction.getMinStep());
		auction.setHighestBid(bid);

	}

	private void validateBid(Auction auction, Corporateer bidder, Long bid) {

		// Auction is open
		if (Instant.now().isBefore(Instant.parse(auction.getBeginningTimestamp()))
				&& Instant.now().isAfter(Instant.parse(auction.getBeginningTimestamp()))) {
			throw new IllegalBidException("Auction is not open.");
		}

		// Bid is high enough
		if (bid < auction.getCurrentBid() + auction.getMinStep()) {
			throw new IllegalBidException("Bid is too small.");
		}

		// Corporateer is able to bid
		if (influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(bidder,
				auction.getUsableInfluenceDivision(), objectService.getInfluenceTypeByName("INFLUENCE"))
				.getAmount() < bid) {
			throw new IllegalBidException("Not enough influence to bid.");
		}

		// The creator may not bid on his own auction
		if (auction.getCreator().getId().equals(bidder.getId())) {
			throw new IllegalBidException("The creator of an auction is not able to bid.");
		}

	}

	public List<Auction> getAllAuctions() {
		return auctionRepository.findAll();
	}

}
