package com.example.Crackers.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_login_credentials")
public class UserLoginCredentials {
	
	@Id
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "user_role")
	private String userRole;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	@Column(name = "is_verified")
	private boolean isVerified;
	
	@Column(name = "is_first_login")
	private boolean isFirstLogin;
	
	@Column(name = "created_on")
	private String createdOn;
	
	@Column(name = "last_updated_on")
	private String lastUpdatedOn;

	public UserLoginCredentials() {
		super();
	}

	public UserLoginCredentials(Integer userId, String emailId, String password, String userRole, boolean isActive,
			boolean isVerified, boolean isFirstLogin, String createdOn, String lastUpdatedOn) {
		super();
		this.userId = userId;
		this.emailId = emailId;
		this.password = password;
		this.userRole = userRole;
		this.isActive = isActive;
		this.isVerified = isVerified;
		this.isFirstLogin = isFirstLogin;
		this.createdOn = createdOn;
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	@Override
	public String toString() {
		return "UserLoginCredentials [userId=" + userId + ", emailId=" + emailId + ", password=" + password
				+ ", userRole=" + userRole + ", isActive=" + isActive + ", isVerified=" + isVerified + ", isFirstLogin="
				+ isFirstLogin + ", createdOn=" + createdOn + ", lastUpdatedOn=" + lastUpdatedOn + "]";
	}

}
