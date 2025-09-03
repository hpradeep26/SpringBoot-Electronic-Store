package com.lcwd.electronic.store.dtos;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	
	private static final long serialVersionUID = -2926672251915451940L;
	
	private String token;
	
	private RefreshTokenDto refreshTokenDto;

	public JwtResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public JwtResponse(String token) {
		this.token = token;
	}

	public JwtResponse(String token, RefreshTokenDto refreshToken) {
		this.token = token;
		this.refreshTokenDto = refreshToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public RefreshTokenDto getRefreshTokenDto() {
		return refreshTokenDto;
	}

	public void setRefreshTokenDto(RefreshTokenDto refreshTokenDto) {
		this.refreshTokenDto = refreshTokenDto;
	}
	
	

}
