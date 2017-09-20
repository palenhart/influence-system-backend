package com.thecorporateer.influence.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.ObjectService;
import com.thecorporateer.influence.services.TransactionService;
import com.thecorporateer.influence.services.UserHandlingService;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zollak
 *
 */
@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private ActionLogService actionLogService;
	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private ObjectService objectService;

	/**
	 * 
	 * Requests to transfer influence to another corporateer
	 * 
	 * @param request
	 *            The transaction which should be executed as TransactionRequest for
	 *            the authenticated user
	 * @return HTTP response 200 or 400 (exception)
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/transfer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> transfer(@RequestBody TransactionRequest request) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		transactionService.transfer(authentication, request.getReceiver(), request.getMessage(), request.getAmount(),
				request.getType().toUpperCase());
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(), "Influence transfer");
		
		return ResponseEntity.ok().body("{\"message\":\"Transaction successful\"}");
	}

	/**
	 * 
	 * Requests a list of incoming/outgoing transactions for the user
	 * 
	 * @param direction
	 *            The direction of transactions to get for the authenticated user
	 *            (sender/receiver)
	 * @return List of transactions or HTTP Error 400 (exception)
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/transactions/{direction}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCorporarateersTransactions(@PathVariable("direction") String direction) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		List<TransactionResponse> response = new ArrayList<TransactionResponse>();

		if (direction.equals("sender")) {
			for (Transaction transaction : userHandlingService.getUserByName(currentPrincipalName).getCorporateer()
					.getSentTransactions()) {
				response.add(new TransactionResponse(transaction.getTimestamp(), transaction.getSender().getName(),
						transaction.getReceiver().getName(), transaction.getAmount(), transaction.getType().getName(),
						transaction.getMessage(), transaction.getDivision().getName(),
						transaction.getDivision().getDepartment().getName()));
			}
			return ResponseEntity.ok().body(response);
		}

		else if (direction.equals("receiver")) {
			for (Transaction transaction : userHandlingService.getUserByName(currentPrincipalName).getCorporateer()
					.getReceivedTransactions()) {
				// do not show demerits
				// TODO: Refactor to not use the name
				if (transaction.getType().equals(objectService.getInfluenceTypeByName("INFLUENCE"))) {
					response.add(new TransactionResponse(transaction.getTimestamp(), transaction.getSender().getName(),
							transaction.getReceiver().getName(), transaction.getAmount(),
							transaction.getType().getName(), transaction.getMessage(),
							transaction.getDivision().getName(), transaction.getDivision().getDepartment().getName()));
				}
			}
			return ResponseEntity.ok().body(response);
		}

		return ResponseEntity.notFound().build();
	}

}

/**
 * @author Zollak
 *
 *         Request object to create a transaction from
 *
 */
@Getter
@AllArgsConstructor
class TransactionRequest {

	private String receiver;
	private Integer amount;
	private String message;
	private String type;

}

/**
 * @author Zollak
 *
 *         Response object giving transaction information to show the user
 *
 */
@Getter
@AllArgsConstructor
class TransactionResponse {

	public TransactionResponse(String timestamp, String sender, String receiver, int amount, String type,
			String message, String division, String department) {
		this.timestamp = timestamp;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.division = division;
		this.department = department;
	}

	String timestamp;
	String sender;
	String receiver;
	int amount;
	String type;
	String message;
	String division;
	String department;
	String receivingDivision;

}
