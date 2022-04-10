package com.example.demo.controller;

import com.example.demo.model.AppUser;
import com.example.demo.service.AppUserService;

import dto.LoginInfoDTO;
import dto.UserInfoBackDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="appUser")
public class AppUserController {
    private AppUserService appUserService;

    public  AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoBackDTO> login(@RequestBody LoginInfoDTO info){
    	
    	AppUser user = appUserService.findByEmail(info.getEmail());
    	
    	if (user == null) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	} else if (!user.password.equals(info.getPassword())) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	return new ResponseEntity<UserInfoBackDTO>(new UserInfoBackDTO(user.email, user.role), HttpStatus.OK);
    	
    }
    
}

