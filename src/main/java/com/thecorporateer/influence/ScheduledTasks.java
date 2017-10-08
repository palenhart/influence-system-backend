package com.thecorporateer.influence;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	// TODO implement scheduled function calls like tribute distribution
	@Scheduled(fixedRate = 60000)
	public void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	}
}