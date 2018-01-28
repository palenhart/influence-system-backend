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

import com.thecorporateer.influence.objects.Auction;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.AuctionService;
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/auctions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAuctions() {

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
