package com.example.demo.controller;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.NewUserDto;
import com.example.demo.model.AppUser;
import com.example.demo.model.UserType;
import com.example.demo.service.AppUserService;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	private AppUserService appUserService;
	private JavaMailSender javaMailSender;
	
	private static HashMap<String, String> verification = new HashMap<String, String>();
	
	
	public RegistrationController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}

	@PostMapping(path = "/registerUser")
	public boolean registerUser(@RequestBody NewUserDto user)
	{	
		
		Optional<AppUser> oldUser = Optional.ofNullable(appUserService.findByEmail(user.email)); // Mail -> Korisnik
		if(!oldUser.isPresent()) {
			
			String verificationCode = generateVerificationCode();
			if(!verification.containsKey(user.email))
			{
				verification.put(user.email, verificationCode);	
			}
			String link = "http://localhost:8082/#/emailVerification?userCode=" + verificationCode;
			
			String body = "Hello,\nThank you for registering on our website. Below is your verification code.\n" 
							  + "Your Code is: " + verificationCode + "\n Or you can verify your account when you click on this link:"
							  + "<a href=" + link + ">ACTIVATE ACCOUNT</a>" +
							    "\nIf you have any trouble, write to our support : isa.projekat.tester@gmail.com";
			String title = "Verification Code";
			try 
			{
				Thread t = new Thread() {
					public void run()
					{
						sendEmail(user.email,body,title);		
					}
				};
				t.start();	
				user.verified = false;
				user.verificationCode = verificationCode;
					
				AppUser appUser = new AppUser(user.name, user.surname, user.email, user.password, user.address, user.city, user.country, UserType.end_user);
				appUserService.save(appUser);		
				return true;
			} 
			catch (Exception e) 
			{
				return false;
			}
		}
		System.out.println("Korisnik sa ovim mailom postoji ili je nepostojeci mail.");
		return false;
	}
	

	@PostMapping(path = "/emailVerification")
    public boolean verify(@RequestBody String userCode)
	{	
		String codeTokens = userCode;
		String code = codeTokens.split("=")[0];
		AppUser user = appUserService.findByVerificationCode(code);
		if(user != null)
		{
			if(user.verificationCode.equalsIgnoreCase(code))
			{
				user.verified = true;
				user.verificationCode = null;
				appUserService.save(user);
				return true;
			}
		}
		return false;
	}
	
	private String generateVerificationCode()
	{
		Random rand = new Random();
		String verificationCode = "";
		for(int i = 0 ; i < 6 ; i++)
		{
			verificationCode += String.valueOf(rand.nextInt(10));
		}
		return verificationCode;
	}
	
	public void sendEmail(String to, String body, String topic)
	{
		 
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(topic);
		msg.setText(body);
		//javaMailSender.send(msg);
		System.out.println("Email sent...");
	}
	
	
	public boolean isNumber(String st) {
		try {
			Integer.parseInt(st);
			return true;
		}catch(NumberFormatException ex){
			return false;
		}
	}
}

