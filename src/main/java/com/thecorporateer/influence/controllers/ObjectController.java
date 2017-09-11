package com.thecorporateer.influence.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
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
import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DepartmentRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.InfluenceRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.repositories.RankRepository;
import com.thecorporateer.influence.repositories.TransactionRepository;
import com.thecorporateer.influence.repositories.UserRepository;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.InfluenceHandlingService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
public class ObjectController {

	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private InfluenceRepository influenceRepository;
	@Autowired
	private InfluenceTypeRepository influencetypeRepository;
	@Autowired
	private InfluenceHandlingService influencehandlingService;
	@Autowired
	private ActionLogService actionLogService;

	@CrossOrigin(origins = "*")
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, value = "/divisions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDivisions() {
		return ResponseEntity.ok().body(divisionRepository.findAll());
	}
	
	@CrossOrigin(origins = "*")
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, value = "/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRanks() {
		return ResponseEntity.ok().body(rankRepository.findAll());
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, value = "/corporateers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCorporateers() {
		return ResponseEntity.ok().body(corporateerRepository.findAll());
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok().body(userRepository.findAll());
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/transactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllTransactions() {
		List<TransactionResponse> response = new ArrayList<TransactionResponse>();
		for (Transaction transaction : transactionRepository.findAll()) {
			response.add(new TransactionResponse(transaction.getTimestamp(), transaction.getSender().getName(),
					transaction.getReceiver().getName(), transaction.getAmount(), transaction.getType().getName(),
					transaction.getMessage(), transaction.getDivision().getName(),
					transaction.getDivision().getDepartment().getName(), transaction.getReceivingDivision().getName()));
		}
		return ResponseEntity.ok().body(response);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/convertInfluence", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convertInfluence(@RequestBody ObjectNode request) throws JSONException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Corporateer corporateer = userRepository.findByUsername(authentication.getName()).getCorporateer();

		ConversionRequest conversionRequest = new ConversionRequest(request.get("influence"));
		boolean toGeneral = request.get("toGeneral").asBoolean();

		Influence influence = influenceRepository.findByCorporateerAndDivisionAndType(corporateer,
				divisionRepository.findByNameAndDepartment(conversionRequest.getDivision(),
						departmentRepository.findByName(conversionRequest.getDepartment())),
				influencetypeRepository.findOne(1L));

		// do not convert more influence than available
		if (influence.getAmount() < conversionRequest.getAmount()) {
			return ResponseEntity.badRequest().body("{\"reason\":\"You don't have enough influence to convert\"}");
		}

		boolean result = influencehandlingService.convertInfluence(influence, conversionRequest.getAmount(), toGeneral);

		if (result) {
			actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
					"Influence conversion");
			return ResponseEntity.ok().body("{\"message\":\"Conversion successful\"}");
		} else {
			return ResponseEntity.badRequest().body("{\"reason\":\"Conversion failed\"}");
		}
	}

}

@Getter
@AllArgsConstructor
class ConversionRequest {

	public ConversionRequest(JsonNode conversionRequest) throws JSONException {
		this.department = conversionRequest.get("department").asText();
		this.division = conversionRequest.get("division").asText();
		this.amount = conversionRequest.get("amount").asInt();
	}

	String department;
	String division;
	int amount;
}
