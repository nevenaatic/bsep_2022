package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class CertificateData {

	@Id
	@SequenceGenerator(name = "certificateSeqGen", sequenceName = "certificateSeq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificateSeqGen")
	@Column(name="id", unique=true, nullable=false)
	public long id;
	@Column
	public String serialCode;
	@Column
	public long subjectUserId;
	@Column
	public long issuerUserId;
	@Column
	public String subjectKeyId;
	@Column
	public String issuerKeyId;
	@Column
	public Date validFrom;
	@Column
	public Date validUntil;
	@Column
	public boolean revoked;
	@Enumerated(value = EnumType.STRING)
	@Column
	public CertificateType type;

	public CertificateData(long id, String serialCode, long subjectUserId, long issuerUSerId, String subjectKeyId,
			String issuerKeyId, Date validFrom, Date validUntil, boolean revoked,
			CertificateType type) {
		super();
		this.id = id;
		this.serialCode = serialCode;
		this.subjectUserId = subjectUserId;
		this.issuerUserId = issuerUSerId;
		this.subjectKeyId = subjectKeyId;
		this.issuerKeyId = issuerKeyId;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.revoked = revoked;
		this.type = type;
	}



	public CertificateData() {
		super();
	}
	
	
	
	
}
