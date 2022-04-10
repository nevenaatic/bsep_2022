package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CertificateData;
import com.example.demo.model.CertificateType;
import com.example.demo.repository.CertificateRepository;

@Service
public class CertificateService {

	@Autowired
	private CertificateRepository certificateRepository;
	
	public List<CertificateData> findAll() {
		return certificateRepository.findAll();
	}
	
    public void revokeCertificate(String serialNumber) {
    	CertificateData certificate = certificateRepository.getCertificateByCode(serialNumber);
    	List <CertificateData> allCerts = certificateRepository.findAll();
		for (CertificateData c : allCerts) {
			if (c.id == certificate.id) {
				c.revoked = true;
				break;
			}
		}
		certificateRepository.saveAll(allCerts);
    	if (certificate.certificateType == CertificateType.CA) {
    		List<String> keyIdValues = new ArrayList<String>();
    		String certKeyId = certificate.subjectKeyId;
    		for (CertificateData c : allCerts) {
    			if (c.issuerKeyId.equalsIgnoreCase(certKeyId)) {
    				c.revoked = true;
    				if (c.certificateType == CertificateType.CA) {
    					if (!keyIdValues.contains(c.subjectKeyId)) {
    						keyIdValues.add(c.subjectKeyId);
    					}
    				}
    			}
    		}
    		int size = 0;
    		while (size != keyIdValues.size()) {
    			size = keyIdValues.size();
	    		for (CertificateData c : allCerts) {
	    			if (keyIdValues.contains(c.issuerKeyId)) {
	    				c.revoked = true;
	    				if (c.certificateType == CertificateType.CA) {
	    					keyIdValues.add(c.subjectKeyId);
	    				}
	    			}
	    		}
    		}
    	certificateRepository.saveAll(allCerts);
    	}
    }
}
