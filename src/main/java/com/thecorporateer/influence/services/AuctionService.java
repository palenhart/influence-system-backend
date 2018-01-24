package com.thecorporateer.influence.services;

import java.time.Instant;

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
			Corporateer creator, Division usableInfluenceDivision, Long minBet, Long minStep) {

		// values must not be null
		if (null == beginningTimestamp || null == endingTimestamp || null == title || null == description
				|| null == creator || null == usableInfluenceDivision || null == minBet || null == minStep) {
			return false;
		}

		// beginningTimestamp
		// endingTimestamp
		// title
		// description
		// creator
		// usableInfluenceDivision
		// minBet
		// minStep

		return true;
	}

	// create auction with all customizable properties
	public void createAuction(String beginningTimestamp, String endingTimestamp, String title, String description,
			Corporateer creator, Division usableInfluenceDivision, Long minBet, Long minStep) {

		validateAuction(beginningTimestamp, endingTimestamp, title, description, creator, usableInfluenceDivision,
				minBet, minStep);

		Auction auction = new Auction();
		auction.setBeginningTimestamp(beginningTimestamp);
		auction.setEndingTimestamp(endingTimestamp);
		auction.setTitle(title);
		auction.setDescription(description);
		auction.setCreator(creator);
		auction.setUsableInfluenceDivision(usableInfluenceDivision);
		auction.setMinBet(minBet);
		auction.setMinStep(minStep);
		auction.setHighestBid(0L);
		auction.setCurrentBid(0L);

		auctionRepository.save(auction);
	}

	public void createAuction(String beginningTimestamp, String endingTimestamp, String title, String description,
			Corporateer creator, Division usableInfluenceDivision) {
		createAuction(beginningTimestamp, endingTimestamp, title, description, creator, usableInfluenceDivision, 1L,
				1L);
	}

	public void bidOnAuction(Auction auction, Authentication authentication, Long bid) {

		Corporateer bidder = userHandlingService.getUserByName(authentication.getName()).getCorporateer();

		validateBid(auction, bidder, bid);

		auction.setHighestBidder(userHandlingService.getUserByName(authentication.getName()).getCorporateer());
		auction.setCurrentBid(auction.getHighestBid() + auction.getMinStep());
		auction.setHighestBid(bid);

	}

	private void validateBid(Auction auction, Corporateer bidder, Long bid) {

		// Auction is open
		if (Instant.now().isBefore(Instant.parse(auction.getBeginningTimestamp()))
				|| Instant.now().isAfter(Instant.parse(auction.getBeginningTimestamp()))) {
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

	}

}
