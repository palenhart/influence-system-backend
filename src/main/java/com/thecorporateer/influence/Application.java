package com.thecorporateer.influence;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.thecorporateer.influence.services.CorporateerHandlingService;
import com.thecorporateer.influence.services.UserHandlingService;

@SpringBootApplication(scanBasePackages = { "com.thecorporateer.influence" })
@EnableScheduling
public class Application {

	@Autowired
	private UserHandlingService userHandlingService;
	@Autowired
	private CorporateerHandlingService corporateerHandlingService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void initialize() {

		// create users for this phase
//		userHandlingService.createTestuser("weyland", "Pete", "P%bm5xb3ZA", true);
//		corporateerHandlingService.setRank("Pete", "CEO");
//		userHandlingService.createTestuser("zollak", "Zollak", "tX&7E§rtAF", true);
//		corporateerHandlingService.setRank("Zollak", "Board Member");
//		userHandlingService.createTestuser("braden12", "braden12", "9cNQqr9$uT", true);
//		corporateerHandlingService.setRank("braden12", "Board Member");
//		userHandlingService.createTestuser("nikmyth", "Nikmyth", "zKLidnH1!6", true);
//		corporateerHandlingService.setRank("Nikmyth", "Board Member");
//		corporateerHandlingService.electionWorkaroundChangeCorporateerDivisionMembership("braden12", "Transport", true);
		

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
