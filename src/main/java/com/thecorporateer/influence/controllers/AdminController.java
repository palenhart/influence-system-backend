package com.thecorporateer.influence.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;

@RestController
public class AdminController {

	@Autowired
	private CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private ActionLogService actionLogService;

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/distributeTributes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> distributeTributes() {
		corporateerHandlingService.distributeTributes();
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(), "Tribute distribution");
		return ResponseEntity.ok().body("{\"message\":\"Tribute distribution successful\"}");
	}
}
