package com.thecorporateer.influence.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thecorporateer.influence.objects.Influence;
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
	 * @return User as JSON object
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.GET, value = "/currentUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentUser() {

		return ResponseEntity.ok().body(
				userHandlingService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
	}

	/**
	 * 
	 * Request to show one's corporateer
	 * 
	 * @return Corporateer as JSON object
	 */
	@CrossOrigin(origins = "*")
	@JsonView(Views.Private.class)
	@RequestMapping(method = RequestMethod.GET, value = "/currentCorporateer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentCorporateer() {

		return ResponseEntity.ok().body(userHandlingService
				.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName()).getCorporateer());
	}

	/**
	 * 
	 * Request to show one's own current influence
	 * 
	 * @return Array of influence as JSON object
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.GET, value = "/currentInfluences", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentInfluences() {

		List<InfluenceResponse> response = new ArrayList<InfluenceResponse>();

		for (Influence influence : userHandlingService
				.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName()).getCorporateer()
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
	 * @return HTTP Status 200
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changePassword(@RequestBody ObjectNode request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		userHandlingService.changePassword(authentication, request.get("currentPassword").asText(),
				request.get("newPassword").asText());
		actionLogService.logAction(authentication, "Password change");

		return ResponseEntity.ok().body("{\"message\":\"Password successfully changed\"}");
	}

	/**
	 * 
	 * Request to change the corporateer's main division
	 * 
	 * @param division
	 *            The division which should become the new main division
	 * @return HTTP Status 200 (or 400)
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/setMyMainDivision", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> setMyMainDivision(@RequestBody ObjectNode request) {

		corporateerHandlingService.setMainDivision(SecurityContextHolder.getContext().getAuthentication(),
				request.get("name").asText());

		return ResponseEntity.ok().body("{\"message\":\"Division successfully changed\"}");
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/buyRank", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buyRank(@RequestBody ObjectNode request) throws JSONException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String rankname = request.get("name").asText();

		corporateerHandlingService.buyRank(authentication, rankname);
		actionLogService.logAction(authentication, "Bought rank " + rankname);

		return ResponseEntity.ok().body("{\"message\":\"Rank successfully bought\"}");
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(method = RequestMethod.POST, value = "/setMembership", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addRemoveMember(@RequestBody ObjectNode request) throws JSONException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String corporateerName = request.get("corporateer").asText();
		String divisionName = request.get("division").asText();
		Boolean add = request.get("add").asBoolean();

		corporateerHandlingService.changeCorporateerDivisionMembership(authentication, corporateerName, divisionName,
				add);

		if (add) {
			actionLogService.logAction(authentication, "Added " + corporateerName + " to " + divisionName);
		} else {
			actionLogService.logAction(authentication, "Removed " + corporateerName + " from " + divisionName);
		}

		return ResponseEntity.ok().body("{\"message\":\"Rank successfully bought\"}");
	}
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