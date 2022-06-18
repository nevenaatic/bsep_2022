package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.JwtAuthenticationRequest;
import com.example.demo.dto.NewUserDto;
import com.example.demo.dto.UserTokenState;
import com.example.demo.model.AppUser;
import com.example.demo.model.PasswordChange;
import com.example.demo.model.PasswordlessInfo;
import com.example.demo.model.PermissionRole;
import com.example.demo.model.Role;
import com.example.demo.model.UserVerifications;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.PasswordChangeRepository;
import com.example.demo.repository.PasswordlessInfoRepository;
import com.example.demo.repository.UserVerificationsRepository;
import com.example.demo.utils.TokenUtils;

@Service
public class RegistrationService {
	
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private UserVerificationsRepository userVerificationsRepository;
	@Autowired
	private PasswordlessInfoRepository passwordlessInfoRepository;
	@Autowired
	private PasswordChangeRepository passwordChangeRepository;
	@Autowired
	private RoleService roleService; 
	@Autowired
	private PermissionRoleService permissionRoleService; 
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenUtils tokenUtils;
	final static Logger loggerErr = Logger.getLogger("errorLogger"); 
	final static Logger loggerInfo = Logger.getLogger("infoLogger");
	final static Logger loggerWarn = Logger.getLogger("warnLogger");
	
	public RegistrationService() {}
	
	public ResponseEntity<Boolean> registerUser(NewUserDto user)
	{	
		if(!isValid(user.password))
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
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
				Role role = roleService.findByName("ROLE_END_ENTITY");
				AppUser appUser = new AppUser(user.name, user.surname, user.email, passwordEncoder.encode(user.password), user.address, user.city, user.country,role, user.twoFA);
				appUserRepository.save(appUser);	
				loggerInfo.info("NUR | UI  "+ appUser.id);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} 
			catch (Exception e) 
			{
				loggerErr.error("FUR ");
				System.out.println(e);
				return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		System.out.println("Korisnik sa ovim mailom postoji ili je nepostojeci mail.");
		loggerErr.error("FETI ");
		return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
	}
	
	public static boolean isValid(String password) {
		Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^(*)~]).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
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
				loggerInfo.info("UVACC | UI  "+ user.id );
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		if(verification != null) {
			userVerificationsRepository.delete(verification);
		}
		loggerErr.error("FVU ");
		return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
	
		}
    
    public Boolean verifyPasswordless(String userCode)
	{	
    	String code = userCode.split("=")[0];
    	System.out.println(code);
		PasswordlessInfo verification = passwordlessInfoRepository.getByVerificationCode(code);
		if(verification != null && verification.timeOfRequest.isBefore(verification.timeOfRequest.plusMinutes(10))) {
			passwordlessInfoRepository.delete(verification);
			return true;
		}
		loggerErr.error("FVU. ");
		return false;
	
		}
    
    public Boolean verifyPasswordChange(String userCode)
	{	
    	System.out.println(userCode);
		PasswordChange verification = passwordChangeRepository.getByVerificationCode(userCode);
		if(verification != null && verification.timeOfRequest.isBefore(verification.timeOfRequest.plusMinutes(10))) {
			return true;
		}
		loggerErr.error("FVU ");
		return false;
	
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
	
	private void sendPasswordlessEmail(String code, String email) {
		String body = "Hello,\nThank you for using on our website. Below is your passwordless login code and it will last for the NEXT 10 MINUTES.\n\n" 
				  + "Your Code is: " + code + 
				    "\n\n If you have any trouble, write to our support : isa.projekat.tester@gmail.com";

		String title = "Passwordless Login Code";
		try 
		{
			Thread t = new Thread() {
				public void run()
				{
					sendEmail(email,body,title);		
				}
			};
			t.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendPasswordChangeEmail(String code, String email) {
		String body = "Hello,\nThank you for using on our website. Below is your passwordless login code and it will last for the NEXT 10 MINUTES.\n\n" 
				  + "Your Code is: " + code + 
				    "\n\n If you have any trouble, write to our support : isa.projekat.tester@gmail.com";

		String title = "Password Change Code";
		try 
		{
			Thread t = new Thread() {
				public void run()
				{
					sendEmail(email,body,title);		
				}
			};
			t.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 public UserTokenState createAuthenticationToken(String email) throws Exception {

	        AppUser user = appUserRepository.getUserByEmail(email);
	        String jwt = tokenUtils.generateToken(user.email);
	        int expiresIn = tokenUtils.getExpiredIn();
	        if (user.isEnabled() == false){
	        	//loggerInfo.info("User id " + user.id +" is not logged in, because he is not verified yet");
	            return null;
	        }
	        loggerInfo.info("LI | UI " + user.id );
	        
	        // Vrati token kao odgovor na uspesnu autentifikaciju
	        return new UserTokenState(jwt, expiresIn,user.role.getName(), user.isEnabled(),user.isMust_change_password(), user.twoFa, user.id);
	    }

	public Boolean checkEmail(String email) {
		String emailFormat = (email.split("%40")[0] + "@" + email.split("%40")[1]).split("=")[0];
		System.out.println(emailFormat);
		for (AppUser au : appUserRepository.findAll()) {
			if (au.email.equalsIgnoreCase(emailFormat)) {
				String verificationCode = generateVerificationCode();
				passwordlessInfoRepository.save(new PasswordlessInfo(au.email, verificationCode, LocalDateTime.now()));
				sendPasswordlessEmail(verificationCode, au.email);
				return true;
			}
		}
		return false;
	}
	
	public Boolean checkEmailPassChange(String email) {
		String emailFormat = (email.split("%40")[0] + "@" + email.split("%40")[1]).split("=")[0];
		System.out.println(emailFormat);
		for (AppUser au : appUserRepository.findAll()) {
			if (au.email.equalsIgnoreCase(emailFormat)) {
				String verificationCode = generateVerificationCode();
				passwordChangeRepository.save(new PasswordChange(au.email, verificationCode, LocalDateTime.now()));
				sendPasswordChangeEmail(verificationCode, au.email);
				return true;
			}
		}
		return false;
	}

	public Boolean changePassword(ChangePasswordDto changePass) {
		for (PasswordChange pc : passwordChangeRepository.findAll()) {
			if (pc.verificationCode.equals(changePass.code)) {
				AppUser au = appUserRepository.findByEmail(pc.email);
				System.out.println(changePass.password);
				au.password = passwordEncoder.encode(au.password);
				System.out.println(au.password);
				appUserRepository.save(au);
				passwordChangeRepository.delete(pc);
				return true;
			}
		}
		return false;
	}
}
