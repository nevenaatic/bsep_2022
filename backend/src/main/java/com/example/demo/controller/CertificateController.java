package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.security.cert.Certificate;

import org.apache.log4j.Logger;
import org.bouncycastle.cert.CertIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.CertificateFrontDto;
import com.example.demo.dto.IssuerInfoDTO;
import com.example.demo.dto.NewCertificateDto;
import com.example.demo.dto.SubjectInfoDTO;
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
	final static Logger loggerErr = Logger.getLogger("errorLogger"); 
	final static Logger loggerInfo = Logger.getLogger("infoLogger");
	final static Logger loggerWarn = Logger.getLogger("warnLogger");
	
	@PostMapping(path = "/createCertificate")
	public ResponseEntity<Boolean> createCertificate(@RequestBody NewCertificateDto certificateDTO) throws CertIOException
	{
		
		
		System.out.println(certificateDTO.toString());
		AppUser issuer = appUserService.findById(certificateDTO.issuerId);
		IssuerInfoDTO issuerDTO = new IssuerInfoDTO(issuer.name, issuer.surname,"", "", "RS", issuer.email, String.valueOf(issuer.id));
		AppUser subject = appUserService.findById(certificateDTO.subjectId);
		SubjectInfoDTO subjectDTO = new SubjectInfoDTO(subject.name, subject.surname,"", "", "RS", subject.email, String.valueOf(subject.id));
		if (certGen.saveCertificate(issuerDTO, subjectDTO, certificateDTO.validFrom.toString(), certificateDTO.validUntil.toString(), certificateDTO.isCA, certificateDTO.extensions))
			{
			loggerInfo.info("CCRT | SI  " + subject.id + " | II  "+ issuer.id);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
		else {
		loggerErr.error("CNCCRT | II " + issuer.id +" | SI " + subject.id);
		return  new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@PostMapping(path = "/revokeCertificate")
	public  ResponseEntity<Boolean> revokeCertificate(@RequestBody String serialNumber)
	{
		
        AppUser loggedUser = this.loggedUser();
		loggerInfo.info("CRTR " + serialNumber.split("=")[0] + "| UI  " + loggedUser.id);
		return new ResponseEntity<Boolean>(certificateService.revokeCertificate(serialNumber.split("=")[0]), HttpStatus.OK);
	}

	@GetMapping(path = "/getAllCertificates")
	public ResponseEntity<List<CertificateFrontDto>> getAllCertificates()
	{
		
		return new ResponseEntity<List<CertificateFrontDto>>(certificateService.formatCertificateFrontData(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/getCertificatesById/{userId}")
	public ResponseEntity<List<CertificateFrontDto>> getCertificatesById(@PathVariable("userId") long userId)
	{
		return new ResponseEntity<List<CertificateFrontDto>>(certificateService.formatSubjectCertificateFrontData(userId), HttpStatus.OK);
	}
	
	@GetMapping(path = "/getIssuedCertificatesById/{userId}")
	public ResponseEntity<List<CertificateFrontDto>> getIssuedCertificatesById(@PathVariable("userId") long userId)
	{
		return new ResponseEntity<List<CertificateFrontDto>>(certificateService.formatIssuedCertificatesFrontData(userId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/checkValidity/{serialCode}")
	public ResponseEntity<Boolean> isCertificateValid(@PathVariable("serialCode") String serialCode) throws CertificateException, NoSuchAlgorithmException, Exception, GeneralSecurityException, Exception {
		loggerInfo.info("CCV  " + serialCode + " | UI  " + this.loggedUser().id);
		return new ResponseEntity<Boolean> (certificateService.isCertificateValidTEST(serialCode), HttpStatus.OK);
	}

	@PostMapping("/downloadCertificate")
    public ResponseEntity<Resource> downloadCertificate(@RequestBody String serialNumber)
            throws CertificateException, IOException {
		
		AppUser loggedUser = this.loggedUser();
		String serial = serialNumber.split("=")[0];
        certificateService.extractCertificate(serialNumber.split("=")[0]);
        File file = new File(serialNumber.split("=")[0] + ".crt");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        file.deleteOnExit();
        loggerInfo.info("DWLCRT  " + serialNumber.split("=")[0] + " | UI  " + loggedUser.id);
     
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
	
	public AppUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
        return  (AppUser)authentication.getPrincipal();
	}
}
