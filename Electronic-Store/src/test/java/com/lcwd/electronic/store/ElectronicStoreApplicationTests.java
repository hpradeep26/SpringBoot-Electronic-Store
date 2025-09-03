package com.lcwd.electronic.store;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lcwd.electronic.store.config.security.jwt.JwtTokenUtil;
import com.lcwd.electronic.store.entities.CustomUserDetails;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepositary;

@SpringBootTest
class ElectronicStoreApplicationTests {
	
	Logger logger = LoggerFactory.getLogger(ElectronicStoreApplicationTests.class);
	
	@Autowired
	UserRepositary userRepositary;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void Test() {
		
		User user = userRepositary.findByUsernameContainingIgnoreCase("test");
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		//generate token
		String generateToken = jwtTokenUtil.generateToken(customUserDetails);
		logger.info("generateToken = {} ", generateToken);
		
		Boolean tokenExpired = jwtTokenUtil.isTokenExpired(generateToken);
		logger.info("tokenExpired = {} ", tokenExpired);
		
		String username = jwtTokenUtil.getUsernameFromToken(generateToken);
		logger.info("username = {} ", username);
	}
}
