package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class PasswordlessInfo {
	@Id
	@SequenceGenerator(name = "passwordlessInfoSeqGen", sequenceName = "passwordlessInfoSeqGen", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passwordlessInfoSeqGen")
	@Column(name="id", unique=true, nullable=false)
	public long id;
	@Column
	public String email;
	@Column
	public String verificationCode;
	@Column
	public LocalDateTime timeOfRequest;
	
	public PasswordlessInfo(long id, String email, String verificationCode, LocalDateTime timeOfRequest) {
		super();
		this.id = id;
		this.email = email;
		this.verificationCode = verificationCode;
		this.timeOfRequest = timeOfRequest;
	}
	
	public PasswordlessInfo(String email, String verificationCode, LocalDateTime timeOfRequest) {
		super();
		this.email = email;
		this.verificationCode = verificationCode;
		this.timeOfRequest = timeOfRequest;
	}
	
	public PasswordlessInfo() {

	}
}
