package com.thecorporateer.influence.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  JwtAuthenticationRequest implements Serializable {

	private static final long serialVersionUID = 2247050855209777234L;
	
	private String username;
    private String password;

}