package com.thecorporateer.influence.exceptions;

@SuppressWarnings("serial")
public class RepositoryNotFoundException extends RuntimeException {
	
	public RepositoryNotFoundException(String message) {
        super(message);
    }

}
