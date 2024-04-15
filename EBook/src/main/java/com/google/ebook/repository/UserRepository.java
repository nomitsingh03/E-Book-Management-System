package com.google.ebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.google.ebook.entity.User;



public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByEmail(String email);
	
	User findByVerificationCode(String code);
	
	@Query("update User u set u.failedAttempt=?1 where email=?2 ")
	@Modifying
	void updateFailedAttempt(int attempt, String email);
	
	@Query("SELECT u FROM User u WHERE u.email=:email")
	User getUserByUserName(@Param("email")String email);

}
