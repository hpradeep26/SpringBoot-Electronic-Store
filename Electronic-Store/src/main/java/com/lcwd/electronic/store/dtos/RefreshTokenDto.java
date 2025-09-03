package com.lcwd.electronic.store.dtos;

import java.time.Instant;

public class RefreshTokenDto {
	
	private int id;
	private String token;
	private Instant expiryDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Instant getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Instant expirytime) {
		this.expiryDate = expirytime;
	}
	
}
