package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class CertificateStatus {

	@Id
	@SequenceGenerator(name = "certSeqGen", sequenceName = "certSeq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certSeqGen")
	@Column(name="id", unique=true, nullable=false)
	public long id;
	@Column
	public String code;
	@Column
	public boolean status;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public CertificateStatus(long id, String code, boolean status) {
		super();
		this.id = id;
		this.code = code;
		this.status = status;
	}
	
	public CertificateStatus(String code, boolean status) {
		super();
		this.code = code;
		this.status = status;
	}
	
	public CertificateStatus() {
	}
	
	
	
}
