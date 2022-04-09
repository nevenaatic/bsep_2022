package com.example.demo.service;

import com.example.demo.repository.CertificationAuthorityRepository;

import org.springframework.stereotype.Service;

@Service
public class CertificationAuthorityService {

    private CertificationAuthorityRepository certificationAuthorityRepository;

    public CertificationAuthorityService(CertificationAuthorityRepository certificationAuthorityRepository){
        this.certificationAuthorityRepository = certificationAuthorityRepository;

    }
}

