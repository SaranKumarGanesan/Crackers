package com.example.Crackers.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_sessions")
public class UserSessions {
	
	@Id
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "session_token")
	private String sessionToken;
	
	@Column(name = "created_on")
	private String createdOn;

	public UserSessions() {
		super();
	}

	public UserSessions(Integer userId, String sessionToken, String createdOn) {
		super();
		this.userId = userId;
		this.sessionToken = sessionToken;
		this.createdOn = createdOn;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "UserSessions [userId=" + userId + ", sessionToken=" + sessionToken + ", createdOn=" + createdOn + "]";
	}

}
