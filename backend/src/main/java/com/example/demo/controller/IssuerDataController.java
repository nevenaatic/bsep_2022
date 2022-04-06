package com.example.demo.controller;

import com.example.demo.service.IssuerDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="issuer")
public class IssuerDataController {
    private IssuerDataService issuerDataService;

    public  IssuerDataController(IssuerDataService issuerDataService){
        this.issuerDataService = issuerDataService;
    }



}
