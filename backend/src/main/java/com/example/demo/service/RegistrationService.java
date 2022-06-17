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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.NewUserDto;
import com.example.demo.model.AppUser;
import com.example.demo.model.PermissionRole;
import com.example.demo.model.Role;
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
	@Autowired
	private RoleService roleService; 
	@Autowired
	private PermissionRoleService permissionRoleService; 
	@Autowired
	private PasswordEncoder passwordEncoder;
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
				//Role role = roleService.findByName("ROLE_END_ENTITY");
				Role role = roleService.findByName("ROLE_CA");
					if (role == null) {
						role = new Role("ROLE_CA");
						roleService.save(role);
						PermissionRole permCertDownload = new PermissionRole("PERM_CERT_DOWNLOAD");
						permCertDownload.setRole(role);
						permissionRoleService.save(permCertDownload);
						PermissionRole permCertCheckValidity = new PermissionRole("PERM_CERT_CHECK_VALIDITY");
						permCertCheckValidity.setRole(role);
						permissionRoleService.save(permCertCheckValidity);	
						PermissionRole permCertCheckRevoke = new PermissionRole("PERM_CERT_REVOKE");
						permCertCheckRevoke.setRole(role);
						permissionRoleService.save(permCertCheckRevoke);
						PermissionRole permGetNonAdmins = new PermissionRole("PERM_GET_NON_ADMINS");
						permGetNonAdmins.setRole(role);
						permissionRoleService.save(permGetNonAdmins);
						PermissionRole permCertIssue = new PermissionRole("PERM_CERT_ISSUE ");
						permCertIssue.setRole(role);
						permissionRoleService.save(permCertIssue);
//						role = new Role("ROLE_END_ENTITY");
//						roleService.save(role);
//						PermissionRole permCertDownload = new PermissionRole("PERM_CERT_DOWNLOAD");
//						permCertDownload.setRole(role);
//						permissionRoleService.save(permCertDownload);
//						PermissionRole permCertCheckValidity = new PermissionRole("PERM_CERT_CHECK_VALIDITY");
//						permCertCheckValidity.setRole(role);
//						permissionRoleService.save(permCertCheckValidity);	
					}
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
