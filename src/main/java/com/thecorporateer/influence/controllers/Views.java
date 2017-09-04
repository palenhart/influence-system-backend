package com.thecorporateer.influence.controllers;

/**
 * @author Zollak
 * 
 * Views to control which properties are in responses
 *
 */
public class Views {

	public static class Public {
	}

	public static class Private extends Public {
	}
	
	public static class Admin extends Private {
		
	}
	
}