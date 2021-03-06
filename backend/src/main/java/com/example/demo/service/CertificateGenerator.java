package com.example.demo.service;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import com.example.demo.model.IssuerData;
import com.example.demo.model.SubjectData;


public class CertificateGenerator {
	
	final static Logger loggerErr = Logger.getLogger("errorLogger");
	final static Logger loggerInfo = Logger.getLogger("infoLogger");
	final static Logger loggerWarn = Logger.getLogger("warnLogger");
	
	public CertificateGenerator() {}

	public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, boolean isCA, List<Integer> extensions) throws CertIOException {
		
		try {
			//Posto klasa za generisanje sertifiakta ne moze da primi direktno privatni kljuc pravi se builder za objekat
			//Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
			//Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
			//Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
			builder = builder.setProvider("BC");

			//Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
			ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

			//Postavljaju se podaci za generisanje sertifiakta
			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
					new BigInteger(subjectData.getSerialNumber()),
					subjectData.getStartDate(),
					subjectData.getEndDate(),
					subjectData.getX500name(),
					subjectData.getPublicKey());
			ASN1EncodableVector purposes = new ASN1EncodableVector();
			  purposes.add(KeyPurposeId.id_kp_clientAuth);
			  purposes.add(KeyPurposeId.anyExtendedKeyUsage);

			  int allKeyUsages = 0;
				for (int i = 0; i < extensions.size(); i++) {
					allKeyUsages = allKeyUsages + extensions.get(i);
				}
				if(extensions.size() > 0)
					certGen.addExtension(Extension.keyUsage, true, new KeyUsage(allKeyUsages));

			  certGen.addExtension(Extension.extendedKeyUsage, false, new DERSequence(purposes));
			  certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(isCA));
			//Generise se sertifikat
			X509CertificateHolder certHolder = certGen.build(contentSigner);
			//certGen.addExtension(Extension.basicConstraints, true, )
			//Builder generise sertifikat kao objekat klase X509CertificateHolder
			//Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");

			
			//Konvertuje objekat u sertifikat
			return certConverter.getCertificate(certHolder);
		} catch (CertificateEncodingException e) {
			loggerErr.error("CRTERR");
		} catch (IllegalArgumentException e) {
			loggerErr.error("CRTERR");
		} catch (IllegalStateException e) {
			loggerErr.error("CRTERR");
		} catch (OperatorCreationException e) {
			loggerErr.error("CRTERR");
		} catch (CertificateException e) {
			loggerErr.error("CRTERR");
		}
		return null;
	}
}

