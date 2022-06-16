package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.PermissionRole;


@Repository
public interface PermissionRoleRepository extends JpaRepository<PermissionRole, Integer> {
	PermissionRole findByName(String name);
	PermissionRole findById(int id);
    void deleteById(int id);
    PermissionRole save(PermissionRole permission);
}