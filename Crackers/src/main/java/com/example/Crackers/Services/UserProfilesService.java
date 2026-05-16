package com.example.Crackers.Services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.Crackers.Models.UserLoginCredentials;
import com.example.Crackers.Models.UserProfiles;
import com.example.Crackers.Repositories.UserProfilesRespository;
import com.example.Crackers.helper.EmailValidator;

@Service
public class UserProfilesService {
	
	@Autowired
	private UserProfilesRespository userProfilesRespository;
	
	@Autowired
	private UserLoginCredentialsService userLoginCredentialsService;
	
	
	
	public List<UserProfiles> getAllUsers(){
		return (List<UserProfiles>) userProfilesRespository.findAll();
		
	}
	
	public boolean isEmailIdExists(String emailId) {
		try {
			boolean status = false;
			if(userProfilesRespository.existsByEmailId(emailId)) {
				status = true;
			}
			return status;
		} catch (Exception e) {
			throw new RuntimeException("failed to check user ID on database:\t" + e.getMessage());
		}
	}
	
	public UserProfiles getUserById(int userId) {
		try {
			UserProfiles userProfile = userProfilesRespository.findById(userId).get();
			String gender = userProfile.getGender();
			if(gender.equals("Male") || gender.equals("Female") || gender.equals("Other")) {
				if(gender.equals("Male")) {
					gender = "Male";
				}
				if(gender.equals("Female")) {
					gender = "Female";
				}
				if(gender.equals("Other")) {
					gender = "Other";
				}
				userProfile.setGender(gender);
				userProfilesRespository.save(userProfile);
			}
			return userProfile;
		} catch (Exception e) {
			throw new RuntimeException("Failed to get user profile by ID from database:\t" + e.getMessage());
		}
	}
	
	public Map<String, Object> addNewUser(Map<String, Object> newUserData) {
		try {
			Map<String, Object> respMap = new HashMap<String, Object>();
			EmailValidator validator = new EmailValidator();
			
			// Checks whether the email ID already exists.
			if(userProfilesRespository.existsByEmailId(newUserData.get("emailId").toString().toLowerCase())) {
				respMap.put("message", "Email ID already exists.");
				respMap.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMap;
			}
			// Checks whether the name field is not empty.
			if(newUserData.get("firstName").toString().isEmpty() || newUserData.get("firstName").toString().isBlank() || (newUserData.get("firstName").toString() == null)) {
				respMap.put("message", "Please enter your firstName.");
				respMap.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMap;
			}
	
			
			// Checks whether the gender field is not empty.
			if(newUserData.get("gender").toString().isEmpty() || newUserData.get("gender").toString().isBlank() || (newUserData.get("gender").toString() == null)) {
				respMap.put("message", "Please select your gender.");
				respMap.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMap;
			}			
			
			// Mapping new user data to UserProfile Object.
			String timestamp = new Timestamp(System.currentTimeMillis()).toString();
			UserProfiles newUser = new UserProfiles();
			newUser.setFirstName(newUserData.get("firstName").toString());
			newUser.setMiddleName(null);
			newUser.setLastName(null);
			
			
			String gender = newUserData.get("gender").toString();
			if(gender.equals("Male")) {
				gender = "Male";
			}
			if(gender.equals("Female")) {
				gender = "Female";
			}
			if(gender.equals("Other")) {
				gender = "Other";
			}
			newUser.setGender(gender);
			
			String dob = newUserData.get("dateOfBirth").toString();
			newUser.setDateOfBirth(dob.isEmpty() ? null : dob);
			newUser.setAddress(newUserData.get("address").toString());

			newUser.setPhoneNo(newUserData.get("phoneNo").toString());
			// Validating Email Address.
			String emailId = newUserData.get("emailId").toString().toLowerCase();
			if(!validator.isEmailPatternValid(emailId)) {
				respMap.put("message", "Invalid email address.");
				respMap.put("httpStatus", HttpStatus.BAD_REQUEST);
				return respMap;
			}
			newUser.setEmailId(emailId);
			newUser.setCreatedOn(timestamp);
			newUser.setLastUpdatedOn(null);
			
			// Calling INSERT query method and passing mapped user profile object to the method.
			userProfilesRespository.save(newUser);

			System.out.println("=========================================================1.1");
			
			// Checking if the new user data is added to user profile table in database or not.
			if(userProfilesRespository.existsByEmailId(newUser.getEmailId())) {
				int userId = userProfilesRespository.findByEmailId(newUser.getEmailId()).getUserId();

				System.out.println("=========================================================1.2"+userId);
				
				// Mapping new User Login data to UserLoginCredentials Object.
				UserLoginCredentials newUserLogin = new UserLoginCredentials(
						userId, 
						newUser.getEmailId(), 
						newUserData.get("password").toString(), 
						"USER", 
						false, 
						false, 
						true, 
						timestamp,
						null
				);

				System.out.println("=========================================================2"+newUserLogin);
				
				boolean isLoginAddedd = userLoginCredentialsService.addNewUserLogin(newUserLogin);
				
				// Checking if the new user login data is added to user login credentials table in database or not.

				respMap.put("userId", userId);
			} else {
				respMap.put("message", "Couldn't add new user.");
				respMap.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);
				return respMap;
			}

			System.out.println("=========================================================2");
			
			respMap.put("message", "New account created successfully.");
			respMap.put("httpStatus", HttpStatus.OK);
			return respMap;
		} catch (Exception e) {
			throw new RuntimeException("Failed to add new user to database:\t" + e.getMessage());
		}
	}
	
	

}
