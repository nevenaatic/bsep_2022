package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.UserTokenState;
import com.example.demo.model.AppUser;
import com.example.demo.dto.JwtAuthenticationRequest;
import com.example.demo.dto.NewUserDto;
import com.example.demo.service.RegistrationService;

import utils.TokenUtils;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	@Autowired
	private RegistrationService registrationService;

	private AuthenticationManager authenticationManager;

	private TokenUtils tokenUtils;
	
	public RegistrationController() {
	}
	
	public RegistrationController(AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
		this.authenticationManager = authenticationManager;
		this.tokenUtils = tokenUtils;
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
	
	@PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {

        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        AppUser user = (AppUser) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.email);
        int expiresIn = tokenUtils.getExpiredIn();
        if (user.isEnabled() == false){
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn,user.role.getName(), user.isEnabled(),user.isMust_change_password()));
        }

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn,user.role.getName(), user.isEnabled(),user.isMust_change_password()));
    }
	
	/*
	@PostMapping(path = "/login/{email}/{password}")
	public UserTokenState loginUser(@PathVariable("email") String email, @PathVariable("password") String password)
	{	
		return registrationService.loginUser(email, password);
	}
*/
}

