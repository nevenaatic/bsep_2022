package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.model.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>{
	
	@Query("SELECT au FROM AppUser au WHERE au.email=?1")
	public AppUser getUserByEmail(String email);
	
	public AppUser findByEmail(String email);
	
	public AppUser findByVerificationCode(String verificationCode);
}

