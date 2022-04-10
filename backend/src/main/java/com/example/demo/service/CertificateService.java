package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.CertificateData;
import com.example.demo.model.CertificateType;
import com.example.demo.repository.CertificateRepository;

@Service
public class CertificateService {
	
	private CertificateRepository certificateRepository;
	
	public void revokeByIssuerKeyId(String keyId) {
		List <CertificateData> allCerts = certificateRepository.findAll();
		for (CertificateData c : allCerts) 
			if (c.issuerKeyId == keyId) 
				c.revoked = true;
		certificateRepository.saveAll(allCerts);
	}

    public void revokeCertificate(String serialNumber) {
    	CertificateData certificate = certificateRepository.getCertificateByCode(serialNumber);
    	List <CertificateData> allCerts = certificateRepository.findAll();
    	if (certificate.type == CertificateType.end_entity) {
    		for (CertificateData c : allCerts) {
    			if (c.id == certificate.id) {
    				c.revoked = true;
    				break;
    			}
    		}
    		certificateRepository.saveAll(allCerts);
    	}
    	else {
    		List<String> keyIdValues = new ArrayList<String>();
    		
    		int amount = 0;
    		boolean done = false;
    		String certKeyId = certificate.subjectKeyId;
    		while (done != true) {
    		for (CertificateData c : certificateRepository.findAll()) {
    			if (c.issuerKeyId == certKeyId) {
    				c.revoked = true;
    				if (c.type == CertificateType.CA) {
    					if (!keyIdValues.contains(c.subjectKeyId)) {
    						keyIdValues.add(c.subjectKeyId);
    						amount++;
    					}
    				}
    			}
    			if (amount == 0) {
    				certificateRepository.saveAll(allCerts);
    				return;
    			}
    		}
    		boolean again = true;
    		for (CertificateData c : certificateRepository.findAll()) {
    			if (keyIdValues.contains(c.issuerKeyId)) {
    				c.revoked = true;
    				if (c.type == CertificateType.CA) {
    					keyIdValues.add(c.subjectKeyId);
    				}
    			}
    		}
    		}
    		
    	}
    	certificateRepository.saveAll(allCerts);
    }
}
