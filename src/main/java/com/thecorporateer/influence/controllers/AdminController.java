package com.thecorporateer.influence.controllers;

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

import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.ObjectService;
import com.thecorporateer.influence.services.UserHandlingService;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

//	@CrossOrigin(origins = "*")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	@RequestMapping(method = RequestMethod.POST, value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> createUser(@RequestBody ObjectNode request) {
//
//		String username = request.get("name").asText();
//
//		String password = userHandlingService.createUser(username, objectService.getDefaultDivision(),
//				new ArrayList<String>());
//		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(), "Created user " + username);
//
//		return ResponseEntity.ok().body("{\"message\":\"User successfully created\",\"username\":\"" + username
//				+ "\",\"password\":\"" + password + "\"}");
//	}
	
	@CrossOrigin(origins = "*")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
		
		String password = userHandlingService.createUser(request.getName(), objectService.getDefaultDivision(),
				request.getDivisions());
		actionLogService.logAction(SecurityContextHolder.getContext().getAuthentication(), "Created user " + request.getName());

		return ResponseEntity.ok().body("{\"message\":\"User successfully created. Login can be found at https://www.influence-system.tk\",\"username\":\"" + request.getName()
				+ "\",\"password\":\"" + password + "\"}");
	}
}

/**
 * @author Zollak
 *
 *         Request object to create a user
 *
 */
@Getter
@AllArgsConstructor
class UserRequest {

	private String name;
	private List<String> divisions;

}
