package com.thecorporateer.influence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication(scanBasePackages = { "com.thecorporateer.influence" })
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void initialize() {
	}

	// Going to application.properties and setting log level of
	// logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter
	// to DEBUG activates logging of every request
	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(true);
		return loggingFilter;
	}
}
