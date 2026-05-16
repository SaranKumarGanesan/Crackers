package com.example.Crackers.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "last_updated_on")
    private String lastUpdatedOn;

    public UserProfiles() {
        super();
    }

	public UserProfiles(Integer userId, String firstName, String middleName, String lastName, String gender,
			String dateOfBirth, String address, String phoneNo, String emailId, String createdOn,
			String lastUpdatedOn) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.createdOn = createdOn;
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public UserProfiles(String firstName, String middleName, String lastName, String gender, String dateOfBirth,
			String address, String phoneNo, String emailId, String createdOn, String lastUpdatedOn) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.createdOn = createdOn;
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
		return "UserProfiles [userId=" + userId + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", address="
				+ address + ", phoneNo=" + phoneNo + ", emailId=" + emailId + ", createdOn=" + createdOn
				+ ", lastUpdatedOn=" + lastUpdatedOn + "]";
	}


}
