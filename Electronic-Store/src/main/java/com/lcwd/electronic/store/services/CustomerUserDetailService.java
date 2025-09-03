package com.lcwd.electronic.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.entities.CustomUserDetails;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepositary;

@Service
public class CustomerUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepositary userRepositary;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepositary.findByusername(username).orElseThrow(() -> new UsernameNotFoundException("User not Found Exception"));
		return new CustomUserDetails(user);
	}

	
}
