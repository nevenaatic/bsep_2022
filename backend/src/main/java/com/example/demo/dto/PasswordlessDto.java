package com.example.demo.dto;

public class PasswordlessDto {

	public String email;
	public String userCode;
	
	public PasswordlessDto(String email, String userCode) {
		super();
		this.email = email;
		this.userCode = userCode;
	}

	public PasswordlessDto() {
		super();
	}
	
	
	
}
