package com.lcwd.electronic.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.config.security.jwt.JwtTokenUtil;
import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.RefreshTokenDto;
import com.lcwd.electronic.store.services.CustomerUserDetailService;
import com.lcwd.electronic.store.services.RefreshTokenService;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomerUserDetailService customerUserDetailService;
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/generate-token")
	public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		String username = jwtRequest.getUsername();
		logger.info("username = {} ", username);
		
		doAuthentication(username,jwtRequest.getPassword());
		
		UserDetails userDetails = customerUserDetailService.loadUserByUsername(username);
		
		String token = jwtTokenUtil.generateToken(userDetails);
		
		
		RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(username);
		
		
		return ResponseEntity.ok(new JwtResponse(token,refreshToken));
		
	}

	private void doAuthentication(String username, String password) throws Exception {
		try {
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			logger.debug("isAuthenticated = {}",authenticate.isAuthenticated());
			logger.debug("Principal = {}",authenticate.getPrincipal());
			logger.debug("Authorities = {}",authenticate.getAuthorities());
		} catch (DisabledException e) {
			throw new DisabledException("USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username and Password");
		}

	}
	
}
