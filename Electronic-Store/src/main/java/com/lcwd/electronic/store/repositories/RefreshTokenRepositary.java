package com.lcwd.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.RefreshToken;
import com.lcwd.electronic.store.entities.User;

public interface RefreshTokenRepositary extends JpaRepository<RefreshToken, Integer>{
	
	Optional<RefreshToken> findByUser(User user);
	
	Optional<RefreshToken> findByToken(String token);
	
	
}
