package com.thecorporateer.influence.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {

	/**
	 * from ObjectController
	 */

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/divisions", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> divisions(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/corporateers", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> corporateers(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/users", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> users(HttpServletResponse response) {
		response.setHeader("Allow", "POST,GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/transactions", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> transactions(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	/**
	 * from UserController
	 */

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/currentUser", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> currentUser(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/currentCorporateer", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> currentCorporateer(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/currentInfluences", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> currentInfluences(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/changePassword", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> changePassword(HttpServletResponse response) {
		response.setHeader("Allow", "POST,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/setMyMainDivision", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> setMyMainDivision(HttpServletResponse response) {
		response.setHeader("Allow", "POST,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	/**
	 * from TransactionController
	 */

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/transfer", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> transfer(HttpServletResponse response) {
		response.setHeader("Allow", "POST,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/transactions/{direction}", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> getCorporarateersTransactions(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	/**
	 * from AdminController
	 */

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/distributeTributes", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> distributeTributes(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

}
