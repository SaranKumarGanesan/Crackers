package com.example.Crackers.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	
	/**
	 * The function checks if the email id is valid or not
	 * 
	 * @param emailId The email address to be validated.
	 * @return A boolean value.
	 */
	public boolean isEmailPatternValid(String emailId) {
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
		        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(emailId);
		boolean isValid = matcher.find();
		return isValid;
	}

}
