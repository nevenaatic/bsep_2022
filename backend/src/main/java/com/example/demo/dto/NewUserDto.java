package com.example.demo.dto;


public class NewUserDto {

	public String name;
	public String surname;
	public String email;
	public String password;
	public String address;
	public String city;
	public String country;
	public boolean verified;
	public String verificationCode;
	
	public NewUserDto(String name, String surname, String email, String password, String address, String city,
			String country, boolean verified, String verificationCode) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.address = address;
		this.city = city;
		this.country = country;
		this.verified = verified;
		this.verificationCode = verificationCode;
	}
	
	public NewUserDto() {
		super();
	}
	
	
}
