package com.example.demo.controller;

import com.example.demo.dto.CertificateFrontDto;
import com.example.demo.dto.UserFrontDto;
import com.example.demo.service.AppUserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="appUser")
public class AppUserController {
	
	@Autowired
    private AppUserService appUserService;

	@GetMapping(path = "/getAllUsers")
	public ResponseEntity<List<UserFrontDto>> getAllUsers()
	{
		return new ResponseEntity<List<UserFrontDto>>(appUserService.getUsersDto(), HttpStatus.OK);
	}

}

