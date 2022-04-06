package com.example.demo.service;

import com.example.demo.repository.IssuerDataRepository;
import org.springframework.stereotype.Service;

@Service
public class IssuerDataService {

    private IssuerDataRepository issuerDataRepository;

    public IssuerDataService(IssuerDataRepository issuerDataRepository){
        this.issuerDataRepository = issuerDataRepository;

    }
}
