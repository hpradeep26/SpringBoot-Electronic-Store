package com.lcwd.electronic.store.services.impl;

import java.time.Instant;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.RefreshTokenDto;
import com.lcwd.electronic.store.dtos.UserDTO;
import com.lcwd.electronic.store.entities.RefreshToken;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.RefreshTokenRepositary;
import com.lcwd.electronic.store.repositories.UserRepositary;
import com.lcwd.electronic.store.services.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

	private UserRepositary userRepositary;

	private RefreshTokenRepositary refreshTokenRepositary;

	@Autowired
	private ModelMapper modelMapper;


	public RefreshTokenServiceImpl(UserRepositary userRepositary, RefreshTokenRepositary refreshTokenRepositary) {
		this.userRepositary = userRepositary;
		this.refreshTokenRepositary = refreshTokenRepositary;
	}

	@Override
	public RefreshTokenDto createRefreshToken(String userName) {

		User user = userRepositary.findByusername(userName).orElseThrow(() -> new ResourceNotFoundException("User Not Found for"+userName));

		RefreshToken refreshToken = refreshTokenRepositary.findByUser(user).orElse(null);

		if(refreshToken==null) {
			refreshToken = new RefreshToken();
			refreshToken.setToken(UUID.randomUUID().toString());
			refreshToken.setExpiryDate(Instant.now().plusSeconds(5*24*60*60));
			refreshToken.setUser(user);
		}else {
			refreshToken.setToken(UUID.randomUUID().toString());
			refreshToken.setExpiryDate(Instant.now().plusSeconds(5*24*60*60));
		}
		RefreshToken savedRefreshToken = refreshTokenRepositary.save(refreshToken);
		return modelMapper.map(savedRefreshToken, RefreshTokenDto.class);
	}

	@Override
	public RefreshTokenDto findByToken(String token) {
		RefreshToken refreshToken = refreshTokenRepositary.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Refresh Token Not Found"));
		return modelMapper.map(refreshToken, RefreshTokenDto.class);
	}

	@Override
	public RefreshTokenDto verifyRefreshToken(RefreshTokenDto token) {

		var refreshToken = modelMapper.map(token, RefreshToken.class);
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepositary.delete(refreshToken);
			throw new RuntimeException("Refresh Token Expired !!");
		}
		return token;
	}

	@Override
    public UserDTO getUser(RefreshTokenDto dto) {
        RefreshToken refreshToken = refreshTokenRepositary.findByToken(dto.getToken()).orElseThrow(()->  new ResourceNotFoundException("Token not found"));
        User user = refreshToken.getUser();
		return modelMapper.map(user, UserDTO.class);
    }

}
