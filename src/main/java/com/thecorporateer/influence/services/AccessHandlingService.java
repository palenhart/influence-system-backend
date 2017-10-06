package com.thecorporateer.influence.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.JpaEntity;
import com.thecorporateer.influence.repositories.UserRoleRepository;

/**
 * 
 * @author Sicker Typ
 *
 */
@Service
public class AccessHandlingService {

		@Autowired
		private UserHandlingService userHandlingService;
		@Autowired
		private UserRoleRepository userRoleRepository;
		
		public void AccessPermissionVerifier(Authentication authentication, JpaEntity object) {
			
			if(userHandlingService.getUserByName(authentication.getName()).getRoles().contains(userRoleRepository.findByName("ROLE_ADMIN"))) {
				
			}
		}
		
}
