/**
 * 
 */
package com.thecorporateer.influence.services;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.UsernameRule;
import org.passay.WhitespaceRule;
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

	/**
	 * 
	 * Checks whether a supplied password matches a user's current password
	 * 
	 * @param user
	 *            The user to check the password for
	 * @param password
	 *            The password from input
	 * @return <code>true</code> if the password is correct; <code>false</code>
	 *         otherwise
	 */
	public boolean checkCurrentPassword(User user, String password) {
		if (passwordEncoder.matches(password, user.getPassword())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Set new password for a user
	 * 
	 * @param user
	 *            The user who will get the password changed
	 * @param newPassword
	 *            The new password to be set
	 * @return <code>true</code> if the password is changed; <code>false</code>
	 *         otherwise
	 */
	public boolean changePassword(User user, String newPassword) {
		if (!validator.validate(new PasswordData(user.getUsername(), newPassword)).isValid()) {
			return false;
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		return true;
	}

	/**
	 * Settings for password validation
	 */
	PasswordValidator validator = new PasswordValidator(
			// length between 8 and 20 characters
			new LengthRule(8, 20),

			// at least one upper-case character
			new CharacterRule(EnglishCharacterData.UpperCase, 1),

			// at least one lower-case character
			new CharacterRule(EnglishCharacterData.LowerCase, 1),

			// at least one digit character
			new CharacterRule(EnglishCharacterData.Digit, 1),

			// at least one symbol (special character)
			new CharacterRule(EnglishCharacterData.Special, 1),

			// username is not allowed as part of the password (not even backwards)
			new UsernameRule(true, true),

			// no whitespace
			new WhitespaceRule());
}
