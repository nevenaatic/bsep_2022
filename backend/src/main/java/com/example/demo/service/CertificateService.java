package com.example.demo.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.CertificateData;
import com.example.demo.model.CertificateType;
import com.example.demo.repository.CertificateRepository;

@Service
public class CertificateService {
	
	private CertificateRepository certificateRepository;
	private KeyStoreReader keyStoreReader;
	
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
    
    public Boolean isCertificateValid(String serialCode) {
    	CertificateData certificateFromDatabase = certificateRepository.getCertificateByCode(serialCode);
    	Certificate issurerCertificateFromKeyStore = null;
    	CertificateData issurerCertificateDatabase = null;
    	Boolean ret = null;
    	
    	if (certificateFromDatabase.type == CertificateType.root) {
    		return this.checkRoot(certificateFromDatabase);
    	}
    	
    	if (certificateFromDatabase.type == CertificateType.CA) {
    		ret = this.checkCA(certificateFromDatabase);
    		issurerCertificateDatabase = this.certificateRepository.findBySubjectKeyId(certificateFromDatabase.issuerKeyId);
    		issurerCertificateFromKeyStore = this.keyStoreReader.readCertificate("CAKeyStore.jks", "cakeystore", issurerCertificateDatabase.serialCode);	
    		if (issurerCertificateDatabase.type != CertificateType.root) {
    			ret = checkCA(issurerCertificateDatabase);
    			if (ret) {
    				this.isCertificateValid(issurerCertificateDatabase.serialCode);
    			}
    		} else {
    			ret = this.checkRoot(issurerCertificateDatabase);
    		}
    	} 
    	
    	/*if (certificateFromDatabase.type == CertificateType.end_entity) {
    		ret = checkEndEntity(certificateFromDatabase);
    	} */  	
    	
    	return ret;
    }

	private Boolean checkCA(CertificateData certificateFromDatabase) {
		if (certificateFromDatabase.revoked) {
    		return false;
    	}
    	
    	if (!certificateFromDatabase.validFrom.before(new Date()) || !certificateFromDatabase.validUntil.after(new Date())) {
    		return false;
    	}
    	
    	Certificate certificateFromKeyStore = null;
    	Certificate issurerCertificateFromKeyStore = null;
    	CertificateData issurerCertificateDatabase = null;
    	
    	if (certificateFromDatabase.type == CertificateType.CA) {
    		certificateFromKeyStore = this.keyStoreReader.readCertificate("CAKeyStore.jks", "cakeystore", certificateFromDatabase.serialCode);
    		issurerCertificateDatabase = this.certificateRepository.findBySubjectKeyId(certificateFromDatabase.issuerKeyId);
    		issurerCertificateFromKeyStore = this.keyStoreReader.readCertificate("CAKeyStore.jks", "cakeystore", issurerCertificateDatabase.serialCode);	
    	} 
    	
    	try {
			certificateFromKeyStore.verify(issurerCertificateFromKeyStore.getPublicKey());
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			e.printStackTrace();
		}
    	
    	return true;
	}
	
	private Boolean checkEndEntity(CertificateData certificateFromDatabase) {
		if (certificateFromDatabase.revoked) {
    		return false;
    	}
    	
    	if (!certificateFromDatabase.validFrom.before(new Date()) || !certificateFromDatabase.validUntil.after(new Date())) {
    		return false;
    	}
    	
    	Certificate certificateFromKeyStore = null;
    	Certificate issurerCertificateFromKeyStore = null;
    	CertificateData issurerCertificateDatabase = null;
    	
    	if (certificateFromDatabase.type == CertificateType.end_entity) {
    		certificateFromKeyStore = this.keyStoreReader.readCertificate("EndEntityKeyStore.jks", "endentitykeystore", certificateFromDatabase.serialCode);
    		issurerCertificateDatabase = this.certificateRepository.findBySubjectKeyId(certificateFromDatabase.issuerKeyId);
    		issurerCertificateFromKeyStore = this.keyStoreReader.readCertificate("EndEntityKeyStore.jks", "endentitykeystore", issurerCertificateDatabase.serialCode);
    	}  
    	
    	try {
			certificateFromKeyStore.verify(issurerCertificateFromKeyStore.getPublicKey());
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			e.printStackTrace();
		}
    	
    	return true;
	}
	
	private Boolean checkRoot(CertificateData certificateFromDatabase) {
		
		if (certificateFromDatabase.revoked) {
    		return false;
    	}
    	
    	if (!certificateFromDatabase.validFrom.before(new Date()) || !certificateFromDatabase.validUntil.after(new Date())) {
    		return false;
    	}
    	
    	return true;
	}
}
