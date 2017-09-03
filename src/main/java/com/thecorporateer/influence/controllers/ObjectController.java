package com.thecorporateer.influence.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.TransactionRepository;
import com.thecorporateer.influence.repositories.UserRepository;

@RestController
public class ObjectController {

	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private CorporateerRepository corporateerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionRepository transactionRepository;

	@CrossOrigin(origins = "*")
	@JsonView(Views.Public.class)
	@RequestMapping(method = RequestMethod.GET, value = "/divisions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDivisions() {
		return ResponseEntity.ok().body(divisionRepository.findAll());
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
	@RequestMapping(value = "/transactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTransactions() {
		return ResponseEntity.ok().body(transactionRepository.findAll());
	}

}
