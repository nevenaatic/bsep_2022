package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class AppUser {

	@Id
	@SequenceGenerator(name = "myUserSeqGen", sequenceName = "myUserSeq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "myUserSeqGen")
	@Column(name="id", unique=true, nullable=false)
	public long id;
	@Column
	public String name;
	@Column
	public String surname;
	@Column
	public String email;
	@Column
	public String password;
	@Column
	public String address;
	@Column
	public String city;
	@Column
	public String country;
	@Enumerated(value = EnumType.STRING)
	@Column
	public UserType role;
	@Column
	public boolean verified;
	@Column
	public String verificationCode;
	
	public AppUser() {
		super();
	}
	
	public AppUser(long id, String name, String surname, String email, String password, String address, String city,
			String country, UserType role) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.address = address;
		this.city = city;
		this.country = country;
		this.role = role;
	}
	
	public AppUser(String name, String surname, String email, String password, String address, String city,
			String country, UserType role) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.address = address;
		this.city = city;
		this.country = country;
		this.role = role;
	}
	
}
