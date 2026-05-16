package com.example.Crackers.Services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Crackers.Models.UserConfirmations;
import com.example.Crackers.Repositories.UserConfirmationsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserConfirmationsService {
	
	@Autowired
	private UserConfirmationsRepository userConfirmationsRepository;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	/**
	 * Generate a random number between 111111 and 999999, convert it to a string, and return it
	 * 
	 * @return A random number between 111111 and 999999
	 */
	public String generateRandomNum() {
		String randomNum;
		int minVal = 111111;
		int maxVal = 999999;
		Integer num = (int) (Math.random() * (maxVal - minVal + 1) + minVal);
		
		randomNum = num.toString();
		
		return randomNum;
	}
	
	/**
	 * It checks whether the user have a record on the database
	 * 
	 * @param userId The user's ID
	 * @return A boolean value.
	 */
	public boolean isUserHaveRecord(int userId) {
		try {
			boolean status = false;
			if(userConfirmationsRepository.existsById(userId)) {
				status = true;
			}
			return status;
		} catch (Exception e) {
			throw new RuntimeException("Failed to check whether user have record on database:\t" + e.getMessage());
		}
	}
	
	/**
	 * It generates a random number, hashes it, saves it to the database, and sends it to the user's email
	 * 
	 * @param userId The id of the user who is registering
	 * @param email the email address of the user
	 */
	public void addConfirmationToken(int userId, String email) {
		try {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			// String token = UUID.randomUUID().toString();
			String token = generateRandomNum();
			String hashedToken = bCryptPasswordEncoder.encode(token).toString();
			UserConfirmations userConfirmation = new UserConfirmations(
					userId, 
					hashedToken, 
					false, 
					new Timestamp(System.currentTimeMillis()).toString()
			);
			
			userConfirmationsRepository.save(userConfirmation);
			emailSenderService.confirmationMailSender(userId, email, token);
		} catch (Exception e) {
			throw new RuntimeException("Failed to add new record to database:\t" + e.getMessage());
		}
	}
	
	/**
	 * It checks if the user exists, if the token is valid, if the token is not expired, and if all of the
	 * above are true, it deletes the token from the database
	 * 
	 * @param userId The user id of the user who is trying to confirm the token.
	 * @param token The token that was sent to the user's email address.
	 * @return A map of String and Object.
	 */
	public Map<String, Object> confirmUserToken(int userId, String token) {
		try {
			Map<String, Object> respMessage = new HashMap<String, Object>();
			if(!userConfirmationsRepository.existsById(userId)) {
				respMessage.put("message", "Invalid User ID.");
				respMessage.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMessage;
			}
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			UserConfirmations userConfirmation = userConfirmationsRepository.findById(userId).get();
			if(bCryptPasswordEncoder.matches(token, userConfirmation.getConfirmationToken())) {
				if(!userConfirmation.isExpired()) {
					userConfirmationsRepository.delete(userConfirmation);
				} else {
					respMessage.put("message", "Confirmation token expired.");
					respMessage.put("httpStatus", HttpStatus.BAD_REQUEST);
					return respMessage;
				}
			} else {
				respMessage.put("message", "Invalid OTP.");
				respMessage.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMessage;
			}
			
			respMessage.put("message", "Verified successfully.");
			respMessage.put("httpStatus", HttpStatus.OK);
			return respMessage;
		} catch (Exception e) {
			throw new RuntimeException("Failed to update record to database:\t" + e.getMessage());
		}
	}
	
	/**
	 * It generates a random token, hashes it, saves it in the database, and sends it to the user's email for changing the password
	 * 
	 * @param userId The user's id
	 * @param emailId The email address of the user
	 */
	public void forgotPasswordToken(int userId, String emailId) {
		try {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String token = generateRandomNum();
			String hashedToken = bCryptPasswordEncoder.encode(token).toString();
			UserConfirmations userConfirmation = new UserConfirmations(
					userId, 
					hashedToken, 
					false, 
					new Timestamp(System.currentTimeMillis()).toString()
			);
			
			userConfirmationsRepository.save(userConfirmation);
			emailSenderService.forgotPasswordMailSender(userId, emailId, token);
		} catch (Exception e) {
			throw new RuntimeException("Failed to add new token for forget password:\t" + e.getMessage());
		}
	}
	
	/**
	 * It generates a random token, hashes it, saves it to the database, and sends it to the user's email for deleting the account
	 * 
	 * @param userId The user's id
	 * @param emailId The email address of the user
	 */
	public void accountDeleteRequestToken(int userId, String emailId) {
		try {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String token = generateRandomNum();
			String hashedToken = bCryptPasswordEncoder.encode(token).toString();
			UserConfirmations userConfirmation = new UserConfirmations(
					userId, 
					hashedToken, 
					false, 
					new Timestamp(System.currentTimeMillis()).toString()
			);
			
			userConfirmationsRepository.save(userConfirmation);
			emailSenderService.deleteRequestMailSender(userId, emailId, token);
		} catch (Exception e) {
			throw new RuntimeException("Failed to add new token for delete account verification:\t" + e.getMessage());
		}
	}
	
	/**
	 * It deletes a record from the database by the user's ID
	 * 
	 * @param userId The user ID of the user who's confirmation record you want to delete.
	 */
	public void deleteConfirmationRecordByUserId(int userId) {
		try {
			userConfirmationsRepository.deleteById(userId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete data by User ID:\t" + e.getMessage());
		}
	}

}

