package com.example.demo.dto;

public class ChangePasswordDto {

	public String code;
	public String password;
	
	public ChangePasswordDto(String code, String password) {
		super();
		this.code = code;
		this.password = password;
	}

	public ChangePasswordDto() {
		super();
	}
	
	
}
