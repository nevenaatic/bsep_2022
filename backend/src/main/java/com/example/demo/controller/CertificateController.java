package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@CrossOrigin
@RequestMapping(value="certificate")
public class CertificateController {

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
}
