package com.thecorporateer.influence.exceptions;

@SuppressWarnings("serial")
public class IllegalTransferRequestException extends RuntimeException {
	
	public IllegalTransferRequestException(String message) {
        super(message);
    }

}
