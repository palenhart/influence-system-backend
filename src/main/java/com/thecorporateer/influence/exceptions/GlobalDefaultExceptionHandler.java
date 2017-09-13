package com.thecorporateer.influence.exceptions;

import java.time.Instant;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> defaultErrorhandler(HttpServletRequest req) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Unknown Error", req.getServletPath()));
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> customErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "User not found", req.getServletPath()));
	}

}

@AllArgsConstructor
@Getter
class ExceptionResponse {

	private Long timestamp;
	private String status;
	private String error;
	private String message;
	private String path;
}
