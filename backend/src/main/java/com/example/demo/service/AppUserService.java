package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;

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
    
    public void save(AppUser user) {
    	appUserRepository.save(user);
    }
    
    public AppUser findByVerificationCode(String code) {
    	return appUserRepository.findByVerificationCode(code);
    }
}
