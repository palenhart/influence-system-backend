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
import com.thecorporateer.influence.repositories.CorporateerRepository;
import com.thecorporateer.influence.repositories.DivisionRepository;

@RestController
public class ObjectController {

	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private CorporateerRepository corporateerRespository;

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/divisions", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsGetDivisions(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.Corporateer.class)
	@RequestMapping(method = RequestMethod.GET, value = "/divisions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDivisions() {
		return ResponseEntity.ok().body(divisionRepository.findAll());
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/corporateers", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsGetCorporateers(HttpServletResponse response) {
		response.setHeader("Allow", "GET,OPTIONS");
		return ResponseEntity.ok().body(null);
	}

	@CrossOrigin(origins = "*")
	@JsonView(Views.Corporateer.class)
	@RequestMapping(method = RequestMethod.GET, value = "/corporateers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCorporateers() {
		return ResponseEntity.ok().body(corporateerRespository.findAll());
	}

}
