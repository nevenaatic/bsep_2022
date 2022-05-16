package com.example.demo.dto;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.demo.model.CertificateType;

public class CertificateFrontDto {

	public String serialCode;
	public String subjectFullName;
	public String issuerFullName;
	public String subjectEmail;
	public String issuerEmail;
	public Date validFrom;
	public Date validUntil;
	public boolean revoked;
	@Enumerated(value = EnumType.STRING)
	public CertificateType certificateType;
	public String certificateIntention;
	
	public CertificateFrontDto(String serialCode, String subjectFullName, String issuerFullName, String subjectEmail,
			String issuerEmail, Date validFrom, Date validUntil, boolean revoked, CertificateType certificateType,
			String certificateIntention) {
		super();
		this.serialCode = serialCode;
		this.subjectFullName = subjectFullName;
		this.issuerFullName = issuerFullName;
		this.subjectEmail = subjectEmail;
		this.issuerEmail = issuerEmail;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.revoked = revoked;
		this.certificateType = certificateType;
		this.certificateIntention = certificateIntention;
	}
	
	public CertificateFrontDto() {
		super();
	}
	
	
	
}
