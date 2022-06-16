package com.example.demo.dto;

public class SubjectInfoDTO {

	public String name;
	public String surname;
	public String organization;
	public String organizationalUnit;
	public String countryCode;
	public String email; 
	public String userId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOrganizationalUnit() {
		return organizationalUnit;
	}
	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public SubjectInfoDTO(String name, String surname, String organization, String organizationalUnit,
			String countryCode, String email, String userId) {
		super();
		this.name = name;
		this.surname = surname;
		this.organization = organization;
		this.organizationalUnit = organizationalUnit;
		this.countryCode = countryCode;
		this.email = email;
		this.userId = userId;
	}
	public SubjectInfoDTO() {
		super();
	}
}
