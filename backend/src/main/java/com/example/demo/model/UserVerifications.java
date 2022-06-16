package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class UserVerifications {
	@Id
	@SequenceGenerator(name = "userVerificationSeqGen", sequenceName = "userVerificationSeqGen", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userVerificationSeqGen")
	@Column(name="id", unique=true, nullable=false)
	public long id;
	@Column
	public String email;
	@Column
	public String verificationCode;
	@Column
	public LocalDateTime timeOfRequest;
	
	public UserVerifications(long id, String email, String verificationCode, LocalDateTime timeOfRequest) {
		super();
		this.id = id;
		this.email = email;
		this.verificationCode = verificationCode;
		this.timeOfRequest = timeOfRequest;
	}
	
	public UserVerifications(String email, String verificationCode, LocalDateTime timeOfRequest) {
		super();
		this.email = email;
		this.verificationCode = verificationCode;
		this.timeOfRequest = timeOfRequest;
	}
	
	public UserVerifications() {

	}
	
}
