package com.example.demo.dto;


public class UserFrontDto {

	public long id;
	
	public String name;
	
	public String surname;
	
	public String email;
		
	public String address;
	
	public String city;
	
	public String country;

	public UserFrontDto(long id, String name, String surname, String email, String address,
			String city, String country) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.address = address;
		this.city = city;
		this.country = country;
	}

	public UserFrontDto() {
		super();
	}
	
	
}
