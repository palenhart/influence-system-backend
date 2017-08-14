package com.thecorporateer.influence.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.thecorporateer.influence.objects.Corporateer;
import com.thecorporateer.influence.objects.InfluenceType;
import com.thecorporateer.influence.objects.Transaction;
import com.thecorporateer.influence.objects.TransactionRequest;
import com.thecorporateer.influence.objects.TransactionResponse;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.InfluenceTypeRepository;
import com.thecorporateer.influence.security.UserRepository;
import com.thecorporateer.influence.services.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	CorporateerRepository corporateerRepository;
	@Autowired
	InfluenceTypeRepository influenceTypeRepository;

	@Autowired
	TransactionService transactionService;

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/transfer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> transfer(@RequestBody TransactionRequest request) {
		System.out.println(request.getReceiver());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Corporateer senderObject = userRepository.findByUsername(authentication.getName()).getCorporateer();
		Corporateer receiverObject = corporateerRepository.findByName(request.getReceiver());
		InfluenceType typeObject = influenceTypeRepository.findByName(request.getType().toUpperCase());
		System.out.println(typeObject.getName());

		boolean result = transactionService.transfer(senderObject, receiverObject, request.getMessage(),
				request.getAmount(), typeObject);

		if (result) {
			System.out.println("OK");
			return ResponseEntity.ok().body("{\"message\":\"Transaction successful\"}");
		} else {
			System.out.println("FAIL");
			return ResponseEntity.badRequest().body("{\"message\":\"Transaction failed\"}");
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getTransactions/{direction}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTransactions(@PathVariable("direction") String direction) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		List<TransactionResponse> response = new ArrayList<TransactionResponse>();
		if (direction.equals("sender")) {
			for (Transaction transaction : userRepository.findByUsername(currentPrincipalName).getCorporateer()
					.getSentTransactions()) {
				response.add(new TransactionResponse(transaction.getTimestamp(), transaction.getSender().getName(),
						transaction.getReceiver().getName(), transaction.getAmount(), transaction.getType().getName(),
						transaction.getMessage(), transaction.getDivision().getName(),
						transaction.getDepartment().getName()));
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else if (direction.equals("receiver")) {
			for (Transaction transaction : userRepository.findByUsername(currentPrincipalName).getCorporateer()
					.getReceivedTransactions()) {
				response.add(new TransactionResponse(transaction.getTimestamp(), transaction.getSender().getName(),
						transaction.getReceiver().getName(), transaction.getAmount(), transaction.getType().getName(),
						transaction.getMessage(), transaction.getDivision().getName(),
						transaction.getDepartment().getName()));
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		return ResponseEntity.badRequest().body("{\"message\":\"Bad request\"}");
	}
}
