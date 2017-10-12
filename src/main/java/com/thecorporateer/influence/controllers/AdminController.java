package com.thecorporateer.influence.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.ObjectService;
import com.thecorporateer.influence.services.UserHandlingService;

@RestController
public class AdminController {

	@Autowired
	private CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private ActionLogService actionLogService;
	@Autowired
	private ObjectService objectService;

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/distributeTributes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> distributeTributes() {

		corporateerHandlingService.distributeTributes();
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(), "Tributes distributed");

		return ResponseEntity.ok().body("{\"message\":\"Tribute distribution successful\"}");
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody ObjectNode request) {

		String username = request.get("name").asText();

		userHandlingService.createUser(username, objectService.getDefaultDivision(), new ArrayList<String>());
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(), "Created user " + username);

		return ResponseEntity.ok().body("{\"message\":\"User successfully created\"}");
	}
}
