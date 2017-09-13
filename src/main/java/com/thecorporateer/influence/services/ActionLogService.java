package com.thecorporateer.influence.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.ActionLog;
import com.thecorporateer.influence.repositories.ActionLogRepository;

/**
 * @author Zollak
 * 
 *         Service to handle interactions with ActionLog entities
 *
 */
@Service
public class ActionLogService {

	@Autowired
	private ActionLogRepository actionLogRepository;
	@Autowired
	private UserHandlingService userHandlingService;

	public boolean logAction(Authentication authentication, String action) {
		ActionLog log = new ActionLog();
		log.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
		log.setUser(userHandlingService.getUserByName(authentication.getName()));
		log.setAction(action);

		actionLogRepository.save(log);
		return true;
	}

	public List<ActionLog> getAllLogs() {
		return actionLogRepository.findAll();
	}

}
