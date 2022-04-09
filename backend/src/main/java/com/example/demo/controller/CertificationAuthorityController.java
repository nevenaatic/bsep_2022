package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.CertificationAuthorityService;

@Controller
@RequestMapping(value="certificationAuthority")
public class CertificationAuthorityController {

	private CertificationAuthorityService certificationAuthorityService;

    public  CertificationAuthorityController(CertificationAuthorityService certificationAuthorityService){
        this.certificationAuthorityService = certificationAuthorityService;
    }
}
