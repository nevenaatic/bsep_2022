package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.AppUser;
import com.example.demo.model.PermissionRole;
import com.example.demo.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final AppUser user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
        	System.out.println(user.getAuthorities());
            return user;
        }
    }

    private Collection<GrantedAuthority> getAuthorities(AppUser user){
        Set<PermissionRole> userGroups = user.role.getPerimissions();
        Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
        for(PermissionRole userGroup : userGroups){
            authorities.add(new SimpleGrantedAuthority(userGroup.getName()));
        }

        return authorities;
    }
}
