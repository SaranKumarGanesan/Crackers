package com.example.Crackers.Services;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Crackers.Models.UserLoginCredentials;
import com.example.Crackers.Models.UserProfiles;
import com.example.Crackers.Repositories.UserLoginCredentialsRepository;

@Service
public class LoginService {
	
	@Autowired
	private UserLoginCredentialsRepository userLoginCredentialsRepository;
	
	@Autowired
	private UserProfilesService userProfilesService;
	
	@Autowired
	private UserSessionsService userSessionsService;
	
	/**
	 * It validates the user login credentials and returns the user profile, user settings, and a token
	 * 
	 * @param userLogin A map of emailId and password.
	 * @return A map of the user's profile, settings, and login status.
	 */
	public Map<String, Object> validateLogin(Map<String, String> userLogin) {
		try {
			String emailId = userLogin.get("emailId").toString();
			String password = userLogin.get("password").toString();
			String timestamp = new Timestamp(System.currentTimeMillis()).toString();
			Map<String, Object> validateResp = new HashMap<String, Object>();
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			
			// Checking if the emailId exists in the database.
			if(!userLoginCredentialsRepository.existsByEmailId(emailId)) {
				validateResp.put("message", "ユーザー名またはパスワードが間違っています。");
				validateResp.put("httpStatus", HttpStatus.BAD_REQUEST.toString());
				return validateResp;
			}
			
			UserLoginCredentials usrLoginCredentials = userLoginCredentialsRepository.findByEmailId(emailId);
			// Checking if the password is correct.
			if(!bCryptPasswordEncoder.matches(password, usrLoginCredentials.getPassword())) {
				validateResp.put("message", "Incorrect username or password.。");
				validateResp.put("httpStatus", HttpStatus.BAD_REQUEST.toString());
				return validateResp;
			}
			
			// This is checking if the user has verified their account.
			if(!usrLoginCredentials.isVerified()) {
				validateResp.put("message", "Please check your account.");
				validateResp.put("httpStatus", HttpStatus.BAD_REQUEST.toString());
				return validateResp;
			}
			
			// Creating a new session for the user.
			String token = userSessionsService.createNewSession(usrLoginCredentials.getUserId(), timestamp);
			
			// Getting the user profile by the user id.
			UserProfiles usrProfiles = userProfilesService.getUserById(usrLoginCredentials.getUserId());
			
			// Mapping user profiles.			
			for(Field field : usrProfiles.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				// Skipping the fields that are not required in the response.
				if(field.getName().equals("createdOn") || field.getName().equals("lastUpdatedOn") || field.getName().equals("phoneNo") || field.getName().equals("emailId")) {
					continue;
				}
				else {
					validateResp.put(field.getName(), field.get(usrProfiles));					
				}
			}
					
			// Mapping user login status.
			validateResp.put("isActive", usrLoginCredentials.isActive());
			validateResp.put("isFirstLogin", usrLoginCredentials.isFirstLogin());
			
			validateResp.put("token", token);
			validateResp.put("httpStatus", HttpStatus.OK.toString());
			
			return validateResp;
		} catch (Exception e) {
			throw new RuntimeException("Failed to validate login credentials:\t" + e.getMessage());
		}
	}

}
