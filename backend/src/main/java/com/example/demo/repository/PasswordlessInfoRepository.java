package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.PasswordlessInfo;

public interface PasswordlessInfoRepository extends JpaRepository<PasswordlessInfo, Long>{
	
	@Query("SELECT uv FROM PasswordlessInfo uv WHERE uv.verificationCode=?1")
	public PasswordlessInfo getByVerificationCode(String code);
}
