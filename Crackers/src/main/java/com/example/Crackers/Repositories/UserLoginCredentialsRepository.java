package com.example.Crackers.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Crackers.Models.UserLoginCredentials;

@Repository
public interface UserLoginCredentialsRepository extends CrudRepository<UserLoginCredentials, Integer> {
	
	// Query to check whether the email exists.
	public boolean existsByEmailId(String emailId);
	
	// Query to get user login credentials by email ID.
	public UserLoginCredentials findByEmailId(String emailId);
	
	// Query to count of active users.
	@Query(
		value = "SELECT COUNT(*) FROM user_login_credentials WHERE is_active = true",
		nativeQuery = true
	)
	public Integer countOfActiveUsers();

}
