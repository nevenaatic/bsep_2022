package com.example.demo.model;

import java.security.PublicKey;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bouncycastle.asn1.x500.X500Name;

public class SubjectData {

	//@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	private PublicKey publicKey;
	private X500Name x500name;
	private String serialNumber;
	private Date startDate;
	private Date endDate;

	public SubjectData() {

	}

	public SubjectData(PublicKey publicKey, X500Name x500name, String serialNumber, Date startDate, Date endDate) {
		this.publicKey = publicKey;
		this.x500name = x500name;
		this.serialNumber = serialNumber;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public X500Name getX500name() {
		return x500name;
	}

	public void setX500name(X500Name x500name) {
		this.x500name = x500name;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
