package com.example.demo.model;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class AppUser implements UserDetails  {

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
//	@Enumerated(value = EnumType.STRING)
//	@Column
//	public UserType role;
	@Column
	public boolean verified;
	@Column
	public String verificationCode;
	
	@Column(name = "last_password_reset_date", nullable = true)
	private Timestamp lastPasswordResetDate;
	
	@Column(name = "must_change_password")
	private boolean must_change_password;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "role_id")
	public Role role;
	
	public AppUser() {
		super();
	}
	
	public AppUser(long id, String name, String surname, String email, String password, String address, String city,
			String country,  Role role) {
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
			String country, Role role) {
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
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Role> collection = new ArrayList<Role>();
		collection.add(this.role);
		return collection;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return verified;
	}

	public Timestamp getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public boolean isMust_change_password() {
		return must_change_password;
	}

	public void setMust_change_password(boolean must_change_password) {
		this.must_change_password = must_change_password;
	}
	
	
}
