package com.thecorporateer.influence.exceptions;

@SuppressWarnings("serial")
public class IllegalBuyRequestException extends RuntimeException {
	
	public IllegalBuyRequestException(String message) {
        super(message);
    }

}
