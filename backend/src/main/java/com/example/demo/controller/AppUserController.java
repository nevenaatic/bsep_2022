package com.example.demo.controller;

import com.example.demo.service.AppUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="appUser")
public class AppUserController {
    private AppUserService appUserService;

    public  AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }



}

