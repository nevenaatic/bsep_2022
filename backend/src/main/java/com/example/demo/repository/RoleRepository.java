package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
    Role findByName(String name);
    Role findById(int id);
    void deleteById(int id);
    
    @Query("select r from Role r join fetch r.permissions where r.name = ?1")
	Role findByNameWithPermissions(String name);
}
