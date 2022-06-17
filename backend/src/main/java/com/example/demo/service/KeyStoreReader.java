package com.example.demo.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Service;

import com.example.demo.model.IssuerData;

@Service
public class KeyStoreReader {
	//KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
	//Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
	// - Sertifikati koji ukljucuju javni kljuc
	// - Privatni kljucevi
	// - Tajni kljucevi, koji se koriste u simetricnima siframa
	private KeyStore keyStore;
	
	final static Logger loggerErr = Logger.getLogger("errorLogger"); 
	final static Logger loggerInfo = Logger.getLogger("infoLogger");
	final static Logger loggerWarn = Logger.getLogger("warnLogger");
	
	public KeyStoreReader() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
	 * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
	 * 
	 * @param keyStoreFile - datoteka odakle se citaju podaci
	 * @param alias - alias putem kog se identifikuje sertifikat izdavaoca
	 * @param password - lozinka koja je neophodna da se otvori key store
	 * @param keyPass - lozinka koja je neophodna da se izvuce privatni kljuc
	 * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
	 */
	public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
		try {
			//Datoteka se ucitava
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, password);
			//Iscitava se sertifikat koji ima dati alias
			Certificate cert = keyStore.getCertificate(alias);
			//Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
			PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			return new IssuerData(privKey, issuerName);
		} catch (KeyStoreException e) {
			loggerErr.error("FRIFKS");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			loggerErr.error("FRIFKS");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			loggerErr.error("FRIFKS");
			e.printStackTrace();
		} catch (CertificateException e) {
			loggerErr.error("FRIFKS");
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			loggerErr.error("FRIFKS");
			e.printStackTrace();
		} catch (IOException e) {
			loggerErr.error("FRIFKS");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Ucitava sertifikat is KS fajla
	 */
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			
			if(ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;
			}
		} catch (KeyStoreException e) {
			loggerErr.error("FRCRT");
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			loggerErr.error("FRCRT");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			loggerErr.error("FRCRT");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			loggerErr.error("FRCRT");
			e.printStackTrace();
		} catch (CertificateException e) {
			loggerErr.error("FRCRT");
			e.printStackTrace();
		} catch (IOException e) {
			loggerErr.error("FRCRT");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Ucitava privatni kljuc is KS fajla
	 */
	public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			
			if(ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (KeyStoreException e) {
			loggerErr.error("FRPKY");
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			loggerErr.error("FRPKY");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			loggerErr.error("FRPKY");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			loggerErr.error("FRPKY");
			e.printStackTrace();
		} catch (CertificateException e) {
			loggerErr.error("FRPKY");
			e.printStackTrace();
		} catch (IOException e) {
			loggerErr.error("FRPKY");
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			loggerErr.error("FRPKY");
			e.printStackTrace();
		}
		return null;
	}
}

