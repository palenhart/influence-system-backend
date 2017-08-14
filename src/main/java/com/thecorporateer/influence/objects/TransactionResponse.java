package com.thecorporateer.influence.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransactionResponse {
	
	String timestamp;
	String sender;
	String receiver;
	int amount;
	String type;
	String message;
	String division;
	String department;

}
