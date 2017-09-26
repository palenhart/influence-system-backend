package com.thecorporateer.influence.exceptions;

@SuppressWarnings("serial")
public class PasswordComplexityException extends RuntimeException {
	
	public PasswordComplexityException(String message) {
        super(message);
    }

}
