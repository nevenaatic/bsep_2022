package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.UserTokenState;
import com.example.demo.dto.NewUserDto;
import com.example.demo.service.RegistrationService;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	@Autowired
	private RegistrationService registrationService;
	
	public RegistrationController() {
	}

	@PostMapping(path = "/registerUser")
	public ResponseEntity<Boolean> registerUser(@RequestBody NewUserDto user)
	{	
		return registrationService.registerUser(user);
	}
	

	@PostMapping(path = "/emailVerification")
    public ResponseEntity<Boolean> verify(@RequestBody String userCode)
	{	
		return registrationService.verify(userCode);
	}
	/*
	@PostMapping(path = "/login/{email}/{password}")
	public UserTokenState loginUser(@PathVariable("email") String email, @PathVariable("password") String password)
	{	
		return registrationService.loginUser(email, password);
	}
*/
}

