package com.thecorporateer.influence.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> defaultErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Unknown Error", req.getServletPath()));
	}

	@ExceptionHandler(value = RepositoryNotFoundException.class)
	public ResponseEntity<?> repositoryNotFoundErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = IllegalBuyRequestException.class)
	public ResponseEntity<?> illegalBuyRequestErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = IllegalDivisionChangeRequestException.class)
	public ResponseEntity<?> illegalDivisionChangeRequestErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = IllegalInfluenceConversionException.class)
	public ResponseEntity<?> illegalInfluenceConversionErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = IllegalTransferRequestException.class)
	public ResponseEntity<?> illegalTransferRequestErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = JSONException.class)
	public ResponseEntity<?> JSONErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Malformed request", req.getServletPath()));
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<?> HTTPErrorhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", "Malformed request", req.getServletPath()));
	}

	@ExceptionHandler(value = PasswordComplexityException.class)
	public ResponseEntity<?> PasswordComplexityhandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = IllegalMembershipChangeException.class)
	public ResponseEntity<?> DivisionMembershipHandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = UserAlreadyExistsException.class)
	public ResponseEntity<?> UserAlreadyExistsHandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	@ExceptionHandler(value = IllegalBidException.class)
	public ResponseEntity<?> IllegalBidHandler(HttpServletRequest req, Exception e) {

		logException(req, e);

		return ResponseEntity.badRequest().body(new ExceptionResponse(Instant.now().getEpochSecond(), "400",
				"Bad Request", e.getMessage(), req.getServletPath()));
	}

	private void logException(HttpServletRequest req, Exception e) {
		log.warn(e.getClass().getName());

		log.warn("User: " + req.getUserPrincipal().getName());

		log.warn("Request path: " + req.getServletPath());

		for (StackTraceElement element : e.getStackTrace()) {
			log.warn(element.toString());
		}
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
