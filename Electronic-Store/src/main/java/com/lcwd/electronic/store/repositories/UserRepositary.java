package com.lcwd.electronic.store.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.entities.User;

@Repository
public interface UserRepositary extends JpaRepository<User, String>{
	
	User findByUsernameContainingIgnoreCase(String name);
	
	Optional<User> findByusername(String name);

}
