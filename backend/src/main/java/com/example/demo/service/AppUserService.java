package com.example.demo.service;

import com.example.demo.dto.UserFrontDto;
import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private AppUserRepository appUserRepository;

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
    	appUserRepository.save(user);
    }
    
    public AppUser findByVerificationCode(String code) {
    	return appUserRepository.findByVerificationCode(code);
    }
    
    public List<UserFrontDto> getUsersDto() {
    	List <UserFrontDto> usersRet = new ArrayList<UserFrontDto>();
    	for (AppUser au : appUserRepository.findAll()) { 
    		usersRet.add(new UserFrontDto(au.id, au.name, au.surname, au.email, au.address, au.city, au.country));
    	}
    	return usersRet;
    }
}
