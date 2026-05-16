package com.example.Crackers.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Crackers.Models.UserLoginCredentials;
import com.example.Crackers.Models.UserSessions;
import com.example.Crackers.Repositories.UserSessionsRepository;

@Service
public class UserSessionsService {
	
	@Autowired
	private UserSessionsRepository userSessionsRepository;
	
	@Autowired
	private UserLoginCredentialsService userLoginCredentialsService;
	
	/**
	 * It checks whether the user ID and session token matches in the database
	 * 
	 * @param userId The user ID of the user who is trying to access the API.
	 * @param sessionToken This is the session token that is generated when the user logs in.
	 * @return A boolean value.
	 */
	public boolean isUserIdAndSessionTokenExists(int userId, String sessionToken) {
		try {
			boolean status = false;
			
			if(userSessionsRepository.existsByUserIdAndSessionToken(userId, sessionToken)) {
				UserLoginCredentials usrLoginCredential = userLoginCredentialsService.getUserLoginCredsByUserId(userId);
				if(usrLoginCredential.getUserRole().equals("USER")) {
					status = true;
				}
			}
			
			return status;
		} catch (Exception e) {
			throw new RuntimeException("Failed to check whether user ID and session token matches:\t" + e.getMessage());
		}
	}
	
	/**
	 * It checks whether a session token exists in the database
	 * 
	 * @param sessionToken The session token to be checked.
	 * @return A boolean value.
	 */
	public boolean isSessionTokenExists(String sessionToken) {
		try {
			boolean status = false;
			
			if(userSessionsRepository.existsBySessionToken(sessionToken)) {
				status = true;
			}
			return status;
		} catch (Exception e) {
			throw new RuntimeException("Failed to check whether session token exists:\t" + e.getMessage());
		}
	}
	
	/**
	 * It gets a user session by session token
	 * 
	 * @param sessionToken The session token that was generated when the user logged in.
	 * @return A UserSessions object.
	 */
	public UserSessions getBySessionToken(String sessionToken) {
		try {
			UserSessions userSessions = userSessionsRepository.findBySessionToken(sessionToken);
			
			return userSessions;
		} catch (Exception e) {
			throw new RuntimeException("Failed to get by session token from database:\t" + e.getMessage());
		}
	}

	/**
	 * It takes a userId and a timestamp, generates a random UUID, hashes it, and saves it to the database to create new session
	 * 
	 * @param userId The user's id
	 * @param timestamp The current time in milliseconds
	 * @return The hashedToken is being returned.
	 */
	public String createNewSession(int userId, String timestamp) {
		try {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String token = UUID.randomUUID().toString();
			String hashedToken = bCryptPasswordEncoder.encode(token).toString();
			UserSessions userSession = new UserSessions(
				userId,
				hashedToken,
				timestamp
			);
			
			userSessionsRepository.save(userSession);
			
			return hashedToken;
		} catch (Exception e) {
			throw new RuntimeException("Failed to add new record to database:\t" + e.getMessage());
		}
	}
	
	/**
	 * It deletes a row by user ID for logging out
	 * 
	 * @param userId The userId of the user who is logging out.
	 */
	public void logoutSession(int userId) {
		try {
			userSessionsRepository.deleteById(userId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to logout session:\t" + e.getMessage());
		}
	}

}
