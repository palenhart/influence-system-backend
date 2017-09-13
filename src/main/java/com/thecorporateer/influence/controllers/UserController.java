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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.objects.User;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.UserHandlingService;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zollak
 *
 */
@RestController
public class UserController {

	@Autowired
	private CorporateerHandlingService corporateerHandlingService;

	@Autowired
	private UserHandlingService userHandlingService;

	@Autowired
	private ActionLogService actionLogService;

	/**
	 * 
	 * Request to show one's user
	 * 
	 * @return JSON object User
	 */
	@CrossOrigin(origins = "*")
	@JsonView(Views.Private.class)
	@RequestMapping(method = RequestMethod.GET, value = "/currentUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return ResponseEntity.ok().body(userHandlingService.getUserByName(currentPrincipalName));
	}

	/**
	 * 
	 * Request to show one's corporateer
	 * 
	 * @return JSON object Corporateer
	 */
	@CrossOrigin(origins = "*")
	@JsonView(Views.Private.class)
	@RequestMapping(method = RequestMethod.GET, value = "/currentCorporateer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentCorporateer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return ResponseEntity.ok().body(userHandlingService.getUserByName(currentPrincipalName).getCorporateer());
	}

	/**
	 * 
	 * Request to show one's own current influence
	 * 
	 * @return JSON array of influence
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.GET, value = "/currentInfluences", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentInfluences() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		List<InfluenceResponse> response = new ArrayList<InfluenceResponse>();
		for (Influence influence : userHandlingService.getUserByName(currentPrincipalName).getCorporateer()
				.getInfluence()) {
			if (influence.getType().getName().equals("INFLUENCE")) {
				response.add(new InfluenceResponse(influence.getDivision().getName(),
						influence.getDivision().getDepartment().getName(), influence.getAmount()));
			}
		}
		return ResponseEntity.ok().body(response);
	}

	/**
	 * 
	 * Requests to change user password
	 * 
	 * @param request
	 *            The PasswordChangeRequest containing current and new password
	 * @return HTTP Status 200 or 400
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		User currentUser = userHandlingService.getUserByName(currentPrincipalName);

		if (!userHandlingService.checkCurrentPassword(currentUser, request.getCurrentPassword())) {
			return ResponseEntity.badRequest().body("{\"reason\":\"wrong password\"}");
		}

		if (!userHandlingService.changePassword(currentUser, request.getNewPassword())) {
			return ResponseEntity.badRequest().body("{\"reason\":\"password complexity requirements violated\"}");
		}
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(), "Password change");
		return ResponseEntity.ok().body("{\"message\":\"password successfully changed\"}");
	}

	/**
	 * 
	 * Request to change the corporateer's main division
	 * 
	 * @param division
	 *            The division which should become the new main division
	 * @return HTTP Status 200 (or 400)
	 * @throws JSONException
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/setMyMainDivision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	// TODO: Only allow a subset of divisions if user is able to do change himself
	// TODO: There might be a better way than using JSON just for the sake of it.
	public ResponseEntity<?> setMyMainDivision(@RequestBody ObjectNode request) throws JSONException {
		try {

			if(corporateerHandlingService.setMainDivision(SecurityContextHolder.getContext().getAuthentication(),
					request.get("name").asText())) {
				return ResponseEntity.ok().body("{\"message\":\"division successfully changed\"}");
			}
			else {
				return ResponseEntity.ok().body("{\"message\":\"division did not change\"}");
			}

			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("{\"reason\":\"Bad request\"}");
		}

	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/buyRank", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buyRank(@RequestBody ObjectNode request) throws JSONException {
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();

			String rankname = request.get("name").asText();

			if (corporateerHandlingService.buyRank(currentPrincipalName, rankname)) {
				actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
						"Bought rank " + rankname);
				return ResponseEntity.ok().body("{\"message\":\"Rank successfully bought\"}");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("{\"reason\":\"Bad request\"}");
		}
		return ResponseEntity.badRequest().body("{\"reason\":\"Cannot buy rank\"}");
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody ObjectNode request) throws JSONException {
		try {

			String username = request.get("name").asText();

			if (userHandlingService.createUser(username)) {
				actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
						"Created user " + username);
				return ResponseEntity.ok().body("{\"message\":\"User successfully created\"}");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("{\"reason\":\"Bad request\"}");
		}
		return ResponseEntity.badRequest().body("{\"reason\":\"Cannot buy rank\"}");
	}
}

/**
 * @author Zollak
 * 
 *         Request containing current and new password
 * 
 */
@Getter
@AllArgsConstructor
class PasswordChangeRequest {
	String currentPassword;
	String newPassword;
}

/**
 * @author Zollak
 *
 *         Response containing information about influence object
 *
 */
@Getter
@AllArgsConstructor
class InfluenceResponse {
	private String division;
	private String department;
	private int amount;
}