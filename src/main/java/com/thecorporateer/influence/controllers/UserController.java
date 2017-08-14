package com.thecorporateer.influence.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.thecorporateer.influence.security.UserRepository;
import com.thecorporateer.influence.services.CorporateerHandlingService;

@RestController
public class UserController {
	
	@Autowired
	CorporateerHandlingService corporateerHandlingService;

	@Autowired
	private UserRepository userRepository;

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/currentUser", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> options(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.UserProfile.class)
	@RequestMapping(method = RequestMethod.GET, value = "/currentUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> currentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return new ResponseEntity<>(userRepository.findByUsername(currentPrincipalName), HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/currentCorporateer", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsCurrentCorporateer(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.CorporateerProfile.class)
	@RequestMapping(method = RequestMethod.GET, value = "/currentCorporateer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> currentCorporateer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return new ResponseEntity<>(userRepository.findByUsername(currentPrincipalName).getCorporateer(),
				HttpStatus.OK);
	}
}