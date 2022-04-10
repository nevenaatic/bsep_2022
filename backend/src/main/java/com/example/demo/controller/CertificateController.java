package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.CertificateData;
import com.example.demo.service.CertificateService;

@Controller
@CrossOrigin
@RequestMapping(value="certificate")
public class CertificateController {
	
	@Autowired
	private CertificateService certService;

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
	public ResponseEntity<List<CertificateData>> getAllCertificates()
	{
		return new ResponseEntity<List<CertificateData>>(certService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/getCertificatesById/{userId}")
	public List<String> getCertificatesById()
	{
		return null;
	}
	
	@GetMapping(path = "/revoke/{certificateSerial}")
	public ResponseEntity<List<String>> revoke(@PathVariable("certificateSerial") String serial)
	{
		certService.revokeCertificate(serial);
		List <String> lista = new ArrayList<String>();
		lista.add("BEJB");
		return new ResponseEntity<List<String>>(lista, HttpStatus.OK);
	}
}
