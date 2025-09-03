package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDTO;

public interface UserService {
	
	UserDTO saveUser(UserDTO userDto);
	
	UserDTO updateUser(UserDTO userDto,String userId);
	
	UserDTO findById(String userId);
	
	PageableResponse<UserDTO> findAll(int pageNo,int pageSize,String sortBy,String sortDir);
	
	void deleteUser(String userId);
	
	UserDTO findByUsername(String userName);
	
	void addRoletoUser(String userId,String roleName);
	

}
