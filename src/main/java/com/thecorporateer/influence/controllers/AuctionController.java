package com.thecorporateer.influence.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thecorporateer.influence.objects.Auction;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.repositories.AuctionRepository;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.AuctionService;
import com.thecorporateer.influence.services.InfluenceHandlingService;
import com.thecorporateer.influence.services.ObjectService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
public class AuctionController {

	@Autowired
	private AuctionService auctionService;
	@Autowired
	private ObjectService objectService;
	@Autowired
	private ActionLogService actionLogService;

	@Autowired
	private InfluenceHandlingService influenceHandlingService;
	@Autowired
	private AuctionRepository auctionRepository;

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/createAuction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuction(@RequestBody AuctionRequest auction) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		auctionService.createAuction(auction.getBeginningTimestamp(), auction.getEndingTimestamp(), auction.getTitle(),
				auction.getDescription(), authentication, objectService.getDivisionByNameAndDepartment(
						auction.getDivision(), objectService.getDepartmentByName(auction.getDepartment())));

		actionLogService.logAction(authentication, "Auction created");

		return ResponseEntity.ok().body("{\"message\":\"Auction creation successful\"}");
	}

	@CrossOrigin(origins = "*")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/auctions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentAuctions() {

		List<AuctionLimitedResponse> response = new ArrayList<AuctionLimitedResponse>();

		for (Auction auction : auctionService.getBiddableAuctions()) {
			response.add(new AuctionLimitedResponse(auction.getId(), auction.getBeginningTimestamp(),
					auction.getEndingTimestamp(), auction.getTitle(), auction.getDescription(),
					auction.getHighestBidder().getName(), auction.getCreator().getName(),
					auction.getUsableInfluenceDivision().getDepartment().getName(),
					auction.getUsableInfluenceDivision().getName(), auction.getCurrentBid(), auction.getMinBid(),
					auction.getMinStep()));
		}

		return ResponseEntity.ok().body(response);
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/allauctions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllAuctions() {

		List<AuctionResponse> response = new ArrayList<AuctionResponse>();

		for (Auction auction : auctionService.getAllAuctions()) {
			response.add(new AuctionResponse(auction.getId(), auction.getBeginningTimestamp(),
					auction.getEndingTimestamp(), auction.getTitle(), auction.getDescription(),
					auction.getHighestBidder().getName(), auction.getCreator().getName(),
					auction.getUsableInfluenceDivision().getDepartment().getName(),
					auction.getUsableInfluenceDivision().getName(), auction.getHighestBid(), auction.getCurrentBid(),
					auction.getMinBid(), auction.getMinStep()));
		}

		return ResponseEntity.ok().body(response);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/auctions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bidOnAuction(@RequestBody ObjectNode request) {

		Long id = request.get("id").asLong();
		Long bid = request.get("amount").asLong();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (auctionService.bidOnAuction(id, authentication, bid)) {
			actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
					"Bet " + bid + " on auction with id " + id + " and is highest bidder.");
			return ResponseEntity.ok().body("{\"message\": \"Congratulations, you are now the highest bidder!\"}");
		} else {
			actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
					"Bet " + bid + " on auction with id " + id + " but is not highest bidder.");
			return ResponseEntity.ok().body("{\"message\": \"Sorry, you have been outbid.\"}");
		}

	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/resolveAuction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resolveAuction(@RequestBody Long id) {

		// DEBUG Logging
		Auction auction = auctionRepository.findOne(id);
		Influence winningBidderInfluence = influenceHandlingService.getInfluenceByCorporateerAndDivisionAndType(
				auction.getHighestBidder(), auction.getUsableInfluenceDivision(),
				objectService.getInfluenceTypeByName("INFLUENCE"));

		Long excessInfluence = auction.getHighestBid() - auction.getCurrentBid();
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
				"Auction " + auction.getTitle() + " resolved. The winner was " + auction.getHighestBidder().getName()
						+ ", the highest bid was " + auction.getHighestBid() + ", the winning bid was "
						+ auction.getCurrentBid() + ". This lead to a refund of " + excessInfluence
						+ " added to the current influence " + winningBidderInfluence.getAmount() + ".");

		auctionService.resolveAuction(id);

		return ResponseEntity.ok().body("{\"message\": \"Auction successfully resolved.\"}");
	}

}

/**
 * @author Zollak
 *
 *         Request object to create an auction
 *
 */
@Getter
@AllArgsConstructor
class AuctionRequest {

	private String beginningTimestamp;
	private String endingTimestamp;
	private String title;
	private String description;
	private String highestBidder;
	private String department;
	private String division;

}

@Getter
@AllArgsConstructor
class AuctionResponse {

	private Long id;
	private String beginningTimestamp;
	private String endingTimestamp;
	private String title;
	private String description;
	private String highestBidder;
	private String creator;
	private String department;
	private String division;
	private Long highestBid;
	private Long currentBid;
	private Long minBid;
	private Long minStep;
}

@Getter
@AllArgsConstructor
class AuctionLimitedResponse {
	private Long id;
	private String beginningTimestamp;
	private String endingTimestamp;
	private String title;
	private String description;
	private String highestBidder;
	private String creator;
	private String department;
	private String division;
	private Long currentBid;
	private Long minBid;
	private Long minStep;
}
