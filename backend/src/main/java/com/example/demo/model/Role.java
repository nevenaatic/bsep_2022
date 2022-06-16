package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="ROLE")
public class Role {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name")
    String name;

    public Role(String role) {
        this.name=role;
    }
    
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<PermissionRole> permissions;
   

    public Role(Role role) {
        this.name = role.getName();
        this.id = role.getId();
    }

    public Role() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Set<PermissionRole> getPerimissions() {
		return permissions;
	}

	public void setPerimissions(Set<PermissionRole> perimissions) {
		this.permissions = perimissions;
	}
    
    
}

