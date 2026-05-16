package com.example.Crackers.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_confirmations")
public class UserConfirmations {
	
	@Id
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "confirmation_token")
	private String confirmationToken;
	
	@Column(name = "is_expired")
	private boolean isExpired;
	
	@Column(name = "created_on")
	private String createdOn;

	public UserConfirmations() {
		super();
	}

	public UserConfirmations(Integer userId, String confirmationToken, boolean isExpired, String createdOn) {
		super();
		this.userId = userId;
		this.confirmationToken = confirmationToken;
		this.isExpired = isExpired;
		this.createdOn = createdOn;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "UserConfirmations [userId=" + userId + ", confirmationToken=" + confirmationToken + ", isExpired="
				+ isExpired + ", createdOn=" + createdOn + "]";
	}
	

}
