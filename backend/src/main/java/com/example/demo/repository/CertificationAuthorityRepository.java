package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.CertificationAuthority;

@Repository
public interface CertificationAuthorityRepository extends JpaRepository<CertificationAuthority, Long>{
}

