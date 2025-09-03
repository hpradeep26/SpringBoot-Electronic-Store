package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.RefreshTokenDto;
import com.lcwd.electronic.store.dtos.UserDTO;

public interface RefreshTokenService {
	
	public RefreshTokenDto createRefreshToken(String userId);
	
	RefreshTokenDto findByToken(String token);
	
	RefreshTokenDto verifyRefreshToken(RefreshTokenDto token);

	UserDTO getUser(RefreshTokenDto dto);
	
}
