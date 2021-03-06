package com.example.demo.service;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.IssuerInfoDTO;
import com.example.demo.dto.SubjectInfoDTO;
import com.example.demo.model.AppUser;
import com.example.demo.model.CertificateData;
import com.example.demo.model.CertificateType;
import com.example.demo.model.IssuerData;
import com.example.demo.model.PermissionRole;
import com.example.demo.model.Role;
import com.example.demo.model.SubjectData;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.CertificateRepository;

@Service
public class CertificateExample {
	
	public static KeyStoreWriter ksw = new KeyStoreWriter();
	public static KeyStoreReader ksr = new KeyStoreReader();
	@Autowired
	private CertificateRepository certificateRepository; 
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private RoleService roleService; 
	@Autowired
	private PermissionRoleService permissionRoleService; 
	
	final static Logger loggerErr = Logger.getLogger("errorLogger");
	final static Logger loggerInfo = Logger.getLogger("infoLogger");
	final static Logger loggerWarn = Logger.getLogger("warnLogger");
	
	public CertificateExample() {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public boolean saveCertificate(IssuerInfoDTO issuer, SubjectInfoDTO subject, String dateFrom, String dateUntil, boolean isCA, List<Integer> extensions) throws CertIOException {
		try {
			SubjectData subjectData = generateSubjectData(subject, dateFrom, dateUntil);
			KeyPair keyPairIssuer = generateKeyPair();
			IssuerData issuerData = generateIssuerData(keyPairIssuer.getPrivate(), issuer);
		    System.out.println(keyPairIssuer.getPrivate());
		    System.out.println(keyPairIssuer.getPublic());
			//Generise se sertifikat za subjekta, potpisan od strane issuer-a
			CertificateGenerator cg = new CertificateGenerator();
			X509Certificate cert = cg.generateCertificate(subjectData, issuerData, isCA, extensions);
			String subjectKeyId = getKeyID(40);
			long issuerId = appUserRepository.getUserByEmail(issuer.email).id;
			CertificateData certData = new CertificateData(subjectData.getSerialNumber(), 
					appUserRepository.getUserByEmail(subject.email).id, 
					issuerId, 
					subjectKeyId, 
					getCACertificateById(issuerId), 
					subjectData.getStartDate(),
					subjectData.getEndDate(),
					false, 
					CertificateType.end_entity);
			if (isCA)
				certData.certificateType = CertificateType.CA;
			
			//Moguce je proveriti da li je digitalan potpis sertifikata ispravan, upotrebom javnog kljuca izdavaoca
			cert.verify(keyPairIssuer.getPublic());
			System.out.println("\nValidacija uspesna :)");
			
			loggerWarn.warn("NCC | UI  " + issuerId);
			loggerInfo.info("NCC | UI  " + issuerId);
			certificateRepository.save(certData);
			AppUser subj = appUserRepository.findById(Long.parseLong(subject.userId)).get();
			if (isCA) {
				Role role = roleService.findByName("ROLE_CA");
				subj.role = role;
			    appUserRepository.save(subj);
					loggerInfo.info("NCAC | UI  " + issuerId);
			}
	            KeyStoreWriter privateKeys = new KeyStoreWriter();
	            privateKeys.loadKeyStore("keys.jks", "keys".toCharArray());
	            privateKeys.write(subjectData.getSerialNumber(), keyPairIssuer.getPrivate(), "keys".toCharArray(), cert);
	            privateKeys.saveKeyStore("keys.jks", "keys".toCharArray());
			
			String pass = "rootkeystore";
			String file = "RootKeyStore.jks";
			if (!subject.email.equalsIgnoreCase(issuer.email)) {
				if (isCA) {
					pass = "cakeystore";
					file = "CAKeyStore.jks";
				}
				else {
					pass = "endentitykeystore";
					file = "EndEntityKeyStore.jks";
				}
			}
			ksw.loadKeyStore(file, pass.toCharArray());
			ksw.write(subjectData.getSerialNumber(), keyPairIssuer.getPrivate(), pass.toCharArray(), cert);
			ksw.saveKeyStore(file, pass.toCharArray());
			return true;
			//System.out.println(ksr.readCertificate("RootKeyStore.jks", pass, "qbcdefgh"));
			//Ovde se desava exception, jer se validacija vrsi putem drugog kljuca
		} catch(CertificateException e) {
			loggerErr.error("FSCRT | UI  " + this.loggedUser().id +"  ");
			
			return false;
		} catch (InvalidKeyException e) {
			loggerErr.error("FSCRT | UI " + this.loggedUser().id + " ");
			
			return false;
		} catch (NoSuchAlgorithmException e) {
			loggerErr.error("FSCRT | UI " + this.loggedUser().id +" - algotithm error ");
			
			return false;
		} catch (NoSuchProviderException e) {
			loggerErr.error("FSCRT | UI "  + this.loggedUser().id);
			
			return false;
		} catch (SignatureException e) {
			loggerErr.error("FSCRT | UI "  + this.loggedUser().id +" - validation exception  ");
			System.out.println("\nValidacija neuspesna :(");
			
			return false; 
		}
		
	}
	
	public String getCACertificateById(long id) {
		for (CertificateData cd : certificateRepository.findAll()) {
			if (cd.subjectUserId == id && cd.certificateType == CertificateType.CA && !cd.revoked)
				return cd.subjectKeyId;
		}
		return null;
	}
	
	public IssuerData generateIssuerData(PrivateKey issuerKey, IssuerInfoDTO issuer) {
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
	    builder.addRDN(BCStyle.CN, issuer.name + " " + issuer.surname);
	    builder.addRDN(BCStyle.SURNAME, issuer.surname);
	    builder.addRDN(BCStyle.GIVENNAME, issuer.name);
	    builder.addRDN(BCStyle.C, "RS");
	    builder.addRDN(BCStyle.E, issuer.email);
	    //UID (USER ID) je ID korisnika
	    builder.addRDN(BCStyle.UID, issuer.userId);
	    
		//Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
	    // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
	    // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
	    loggerInfo.info("GID | UI  " + this.loggedUser().id);
		return new IssuerData(issuerKey, builder.build());
	}

	public SubjectData generateSubjectData(SubjectInfoDTO subject, String dateFrom, String dateUntil) {
		try {
			KeyPair keyPairSubject = generateKeyPair();
			
			//Datumi od kad do kad vazi sertifikat
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse(dateFrom);
			Date endDate = iso8601Formater.parse(dateUntil);
			
			//Serijski broj sertifikata
			String sn = getSerialCode(32);
			//klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		    builder.addRDN(BCStyle.CN, subject.name + " " + subject.surname);
		    builder.addRDN(BCStyle.SURNAME, subject.surname);
		    builder.addRDN(BCStyle.GIVENNAME, subject.name);
		    builder.addRDN(BCStyle.C, "RS");
		    builder.addRDN(BCStyle.E, subject.email);
		    //UID (USER ID) je ID korisnika
		    builder.addRDN(BCStyle.UID, subject.userId);
		    builder.addRDN(BCStyle.SERIALNUMBER, sn);

		    //Kreiraju se podaci za sertifikat, sto ukljucuje:
		    // - javni kljuc koji se vezuje za sertifikat
		    // - podatke o vlasniku
		    // - serijski broj sertifikata
		    // - od kada do kada vazi sertifikat
		    loggerInfo.info("GSD | UI  " + this.loggedUser().id);
		    return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
		} catch (ParseException e) {
			loggerErr.error("FGSD | UI " + this.loggedUser().id);
			
		}
		return null;
	}

	public KeyPair generateKeyPair() {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
        	loggerErr.error("FWGKP | UI"  + this.loggedUser().id);
			
		} catch (NoSuchProviderException e) {
			loggerErr.error("FWGKP | UI"  + this.loggedUser().id );
			
		}
        return null;
	}
	
	static String getSerialCode(int n)
    {
  
        // chose a Character random from this String
        String AlphaNumericString = 
                                    "0123456789"
                                   ;
  
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
  
        for (int i = 0; i < n; i++) {
  
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
  
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
        return sb.toString();
    }
	
	static String getKeyID(int n)
    {
  
        // chose a Character random from this String
        String AlphaNumericString = "0123456789"
                                    + "abcdef";
  
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
  
        for (int i = 0; i < n; i++) {
  
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
  
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
        return sb.toString();
    }
	
	
	public AppUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (AppUser)authentication.getPrincipal();
	}
}

