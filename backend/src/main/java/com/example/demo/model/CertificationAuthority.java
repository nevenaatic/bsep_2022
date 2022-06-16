package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class CertificationAuthority {

	@Id
	@SequenceGenerator(name = "caSeqGen", sequenceName = "caSeq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caSeqGen")
	@Column(name="id", unique=true, nullable=false)
	public long id;
	@Column
	public String name;
	@Column
	public String surname;
	@Column(nullable = false, unique = true)
	public String email;
	@Column
	public String password;
	@Column
	public String address;
	@Column
	public String city;
	@Column
	public String country;
	
	public CertificationAuthority() {
		super();
	}
	
	public CertificationAuthority(long id, String name, String surname, String email, String password, String address, String city,
			String country) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.address = address;
		this.city = city;
		this.country = country;
	}
	
	public CertificationAuthority(String name, String surname, String email, String password, String address, String city,
			String country) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.address = address;
		this.city = city;
		this.country = country;
	}
}
