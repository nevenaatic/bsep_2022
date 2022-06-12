package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.security.cert.Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.CertificateFrontDto;
import com.example.demo.model.AppUser;
import com.example.demo.model.CertificateData;
import com.example.demo.service.AppUserService;
import com.example.demo.service.CertificateExample;
import com.example.demo.service.CertificateService;

@Controller
@CrossOrigin(origins="*", allowedHeaders="*")
@RequestMapping(value="certificate")
public class CertificateController {
	
	//@Autowired
	private CertificateService certificateService;
	private AppUserService appUserService;
	@Autowired
	private CertificateExample certGen;
	
	public CertificateController(CertificateService certificateService, AppUserService appUserService) {
		this.certificateService = certificateService;
		this.appUserService = appUserService;
	}

	@PostMapping(path = "/createCertificate")
	public ResponseEntity<Boolean> createCertificate(@RequestBody String ccDTO)
	{
		//certGen.saveCertificate(issuer, subject, dateFrom, dateUntil, isCA);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@PostMapping(path = "/revokeCertificate/{certificateId}")
	public boolean revokeCertificate()
	{
		return true;
	}
	
	@PostMapping(path = "/checkCertificateValidity/{userId}")
	public boolean checkCertificateValidity()
	{
		return true;
	}
	
	@GetMapping(path = "/getAllCertificates")
	public ResponseEntity<List<CertificateFrontDto>> getAllCertificates()
	{
		return new ResponseEntity<List<CertificateFrontDto>>(formatCertificateFrontData(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/getCertificatesById/{userId}")
	public ResponseEntity<List<CertificateFrontDto>> getCertificatesById(@PathVariable("userId") long userId)
	{
		return new ResponseEntity<List<CertificateFrontDto>>(formatSubjectCertificateFrontData(userId), HttpStatus.OK);
	}
	
	@GetMapping(path = "/getIssuedCertificatesById/{userId}")
	public ResponseEntity<List<CertificateFrontDto>> getIssuedCertificatesById(@PathVariable("userId") long userId)
	{
		return new ResponseEntity<List<CertificateFrontDto>>(formatIssuedCertificatesFrontData(userId), HttpStatus.OK);
	}
	
	@GetMapping(path = "/revoke/{certificateSerial}")
	public ResponseEntity<List<String>> revoke(@PathVariable("certificateSerial") String serial)
	{
		certificateService.revokeCertificate(serial);
		List <String> lista = new ArrayList<String>();
		lista.add("BEJB");
		return new ResponseEntity<List<String>>(lista, HttpStatus.OK);
	}
	
	@PostMapping(value = "/checkValidity/{serialCode}")
	public ResponseEntity<Boolean> isCertificateValid(@PathVariable String serialCode) {
		
		return new ResponseEntity<> (certificateService.isCertificateValid(serialCode), HttpStatus.ACCEPTED);
	}
	
	private List <CertificateFrontDto> formatCertificateFrontData() {
		List <CertificateFrontDto> frontData = new ArrayList<CertificateFrontDto>();
		for (CertificateData cd : certificateService.findAll()) {
			AppUser subject = appUserService.findById(cd.subjectUserId);
			AppUser issuer = appUserService.findById(cd.issuerUserId);
			frontData.add(new CertificateFrontDto(cd.serialCode, 
												  subject.name + " " + subject.surname, 
												  issuer.name + " " + issuer.surname, 
												  subject.email, 
												  issuer.email,
												  cd.validFrom, 
												  cd.validUntil, 
												  cd.revoked, 
												  cd.certificateType, 
												  "Identification"));
		}
		return frontData;
	}
	
	private List <CertificateFrontDto> formatSubjectCertificateFrontData(long id) {
		List <CertificateFrontDto> frontData = new ArrayList<CertificateFrontDto>();
		AppUser subject = appUserService.findById(id);
		for (CertificateData cd : certificateService.findAll()) {
			if (cd.subjectUserId == id) {
			AppUser issuer = appUserService.findById(cd.issuerUserId);
			frontData.add(new CertificateFrontDto(cd.serialCode, 
												  subject.name + " " + subject.surname, 
												  issuer.name + " " + issuer.surname, 
												  subject.email, 
												  issuer.email,
												  cd.validFrom, 
												  cd.validUntil, 
												  cd.revoked, 
												  cd.certificateType, 
												  "Identification"));
			}
		}
		return frontData;
	}
	
	private List <CertificateFrontDto> formatIssuedCertificatesFrontData(long id) {
		List <CertificateFrontDto> frontData = new ArrayList<CertificateFrontDto>();
		AppUser issuer = appUserService.findById(id);
		for (CertificateData cd : certificateService.findAll()) {
			if (cd.issuerUserId == id) {
			AppUser subject = appUserService.findById(cd.subjectUserId);
			frontData.add(new CertificateFrontDto(cd.serialCode, 
												  subject.name + " " + subject.surname, 
												  issuer.name + " " + issuer.surname, 
												  subject.email, 
												  issuer.email,
												  cd.validFrom, 
												  cd.validUntil, 
												  cd.revoked, 
												  cd.certificateType, 
												  "Identification"));
			}
		}
		return frontData;
	}
	
	
}
