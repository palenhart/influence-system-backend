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

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thecorporateer.influence.objects.ActionLog;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.InfluenceHandlingService;
import com.thecorporateer.influence.services.ObjectService;
import com.thecorporateer.influence.services.TransactionService;
import com.thecorporateer.influence.services.UserHandlingService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
public class ObjectController {

	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private InfluenceHandlingService influencehandlingService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private ActionLogService actionLogService;
	@Autowired
	private ObjectService objectService;

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.GET, value = "/divisions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDivisions() {

		return ResponseEntity.ok().body(objectService.getDivisionsAsResponse());
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.GET, value = "/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRanks() {

		return ResponseEntity.ok().body(objectService.getAllRanks());
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, value = "/corporateers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCorporateers() {

		return ResponseEntity.ok().body(corporateerHandlingService.getAllCorporateers());
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUsers() {

		return ResponseEntity.ok().body(userHandlingService.getAllUsers());
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/transactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllTransactions() {

		List<TransactionResponse> response = new ArrayList<TransactionResponse>();

		for (Transaction transaction : transactionService.getAllTransactions()) {
			response.add(new TransactionResponse(transaction.getTimestamp(), transaction.getSender().getName(),
					transaction.getReceiver().getName(), transaction.getAmount(), transaction.getType().getName(),
					transaction.getMessage(), transaction.getDivision().getName(),
					transaction.getDivision().getDepartment().getName(), transaction.getReceivingDivision().getName()));
		}

		return ResponseEntity.ok().body(response);
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getLogs() {

		List<LogResponse> response = new ArrayList<LogResponse>();

		for (ActionLog log : actionLogService.getAllLogs()) {
			if (null != log.getUser()) {
				response.add(new LogResponse(log.getTimestamp(), log.getUser().getUsername(), log.getAction()));
			} else {
				response.add(new LogResponse(log.getTimestamp(), "System", log.getAction()));
			}
		}

		return ResponseEntity.ok().body(response);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/convertInfluence", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convertInfluence(@RequestBody ObjectNode request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ConversionRequest conversionRequest = new ConversionRequest(request.get("influence"));

		influencehandlingService.convertInfluence(authentication, conversionRequest.getDivision(),
				conversionRequest.getDepartment(), conversionRequest.getAmount(), request.get("toGeneral").asBoolean());
		actionLogService.logAction(authentication, "Influence conversion");

		return ResponseEntity.ok().body("{\"message\":\"Conversion successful\"}");
	}

}

@Getter
@AllArgsConstructor
class ConversionRequest {

	public ConversionRequest(JsonNode conversionRequest) {
		this.department = conversionRequest.get("department").asText();
		this.division = conversionRequest.get("division").asText();
		this.amount = conversionRequest.get("amount").asInt();
	}

	String department;
	String division;
	int amount;
}

@Getter
@AllArgsConstructor
class LogResponse {

	private String timestamp;
	private String username;
	private String action;
}
