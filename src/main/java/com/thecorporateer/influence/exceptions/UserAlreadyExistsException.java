package com.thecorporateer.influence.exceptions;

@SuppressWarnings("serial")
public class UserAlreadyExistsException extends RuntimeException {
	
	public UserAlreadyExistsException(String message) {
        super(message);
    }

}
