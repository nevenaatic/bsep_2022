package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class NewCertificateDto {

	public long issuerId;
	public long subjectId;
	public LocalDate validFrom;
	public LocalDate validUntil;
	List<Integer> purposes;
	List<Integer> extensions;
	
	public NewCertificateDto(long issuerId, long subjectId, LocalDate validFrom, LocalDate validUntil,
			List<Integer> purposes, List<Integer> extensions) {
		super();
		this.issuerId = issuerId;
		this.subjectId = subjectId;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.purposes = purposes;
		this.extensions = extensions;
	}

	public NewCertificateDto() {
		super();
	}

	@Override
	public String toString() {
		return "NewCertificateDto [issuerId=" + issuerId + ", subjectId=" + subjectId + ", validFrom=" + validFrom
				+ ", validUntil=" + validUntil + ", purposes=" + purposes + ", extensions=" + extensions + "]";
	}
	
	
}
