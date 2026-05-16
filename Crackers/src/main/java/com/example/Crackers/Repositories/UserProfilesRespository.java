package com.example.Crackers.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Crackers.Models.UserProfiles;

@Repository
public interface UserProfilesRespository extends CrudRepository<UserProfiles, Integer> {

	// Query to check whether the Email exists.
	public boolean existsByEmailId(String emailId);

	// Query to get user profile by Email.
	public UserProfiles findByEmailId(String emailId);

}
