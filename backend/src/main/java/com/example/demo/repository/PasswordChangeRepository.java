package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.PasswordChange;
import com.example.demo.model.PasswordlessInfo;

public interface PasswordChangeRepository extends JpaRepository<PasswordChange, Long>{
	
	@Query("SELECT uv FROM PasswordChange uv WHERE uv.verificationCode=?1")
	public PasswordChange getByVerificationCode(String code);
}
