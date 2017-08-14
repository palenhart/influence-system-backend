package com.thecorporateer.influence.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thecorporateer.influence.services.CorporateerHandlingService;

@RestController
public class RegistrationController {

	@Autowired
	private CorporateerHandlingService corporateerHandlingService;

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public void register(@RequestBody String name) {
		
		corporateerHandlingService.createCorporateer(name);
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {

		response.sendError(HttpStatus.BAD_REQUEST.value());

	}
}