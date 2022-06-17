package com.example.demo.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import com.example.demo.dto.CertificateFrontDto;
import com.example.demo.model.AppUser;
import com.example.demo.model.CertificateData;
import com.example.demo.model.CertificateType;
import com.example.demo.model.UserType;
import com.example.demo.repository.CertificateRepository;

@Service
public class CertificateService {

	@Autowired
	private CertificateRepository certificateRepository;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private KeyStoreReader keyStoreReader;
	
	
	final static Logger loggerErr = Logger.getLogger("errorLogger");
	final static Logger loggerInfo = Logger.getLogger("infoLogger");
	final static Logger loggerWarn = Logger.getLogger("warnLogger");
	
	public List<CertificateData> findAll() {
		return certificateRepository.findAll();
	}
	
    public boolean revokeCertificate(String serialNumber) {
    	CertificateData certificate = certificateRepository.getCertificateByCode(serialNumber);
    	List <CertificateData> allCerts = certificateRepository.findAll();
		for (CertificateData c : allCerts) {
			if (c.id == certificate.id) {
				c.revoked = true;
				loggerWarn.warn("CRTR SN " + serialNumber );
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
	    			if (keyIdValues.contains(c.issuerKeyId) && !c.revoked) {
	    				c.revoked = true;
	    				if (c.certificateType == CertificateType.CA) {
	    					keyIdValues.add(c.subjectKeyId);
	    				}
	    			}
	    		}
    		}
    	certificateRepository.saveAll(allCerts);
    	updateUserStatuses();
    	}
    	return true;
    }
    
    private void updateUserStatuses() {
    	List<AppUser> users = appUserService.findAll();
    	for (AppUser au : users) {
    		boolean isCA = false;
    		if (!au.role.getName().equals("admin")) {
    			for (CertificateData c : certificateRepository.getCertificatesBySubjectUserId(au.id))
    				if (!c.revoked && c.certificateType == CertificateType.CA)
    					isCA = true;
    		}
    		if (!isCA) {
    			au.role.setName("end_user");
    		} else {
    			au.role.setName("certification_authority");
    		}
    		loggerInfo.info("SFUC | UI  " + au.id);
    		loggerWarn.warn("SFUC | UI " + au.id);
    	}
    	
    	appUserService.saveAll(users);
    }
    
    public boolean isCertificateValidTEST(String serialCode) throws Exception, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, GeneralSecurityException {
    	CertificateData certificateFromDatabase = certificateRepository.getCertificateByCode(serialCode);
    	
    	if (certificateFromDatabase.revoked) {
    		loggerInfo.info("CRTIV   " + serialCode );
    		return false;
    	}
    	
    	if (!certificateFromDatabase.validFrom.before(new Date()) || !certificateFromDatabase.validUntil.after(new Date())) {
    		loggerInfo.info("CRTINV   " + serialCode );
    		return false;
    	}
    	try {
	    	CertificateData issurerCertificateDatabase;
	    	Certificate certificateFromKeyStore;
	    	Certificate issurerCertificateFromKeyStore;
	    	certificateFromKeyStore = this.keyStoreReader.readCertificate("EndEntityKeyStore.jks", "endentitykeystore", certificateFromDatabase.serialCode);
			issurerCertificateDatabase = this.certificateRepository.findBySubjectKeyId(certificateFromDatabase.issuerKeyId);
			issurerCertificateFromKeyStore = this.keyStoreReader.readCertificate("Keys.jks", "keys", certificateFromDatabase.serialCode);	
			issurerCertificateFromKeyStore.verify(issurerCertificateFromKeyStore.getPublicKey());
    	} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
    		loggerErr.error(" FCCRTV " + serialCode );
    		
    		return false;
		}
    	return true;
    }
    
    public Boolean isCertificateValid(String serialCode) {
    	CertificateData certificateFromDatabase = certificateRepository.getCertificateByCode(serialCode);
    	Certificate issurerCertificateFromKeyStore = null;
    	CertificateData issurerCertificateDatabase = null;
    	Boolean ret = null;
    	
    	if (certificateFromDatabase.certificateType == CertificateType.CA && certificateFromDatabase.issuerUserId == certificateFromDatabase.subjectUserId) {
    		return this.checkRoot(certificateFromDatabase);
    	}
    	
    	if (certificateFromDatabase.certificateType == CertificateType.CA) {
    		ret = this.checkCA(certificateFromDatabase);
    		issurerCertificateDatabase = this.certificateRepository.findBySubjectKeyId(certificateFromDatabase.issuerKeyId);
    		issurerCertificateFromKeyStore = this.keyStoreReader.readCertificate("CAKeyStore.jks", "cakeystore", issurerCertificateDatabase.serialCode);	
    		if (issurerCertificateDatabase.certificateType != CertificateType.root) {
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
    	
    	if (certificateFromDatabase.certificateType == CertificateType.CA) {
    		certificateFromKeyStore = this.keyStoreReader.readCertificate("CAKeyStore.jks", "cakeystore", certificateFromDatabase.serialCode);
    		issurerCertificateDatabase = this.certificateRepository.findBySubjectKeyId(certificateFromDatabase.issuerKeyId);
    		issurerCertificateFromKeyStore = this.keyStoreReader.readCertificate("CAKeyStore.jks", "cakeystore", issurerCertificateDatabase.serialCode);	
    	} 
    	
    	try {
			certificateFromKeyStore.verify(issurerCertificateFromKeyStore.getPublicKey());
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			loggerErr.error(" FCCA  | UI " + loggedUser().id);
			
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
    	
    	if (certificateFromDatabase.certificateType == CertificateType.end_entity) {
    		certificateFromKeyStore = this.keyStoreReader.readCertificate("EndEntityKeyStore.jks", "endentitykeystore", certificateFromDatabase.serialCode);
    		issurerCertificateDatabase = this.certificateRepository.findBySubjectKeyId(certificateFromDatabase.issuerKeyId);
    		issurerCertificateFromKeyStore = this.keyStoreReader.readCertificate("EndEntityKeyStore.jks", "endentitykeystore", issurerCertificateDatabase.serialCode);
    	}  
    	
    	try {
			certificateFromKeyStore.verify(issurerCertificateFromKeyStore.getPublicKey());
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			loggerErr.error(" FCEE | UI " + loggedUser().id);
			
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
	
	private X509Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                InputStream inp = new ByteArrayInputStream(cert.getEncoded());
                return (X509Certificate)certFactory.generateCertificate(inp);
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException | IOException e) {
        	loggerErr.error("FLKS");
        }
        return null;
    }
	
	public boolean extractCertificate(String serialNumber) throws CertificateException, IOException {
		CertificateData cert = certificateRepository.getCertificateByCode(serialNumber);
		String pass = "rootkeystore";
		String file = "RootKeyStore.jks";
		if (!(cert.subjectKeyId == cert.issuerKeyId)) {
			if (cert.certificateType == CertificateType.CA) {
				pass = "cakeystore";
				file = "CAKeyStore.jks";
			}
			else {
				pass = "endentitykeystore";
				file = "EndEntityKeyStore.jks";
			}
		}
        X509Certificate certificate = readCertificate(file, pass, cert.serialCode);
        System.out.println(certificate.getSerialNumber());
        FileOutputStream os = new FileOutputStream(cert.serialCode + ".crt");
        os.write("-----BEGIN CERTIFICATE-----\n".getBytes(StandardCharsets.US_ASCII));
        os.write(Base64.getEncoder().encode(certificate.getEncoded()));
        os.write("\n-----END CERTIFICATE-----\n".getBytes(StandardCharsets.US_ASCII));
        os.close();
        AppUser user= this.loggedUser();
        loggerInfo.info("EXCRT  " + serialNumber + " | UI " + user.id);
        
       // if(!certificateDto.getAuthoritySubject().equals("ca"))
            return true;
        /*PrivateKey key = new KeyStoreReader().readPrivateKey(env.getProperty("keystore.path") + "keys.jks", "12345", certificateDto.getSerialNumberSubject(), "12345");
        os = new FileOutputStream(certificateDto.getSerialNumberSubject() + "-key" + ".pem");
        os.write("-----BEGIN PRIVATE KEY-----\n".getBytes(StandardCharsets.US_ASCII));
        os.write(Base64.getEncoder().encode(key.getEncoded()));
        os.write("\n-----END PRIVATE KEY-----\n".getBytes(StandardCharsets.US_ASCII));
        os.close();
        return true;*/
    }
	
	public List <CertificateFrontDto> formatCertificateFrontData() {
		List <CertificateFrontDto> frontData = new ArrayList<CertificateFrontDto>();
		for (CertificateData cd : findAll()) {
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
	
	public List <CertificateFrontDto> formatSubjectCertificateFrontData(long id) {
		List <CertificateFrontDto> frontData = new ArrayList<CertificateFrontDto>();
		AppUser subject = appUserService.findById(id);
		for (CertificateData cd : findAll()) {
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
	
	public List <CertificateFrontDto> formatIssuedCertificatesFrontData(long id) {
		List <CertificateFrontDto> frontData = new ArrayList<CertificateFrontDto>();
		AppUser issuer = appUserService.findById(id);
		for (CertificateData cd : findAll()) {
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
	
	public AppUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (AppUser)authentication.getPrincipal();
	}
}
