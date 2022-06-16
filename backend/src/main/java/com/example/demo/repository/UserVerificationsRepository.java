package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.UserVerifications;

public interface UserVerificationsRepository extends JpaRepository<UserVerifications, Long>{
	
	@Query("SELECT uv FROM UserVerifications uv WHERE uv.verificationCode=?1")
	public UserVerifications getByVerificationCode(String code);
}
