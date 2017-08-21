package com.thecorporateer.influence.controllers;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.thecorporateer.influence.objects.User;
import com.thecorporateer.influence.repositories.DivisionRepository;
import com.thecorporateer.influence.repositories.UserRepository;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.UserHandlingService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
public class UserController {

	@Autowired
	CorporateerHandlingService corporateerHandlingService;

	@Autowired
	UserHandlingService userHandlingService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DivisionRepository divisionRepository;

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/currentUser", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsCurrentUser(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.UserProfile.class)
	@RequestMapping(method = RequestMethod.GET, value = "/currentUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> currentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return ResponseEntity.ok().body(userRepository.findByUsername(currentPrincipalName));
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/currentCorporateer", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsCurrentCorporateer(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.CorporateerProfile.class)
	@RequestMapping(method = RequestMethod.GET, value = "/currentCorporateer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> currentCorporateer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return ResponseEntity.ok().body(userRepository.findByUsername(currentPrincipalName).getCorporateer());
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/changePassword", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsChangePassword(HttpServletResponse response) {
		response.setHeader("Allow", "POST,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		User currentUser = userRepository.findByUsername(currentPrincipalName);

		if (!userHandlingService.checkOldPassword(currentUser, request.getOldPassword())) {
			return ResponseEntity.badRequest().body("{\"reason\":\"wrong password\"}");
		}

		if (!userHandlingService.changePassword(currentUser, request.getNewPassword())) {
			return ResponseEntity.badRequest().body("{\"reason\":\"password complexity requirements violated\"}");
		}
		return ResponseEntity.ok().body("{\"message\":\"password successfully changed\"}");
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/setMyMainDivision", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsSetMyMainDivision(HttpServletResponse response) {
		response.setHeader("Allow", "POST,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/setMyMainDivision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setMyMainDivision(@RequestBody String division) throws JSONException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		User currentUser = userRepository.findByUsername(currentPrincipalName);

		corporateerHandlingService.setMainDivision(currentUser.getCorporateer(),
				divisionRepository.findByName(new JSONObject(division).getString("division")));
		return ResponseEntity.ok().body("{\"message\":\"division successfully changed\"}");
	}
}

@Getter
@AllArgsConstructor
class PasswordChangeRequest {
	String oldPassword;
	String newPassword;
}