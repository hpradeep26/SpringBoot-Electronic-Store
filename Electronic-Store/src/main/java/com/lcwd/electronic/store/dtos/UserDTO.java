package com.lcwd.electronic.store.dtos;

import java.util.Date;
import java.util.List;

import com.lcwd.electronic.store.validation.PhoneNumber;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {
	
	private String userId;
	@Size(min = 3, max = 25,message = "Invalid Name")
	private String username;
	
	@Pattern(regexp = "^[a-zA-Z0-9 [3]. _%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message =  "Invalid Email")
	private String email;
	private Date dateOfBirth;
	@PhoneNumber
	private String phoneNumber;
	private String gender;
	private String imageName;
	private String password;
	private List<RoleDto> roles;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<RoleDto> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleDto> roles) {
		this.roles = roles;
	}
	
	

}
