package com.thecorporateer.influence.exceptions;

import java.time.Instant;

import org.json.JSONException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
	public ResponseEntity<?> userNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "User not found", req.getServletPath()));
	}

	@ExceptionHandler(value = CorporateerNotFoundException.class)
	public ResponseEntity<?> corporateerNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Corporateer not found", req.getServletPath()));
	}

	@ExceptionHandler(value = DivisionNotFoundException.class)
	public ResponseEntity<?> divisionNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Division not found", req.getServletPath()));
	}

	@ExceptionHandler(value = DepartmentNotFoundException.class)
	public ResponseEntity<?> departmentNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Department not found", req.getServletPath()));
	}

	@ExceptionHandler(value = RankNotFoundException.class)
	public ResponseEntity<?> rankNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Rank not found", req.getServletPath()));
	}

	@ExceptionHandler(value = InfluenceNotFoundException.class)
	public ResponseEntity<?> influenceNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Influence not found", req.getServletPath()));
	}

	@ExceptionHandler(value = InfluenceTypeNotFoundException.class)
	public ResponseEntity<?> influenceTypeNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Influence type not found", req.getServletPath()));
	}

	@ExceptionHandler(value = JSONException.class)
	public ResponseEntity<?> JSONErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Malformed request", req.getServletPath()));
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<?> HTTPErrorhandler(HttpServletRequest req, Exception e) {

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Malformed request", req.getServletPath()));
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
