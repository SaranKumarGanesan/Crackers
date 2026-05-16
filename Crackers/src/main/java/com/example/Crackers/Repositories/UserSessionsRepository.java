package com.example.Crackers.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Crackers.Models.UserSessions;

@Repository
public interface UserSessionsRepository extends CrudRepository<UserSessions, Integer> {
	
	// Query to check whether session token & user ID matches.
	public boolean existsByUserIdAndSessionToken(int userId, String sessionToken);
	
	// Query to check whether session token exists.
	public boolean existsBySessionToken(String sessionToken);
	
	// Query to get by session token.
	public UserSessions findBySessionToken(String sessionToken);

}
