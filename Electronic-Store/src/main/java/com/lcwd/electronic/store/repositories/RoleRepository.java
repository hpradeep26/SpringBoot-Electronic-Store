package com.lcwd.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
	
	Optional<Role> findByName(String name);
	
	
}
