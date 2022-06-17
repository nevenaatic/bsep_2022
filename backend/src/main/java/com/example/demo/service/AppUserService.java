package com.example.demo.service;

import com.example.demo.dto.UserFrontDto;
import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private AppUserRepository appUserRepository;
	final static Logger loggerErr = Logger.getLogger("errorLogger"); 
	final static Logger loggerInfo = Logger.getLogger("infoLogger");
	final static Logger loggerWarn = Logger.getLogger("warnLogger");

    public AppUserService(AppUserRepository appUserRepository){
    	
        this.appUserRepository = appUserRepository;

    }
    
    public AppUser findByEmail(String email) {
    	return appUserRepository.findByEmail(email);
    }
    
    public AppUser findById(long id) {
    	return appUserRepository.findById(id).get();
    }
    
    public List <AppUser> findAll() {
    	return appUserRepository.findAll();
    }
    
    public void saveAll(List<AppUser> users) {
    	appUserRepository.saveAll(users);
    }
    
    public void save(AppUser user) {
    	loggerWarn.warn(" SNUID | UI " + this.loggedUser().id);
    	appUserRepository.save(user);
    }
    
    public AppUser findByVerificationCode(String code) {
    	loggerWarn.warn(" FUBVC | UI " + this.loggedUser().id);
    	return appUserRepository.findByVerificationCode(code);
    }
    
    public List<UserFrontDto> getUsersDto() {
    	List <UserFrontDto> usersRet = new ArrayList<UserFrontDto>();
    	for (AppUser au : appUserRepository.findAll()) { 
    		usersRet.add(new UserFrontDto(au.id, au.name, au.surname, au.email, au.address, au.city, au.country));
    	}
    	return usersRet;
    }
    
	public AppUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (AppUser)authentication.getPrincipal();
	}
}
