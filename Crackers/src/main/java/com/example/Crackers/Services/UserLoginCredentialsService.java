package com.example.Crackers.Services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.Crackers.Models.UserLoginCredentials;
import com.example.Crackers.Repositories.UserLoginCredentialsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserLoginCredentialsService {
	
	@Autowired
	private UserLoginCredentialsRepository userLoginCredentialsRepository;
	
	@Autowired
	private UserConfirmationsService userConfirmationsService;
	
	/**
	 * It checks if a user has a record in the database
	 * 
	 * @param userId The userId of the user you want to check if they have a record.
	 * @return A boolean value.
	 */
	public boolean isUserHaveRecord(int userId) {
		try {
			boolean status = false;
			if(userLoginCredentialsRepository.existsById(userId)) {
				status = true;
			}
			return status;
		} catch (Exception e) {
			throw new RuntimeException("Failed to check if user has record:\t" + e.getMessage());
		}
	}
	
	/**
	 * It takes a UserLoginCredentials object as an argument, encrypts the password, and then saves the
	 * object to the database
	 * 
	 * @param userLoginCredential This is the object of UserLoginCredentials class.
	 * @return A boolean value.
	 */
	public boolean addNewUserLogin(UserLoginCredentials userLoginCredential) {
		try {
			boolean status = false;
			
			// Encrypting the user login credential.
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = bCryptPasswordEncoder.encode(userLoginCredential.getPassword()).toString();
			userLoginCredential.setPassword(hashedPassword);
			
			// Calling INSERT query method and passing UserLoginCredentials object.
			userLoginCredentialsRepository.save(userLoginCredential);
			if(userLoginCredentialsRepository.existsByEmailId(userLoginCredential.getEmailId())) {
				status = true;
			}
			
			return status;
		} catch (Exception e) {
			throw new RuntimeException("Failed while adding new login credentials to database:\t" + e.getMessage());
		}
	}
	
	/**
	 * It checks whether user exists or not, if exists then it checks whether user have record in
	 * confirmation table or not, if yes then it calls confirmUserToken() function of
	 * userConfirmationsService class and returns the response
	 * 
	 * @param userId The user's ID.
	 * @param token The token that was sent to the user's email address.
	 * @return A map of String and Object.
	 */
	public Map<String, Object> confirmUserToken(int userId, String token) {
		try {
			Map<String, Object> respMessage = new HashMap<String, Object>();
			
			// Checking whether user exists.
			if(!userLoginCredentialsRepository.existsById(userId)) {
				respMessage.put("message", "Invalid user ID");
				respMessage.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMessage;
			}
			// Checking whether user have record in confirmation table or not.
			if(!userConfirmationsService.isUserHaveRecord(userId)) {
				respMessage.put("message", "User don't have record.");
				respMessage.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMessage;
			}
			
			respMessage = userConfirmationsService.confirmUserToken(userId, token); 
			userConfirmationsService.confirmUserToken(userId, token); 
			UserLoginCredentials userLoginCredential = userLoginCredentialsRepository.findById(userId).get();
			if(respMessage.get("httpStatus") == HttpStatus.OK && userLoginCredential.isFirstLogin()) {
				UserLoginCredentials usrLogin = userLoginCredentialsRepository.findById(userId).get();
				usrLogin.setActive(true);
				usrLogin.setVerified(true);
				usrLogin.setLastUpdatedOn(new Timestamp(System.currentTimeMillis()).toString());
				userLoginCredentialsRepository.save(usrLogin);
			}
			
			return respMessage;
		} catch (Exception e) {
			throw new RuntimeException("Failed while updating staus of login credentials to database:\t" + e.getMessage());
		}
	}
	
	/**
	 * This method gets the user login credentials by user ID from the database
	 * 
	 * @param userId The user ID of the user whose login credentials are to be retrieved.
	 * @return The userLoginCredentialsRepository.findById(userId).get() is returning a
	 * UserLoginCredentials object.
	 */
	public UserLoginCredentials getUserLoginCredsByUserId(int userId) {
		try {
			UserLoginCredentials userLoginCredential = userLoginCredentialsRepository.findById(userId).get();
			return userLoginCredential;
		} catch (Exception e) {
			throw new RuntimeException("Failed to get login credentials by user ID from database:\t" + e.getMessage());
		}
	}
	
	/**
	 * It checks if the emailId exists in the database, if it does, it sends an OTP to the emailId and
	 * returns a response
	 * 
	 * @param emailId The email address of the user who forgot their password.
	 * @return A map of String, Object.
	 */
	public Map<String, Object> userForgotLoginCreds(String emailId) {
		try {
			Map<String, Object> respMap = new HashMap<String, Object>();
			
			// Checking if the emailId exists in the database.
			if(!userLoginCredentialsRepository.existsByEmailId(emailId)) {
				respMap.put("message", "Invalid email address");
				respMap.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMap;
			}
			
			// Getting the user login credentials by email ID from the database.
			UserLoginCredentials usrLoginCredential = userLoginCredentialsRepository.findByEmailId(emailId);
			userConfirmationsService.forgotPasswordToken(usrLoginCredential.getUserId(), emailId);
			
			respMap.put("message", "The OTP has been sent to your email. Please check it.");
			respMap.put("httpStatus", HttpStatus.OK);
			respMap.put("userId", usrLoginCredential.getUserId());
			return respMap;
		} catch (Exception e) {
			throw new RuntimeException("Failed to send password change mail:\t" + e.getMessage());
		}
	}
	
	/**
	 * It takes a userId and a map of new data, and then it updates the password of the user with the
	 * given userId with the new password in the new data map
	 * 
	 * @param userId The userId of the user whose password is to be changed.
	 * @param newData This is the new password that the user has entered.
	 * @return A map of String and Object.
	 */
	public Map<String, Object> userChangeForgotPassword(int userId, Map<String, Object> newData) {
		try {
			String newPassword = newData.get("password").toString();
			String lastUpdatedOn = new Timestamp(System.currentTimeMillis()).toString();
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			Map<String, Object> respMessage = new HashMap<String, Object>();
			
			UserLoginCredentials usrLoginCredentials = userLoginCredentialsRepository.findById(userId).get();
			String hashedPassword = bCryptPasswordEncoder.encode(newPassword).toString();
			usrLoginCredentials.setPassword(hashedPassword);
			usrLoginCredentials.setLastUpdatedOn(lastUpdatedOn);
			userLoginCredentialsRepository.save(usrLoginCredentials);
			respMessage.put("message", "Password Changed successfully.");
			respMessage.put("httpStatus", HttpStatus.OK);
			
			return respMessage;
		} catch (Exception e) {
			throw new RuntimeException("Failed to change password:\t" + e.getMessage());
		}
	}
	
	/**
	 * This method is used to update the first time login status of a user
	 * 
	 * @param userId The userId of the user whose first time login status needs to be updated.
	 * @return A map of String and Object.
	 */
	public Map<String, Object> updateFirstLoginStatus(int userId) {
		try {
			Map<String, Object> respMessage = new HashMap<String, Object>();
			UserLoginCredentials userLoginCredential = userLoginCredentialsRepository.findById(userId).get();
			if(userLoginCredential.isFirstLogin() == false) {
				respMessage.put("message", "First time login status already updated.");
				respMessage.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMessage;
			}
			userLoginCredential.setFirstLogin(false);
			userLoginCredential.setLastUpdatedOn(new Timestamp(System.currentTimeMillis()).toString());
			userLoginCredentialsRepository.save(userLoginCredential);
			
			respMessage.put("message", "First time login status updated.");
			respMessage.put("httpStatus", HttpStatus.OK);
			return respMessage;
		} catch (Exception e) {
			throw new RuntimeException("Failed to update firt time login status:\t" + e.getMessage());
		}
	}
	
	/**
	 * It updates the user's login email
	 * 
	 * @param userLoginCredential This is the object that contains the new email address.
	 */
	public void updateUserLoginEmail(UserLoginCredentials userLoginCredential) {
		try {
			userLoginCredentialsRepository.save(userLoginCredential);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update login email:\t" + e.getMessage());
		}
	}
	
	/**
	 * It deletes a user's login credentials from the database
	 * 
	 * @param userId The id of the user whose login credentials are to be deleted.
	 */
	public void deleteUserLoginCreds(int userId) {
		try {
			userLoginCredentialsRepository.deleteById(userId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete user login credentials:\t" + e.getMessage());
		}
	}

}
