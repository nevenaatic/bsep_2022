package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.NewUserDto;
import com.example.demo.model.AppUser;
import com.example.demo.model.UserType;
import com.example.demo.model.UserVerifications;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.UserVerificationsRepository;

@Service
public class RegistrationService {
	
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private UserVerificationsRepository userVerificationsRepository;
	
	public ResponseEntity<Boolean> registerUser(NewUserDto user)
	{	
		System.out.println(user.toString());
		String verificationCode = generateVerificationCode();
		Optional<AppUser> oldUser = Optional.ofNullable(appUserRepository.findByEmail(user.email)); // Mail -> Korisnik
		if(!oldUser.isPresent()) {
			userVerificationsRepository.save(new UserVerifications(user.email, verificationCode, LocalDateTime.now()));
			
			String body = "Hello,\nThank you for registering on our website. Below is your verification code and it will last for the NEXT 10 MINUTES.\n\n" 
							  + "Your Code is: " + verificationCode + 
							    "\n\n If you have any trouble, write to our support : isa.projekat.tester@gmail.com";
			
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
				AppUser appUser = new AppUser(user.name, user.surname, user.email, user.password, user.address, user.city, user.country, UserType.end_user);
				appUserRepository.save(appUser);		
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} 
			catch (Exception e) 
			{
				return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		System.out.println("Korisnik sa ovim mailom postoji ili je nepostojeci mail.");
		return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
	}
	
    public ResponseEntity<Boolean> verify(String userCode)
	{	
    	String code = userCode.split("=")[0];
    	System.out.println(code);
		UserVerifications verification = userVerificationsRepository.getByVerificationCode(code);
		if(verification != null && verification.timeOfRequest.isBefore(verification.timeOfRequest.plusMinutes(10))) {
			AppUser user = appUserRepository.getUserByEmail(verification.email);
				user.verified = true;
				user.verificationCode = verification.verificationCode;
				appUserRepository.save(user);
				userVerificationsRepository.delete(verification);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		if(verification != null) {
			userVerificationsRepository.delete(verification);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public void sendEmail(String to, String body, String topic)
	{
		 
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(topic);
		msg.setText(body);
		javaMailSender.send(msg);
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
	
	private String generateVerificationCode()
	{
		Random rand = new Random();
		String verificationCode = "";
		for(int i = 0 ; i < 8 ; i++)
		{
			verificationCode += String.valueOf(rand.nextInt(10));
		}
		return verificationCode;
	}
}
