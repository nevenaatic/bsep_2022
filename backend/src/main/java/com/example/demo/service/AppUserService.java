package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
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
}
