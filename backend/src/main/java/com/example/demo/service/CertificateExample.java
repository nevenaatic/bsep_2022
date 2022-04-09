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

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.example.demo.model.IssuerData;
import com.example.demo.model.SubjectData;

public class CertificateExample {
	
	public static KeyStoreWriter ksw = new KeyStoreWriter();
	public static KeyStoreReader ksr = new KeyStoreReader();
	
	public CertificateExample() {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public void testIt() {
		try {
			SubjectData subjectData = generateSubjectData();
			
			KeyPair keyPairIssuer = generateKeyPair();
			IssuerData issuerData = generateIssuerData(keyPairIssuer.getPrivate());
		    
			//Generise se sertifikat za subjekta, potpisan od strane issuer-a
			CertificateGenerator cg = new CertificateGenerator();
			X509Certificate cert = cg.generateCertificate(subjectData, issuerData);
			
			System.out.println("\n===== Podaci o izdavacu sertifikata =====");
			System.out.println(cert.getIssuerX500Principal().getName());
			System.out.println("\n===== Podaci o vlasniku sertifikata =====");
			System.out.println(cert.getSubjectX500Principal().getName());
			System.out.println("\n===== Sertifikat =====");
			System.out.println("-------------------------------------------------------");
			System.out.println(cert);
			System.out.println("-------------------------------------------------------");
			
			//Moguce je proveriti da li je digitalan potpis sertifikata ispravan, upotrebom javnog kljuca izdavaoca
			cert.verify(keyPairIssuer.getPublic());
			System.out.println("\nValidacija uspesna :)");
			String pass = "rootkeystore";
			//ksw.loadKeyStore("RootKeyStore.jks", pass.toCharArray());
			//ksw.write("qbcdefgh",keyPairIssuer.getPrivate(), pass.toCharArray(), cert);
			//ksw.saveKeyStore("RootKeyStore.jks", pass.toCharArray());
			System.out.println(ksr.readCertificate("RootKeyStore.jks", pass, "qbcdefgh"));
			//Ovde se desava exception, jer se validacija vrsi putem drugog kljuca
			KeyPair anotherPair = generateKeyPair();
			cert.verify(anotherPair.getPublic());
		} catch(CertificateException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			System.out.println("\nValidacija neuspesna :(");
			e.printStackTrace();
		}
	}
	
	private IssuerData generateIssuerData(PrivateKey issuerKey) {
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
	    builder.addRDN(BCStyle.CN, "Nikola Luburic");
	    builder.addRDN(BCStyle.SURNAME, "Luburic");
	    builder.addRDN(BCStyle.GIVENNAME, "Nikola");
	    builder.addRDN(BCStyle.O, "UNS-FTN");
	    builder.addRDN(BCStyle.OU, "Katedra za informatiku");
	    builder.addRDN(BCStyle.C, "RS");
	    builder.addRDN(BCStyle.E, "nikola.luburic@uns.ac.rs");
	    //UID (USER ID) je ID korisnika
	    builder.addRDN(BCStyle.UID, "654321");

		//Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
	    // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
	    // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
		return new IssuerData(issuerKey, builder.build());
	}

	private SubjectData generateSubjectData() {
		try {
			KeyPair keyPairSubject = generateKeyPair();
			
			//Datumi od kad do kad vazi sertifikat
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse("2017-12-31");
			Date endDate = iso8601Formater.parse("2022-12-31");
			
			//Serijski broj sertifikata
			String sn="1";
			//klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		    builder.addRDN(BCStyle.CN, "Goran Sladic");
		    builder.addRDN(BCStyle.SURNAME, "Sladic");
		    builder.addRDN(BCStyle.GIVENNAME, "Goran");
		    builder.addRDN(BCStyle.O, "UNS-FTN");
		    builder.addRDN(BCStyle.OU, "Katedra za informatiku");
		    builder.addRDN(BCStyle.C, "RS");
		    builder.addRDN(BCStyle.E, "sladicg@uns.ac.rs");
		    //UID (USER ID) je ID korisnika
		    builder.addRDN(BCStyle.UID, "123456");
		    
		    //Kreiraju se podaci za sertifikat, sto ukljucuje:
		    // - javni kljuc koji se vezuje za sertifikat
		    // - podatke o vlasniku
		    // - serijski broj sertifikata
		    // - od kada do kada vazi sertifikat
		    return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private KeyPair generateKeyPair() {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static void main(String[] args) {
		CertificateExample certificateExample = new CertificateExample();
		certificateExample.testIt();
	}
}

