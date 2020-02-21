package com.thecorporateer.influence;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thecorporateer.influence.services.ActionLogService;
import com.thecorporateer.influence.services.CorporateerHandlingService;

@Component
public class ScheduledTasks {

	@Autowired
	CorporateerHandlingService corporateerHandlingService;
	@Autowired
	private ActionLogService actionLogService;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "0 0 0 * * SAT")
	public void distributeTributes() {
		corporateerHandlingService.distributeTributes();
		actionLogService.logAction(null, "Tributes automatically distributed");
	}

}