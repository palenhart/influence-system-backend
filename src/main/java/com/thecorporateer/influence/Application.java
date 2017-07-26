package com.thecorporateer.influence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.thecorporateer.influence.services.InitializationService;

@SpringBootApplication(scanBasePackages = { "com.thecorporateer.influence" })
public class Application {

	@Autowired
	InitializationService creator;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void initialize() {
		creator.initialize();
	}
}
