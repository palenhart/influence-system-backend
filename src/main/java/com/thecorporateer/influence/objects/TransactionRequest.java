package com.thecorporateer.influence.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransactionRequest {

	private String receiver;
	private Integer amount;
	private String message;
	private String type;

}
