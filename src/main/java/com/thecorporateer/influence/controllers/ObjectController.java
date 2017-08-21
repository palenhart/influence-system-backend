package com.thecorporateer.influence.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.thecorporateer.influence.repositories.DivisionRepository;

@RestController
public class ObjectController {
	
	@Autowired
	private DivisionRepository divisionRepository;
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getDivisions", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsGetDivisions(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.Corporateer.class)
	@RequestMapping(method = RequestMethod.GET, value = "/getDivisions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDivisions() {
		return ResponseEntity.ok().body(divisionRepository.findAll());
	}

}
