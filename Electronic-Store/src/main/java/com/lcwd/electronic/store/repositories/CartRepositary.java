package com.lcwd.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.User;

public interface CartRepositary extends JpaRepository<Cart, Integer>{
	
	@Query("select c from Cart c where c.user.userId =?1")
	Cart findByUserId(String userId);
	
	Optional<Cart> findByUser(User user);
}
