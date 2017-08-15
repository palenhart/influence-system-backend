/**
 * 
 */
package com.thecorporateer.influence.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thecorporateer.influence.objects.User;
import com.thecorporateer.influence.repositories.UserRepository;

/**
 * @author Zollak
 * 
 *         Service handling actions concerning the User entity
 *
 */
@Service
public class UserHandlingService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean checkOldPassword(User user, String password) {
		if (passwordEncoder.matches(password, user.getPassword())) {
			return true;
		}
		return false;
	}

	public boolean changePassword(User user, String newPassword) {
		// TODO: check password complexity rules
//		if(false) {
//			return false;
//		}
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		return true;
	}
}
