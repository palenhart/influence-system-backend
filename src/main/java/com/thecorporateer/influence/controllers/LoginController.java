//package com.thecorporateer.influence.controllers;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.thecorporateer.influence.objects.Corporateer;
//import com.thecorporateer.influence.repositories.CorporateerRepository;
//
//@RestController
//public class LoginController {
//	
//	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
//
//	@Autowired
//	private CorporateerRepository corporateerRepository;
//	
//	@CrossOrigin(origins = "*")
//	@RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
//	public ResponseEntity<?> options(HttpServletResponse response) {
//	    log.info("OPTIONS /login called");
//	    response.setHeader("Allow", "POST,OPTIONS");
//	    return new ResponseEntity<>(HttpStatus.OK);
//	}
//
//	@CrossOrigin(origins = "*")
//	@RequestMapping(method = RequestMethod.POST, value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> login(@RequestBody Corporateer corporateer) {
//		if (StringUtils.isEmpty(corporateer.getName()) || StringUtils.isEmpty(corporateer.getPassword())) {
//			log.info("Empty");
//			log.info(corporateer.getName());
//			log.info(corporateer.getPassword());
//			return new ResponseEntity<>(new Corporateer(), HttpStatus.OK);
//		}
//		if (corporateerRepository.findByNameAndPassword(corporateer.getName(), corporateer.getPassword()) == null) {
//			log.info("Not found");
//			log.info(corporateer.getName());
//			log.info(corporateer.getPassword());
//			return new ResponseEntity<>(new Corporateer(), HttpStatus.OK);
//		}
//		log.info("Found");
//		log.info(corporateer.getName());
//		log.info(corporateer.getPassword());
//		return new ResponseEntity<>(corporateerRepository.findByName(corporateer.getName()), HttpStatus.OK);
//	}
//
//}
