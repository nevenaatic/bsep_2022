package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.PermissionRole;
import com.example.demo.model.Role;
import com.example.demo.repository.PermissionRoleRepository;
import com.example.demo.repository.RoleRepository;

@Service
public class PermissionRoleService {
	
	    private PermissionRoleRepository roleRepository;

	    public PermissionRoleService(PermissionRoleRepository roleRepository) {
	        this.roleRepository=roleRepository;
	    }

	    public PermissionRole save(PermissionRole role) { 
	    	return this.roleRepository.save(role);}

	    public void saveRole(PermissionRole role) {  
	    	this.roleRepository.save(role);}

	    public void deleteById(int id) {
	        this.roleRepository.deleteById(id);
	    }

	    public void delete(PermissionRole role) {
	        this.roleRepository.delete(role);
	    }

	    public PermissionRole findById(int id){ 
	    	return this.roleRepository.findById(id);
	    }

	    public PermissionRole findByName(String name){ 
	    	return this.roleRepository.findByName(name);
	   }

}
