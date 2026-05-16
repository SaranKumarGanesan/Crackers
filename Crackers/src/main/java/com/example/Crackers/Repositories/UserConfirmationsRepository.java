package com.example.Crackers.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Crackers.Models.UserConfirmations;

@Repository
public interface UserConfirmationsRepository extends CrudRepository<UserConfirmations, Integer> {
	
	// Query to check whether the user ID and Token exists.
	public boolean existsByUserIdAndConfirmationToken(int userId, String token);
	
	// Query to get record by user ID and Token.
	@Query(
		value = "SELECT * FROM user_confirmations WHERE user_id=?1 AND confirmation_token=?2",
		nativeQuery = true
	)
	public UserConfirmations findUserConfirmationByUserIdAndToken(int userId, String token);

}
