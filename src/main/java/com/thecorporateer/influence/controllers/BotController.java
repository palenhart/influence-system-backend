package com.thecorporateer.influence.controllers;

import java.util.ArrayList;
import java.util.List;

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
import com.thecorporateer.influence.objects.Influence;
import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.ObjectService;
import com.thecorporateer.influence.services.TransactionService;
import com.thecorporateer.influence.services.UserHandlingService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
public class BotController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private ActionLogService actionLogService;
	@Autowired
	private CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private ObjectService objectService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/bottransfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTransaction(@RequestBody BotTransactionRequest request) {

		transactionService.botTransfer(request.getSender(), request.getReceiver(), request.getAmount(),
				request.getType().toUpperCase());
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
				"Influence transfer by bot from " + request.getSender() + " to " + request.getReceiver());

		return ResponseEntity.ok().body("{\"message\":\"Transaction successful\"}");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/bottributes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTributes(@RequestBody ObjectNode request) {

		String corporateer = request.get("name").asText();
		int tributes = corporateerHandlingService.getCorporateerByName(corporateer).getTributes();

		return ResponseEntity.ok()
				.body("{\"message\":\"User " + corporateer + " currently has " + tributes + " tributes.\"}");
	}

	/**
	 * 
	 * Request to show a corporateer's current influence
	 * 
	 * @return Array of influence as JSON object
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/botinfluences", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentInfluences(@RequestBody ObjectNode request) {

		String corporateerName = request.get("name").asText();

		List<InfluenceResponse> response = new ArrayList<InfluenceResponse>();

		for (Influence influence : corporateerHandlingService.getCorporateerByName(corporateerName).getInfluence()) {
			if (influence.getType().getName().equals("INFLUENCE")) {
				response.add(new InfluenceResponse(influence.getDivision().getName(),
						influence.getDivision().getDepartment().getName(), influence.getAmount()));
			}
		}

		return ResponseEntity.ok().body(response);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/resetpassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> resetPassword(@RequestBody ObjectNode request) {

		String username = request.get("name").asText();

		String password = userHandlingService.resetPassword(username);

		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
				"Password reset for " + request.get("name").asText() + ".");

		return ResponseEntity.ok()
				.body("{\"message\":\"Password has been successfully changed to " + password + ".\"}");
	}

	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/botuser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody DiscordUserRequest request) {

		String password = userHandlingService.createUser(request.getName(), objectService.getDefaultDivision(),
				request.getId(), request.getDivisions());
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(),
				"Created user " + request.getName());

		return ResponseEntity.ok().body(
				"{\"message\":\"User successfully created. Login can be found at https://influence.thecorporateer.com\",\"username\":\""
						+ request.getName() + "\",\"password\":\"" + password + "\"}");
	}

}

/**
 * @author Zollak
 *
 *         Request object to create a transaction via bot
 *
 */
@Getter
@AllArgsConstructor
class BotTransactionRequest {

	private String sender;
	private String receiver;
	private Integer amount;
	private String type;

}