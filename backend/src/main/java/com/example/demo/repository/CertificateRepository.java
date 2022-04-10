package com.example.demo.repository;

import java.security.cert.Certificate;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CertificateData;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateData, Long>{
	
	@Query("SELECT cd FROM CertificateData cd WHERE cd.serialCode=?1")
	public CertificateData getCertificateByCode(String serialCode);

	public CertificateData findBySubjectKeyId(String issuerKeyId);

}
