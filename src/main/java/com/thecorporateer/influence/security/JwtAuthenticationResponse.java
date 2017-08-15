package com.thecorporateer.influence.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAuthenticationResponse implements Serializable {

	private static final long serialVersionUID = -2753084662184569300L;
	
	private final String token;

}
