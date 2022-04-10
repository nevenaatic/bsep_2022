package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.CertificateService;

@Controller
//@CrossOrigin
@RequestMapping(value="certificate")
public class CertificateController {
	
	private CertificateService certificateService;
	
	public CertificateController(CertificateService certificateService ) {
		this.certificateService = certificateService;
	}

	@PostMapping(path = "/createCertificate")
	public boolean createCertificate()
	{
		return true;
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
	public List<String> getAllCertificates()
	{
		List <String> lista = new ArrayList<String>();
		lista.add("BEJB");
		return lista;
	}
	
	@GetMapping(path = "/getCertificatesById/{userId}")
	public List<String> getCertificatesById()
	{
		return null;
	}
	
	@PostMapping(value = "/checkValidity/{serialCode}")
	public ResponseEntity<Boolean> isCertificateValid(@PathVariable String serialCode) {
		
	
		return new ResponseEntity<> (certificateService.isCertificateValid(serialCode), HttpStatus.ACCEPTED);
		  
	}
	
	
}
