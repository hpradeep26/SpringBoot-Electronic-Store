package com.lcwd.electronic.store.services.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDTO;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepositary;
import com.lcwd.electronic.store.services.UserService;
import com.lcwd.electronic.store.util.FileUtil;
import com.lcwd.electronic.store.util.Helper;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepositary userRepositary;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class); 

	@Override
	public UserDTO saveUser(UserDTO userDto) {
		String userId = UUID.randomUUID().toString();
		User user = new User();
		user.setUserId(userId);
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setGender(userDto.getGender());
		user.setImageName(userDto.getImageName());
		Role role = roleRepository.findByName("ROLE_USER").orElse(null);
		if(role==null) {
			role = new Role();
			role.setName("ROLE_USER");
			roleRepository.save(role);
		}
		user.setRoles(List.of(role));
		User savedUser = userRepositary.save(user);
		UserDTO userDto2 = modelMapper.map(savedUser, UserDTO.class);
		return userDto2;
	}

	@Override
	public UserDTO updateUser(UserDTO userDto, String userId) {
		User user = userRepositary.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found for userId "+userId));
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setImageName(userDto.getImageName());
		User savedUser = userRepositary.save(user);
		UserDTO userDto2 = modelMapper.map(savedUser, UserDTO.class);
		return userDto2;
	}

	@Override
	public UserDTO findById(String userId) {
		User user = userRepositary.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found for userId "+userId));
		return modelMapper.map(user, UserDTO.class);
	}

	@Override
	public PageableResponse<UserDTO> findAll(int pageNo, int pageSize, String sortBy,String sortDir) {
		Sort by = Sort.by(sortBy);
		Sort sort = sortDir.equalsIgnoreCase("desc") ? by.descending() : by.ascending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<User> pageUsers = userRepositary.findAll(pageable);
		PageableResponse<UserDTO> pageableResponse = Helper.getPageableResponse(pageUsers, UserDTO.class);
		return pageableResponse;
	}
	
	@Transactional
	@Override
	public void deleteUser(String userId) {
		User user = userRepositary.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found for userId "+userId));
		 // Step 1: Clear role relationships
        user.getRoles().clear();
        userRepositary.save(user);  
		if(user.getImageName()!=null) {
			String imageFilePath = imagePath + File.separator +user.getImageName();
			logger.info("imagePath = {} ",imageFilePath);
			FileUtil.deletefile(imageFilePath);
		}
		userRepositary.deleteById(userId);
	}

	@Override
	public UserDTO findByUsername(String userName) {
		User user = userRepositary.findByUsernameContainingIgnoreCase(userName);
		if(user==null) {
			return null;
		}
		return modelMapper.map(user, UserDTO.class);
	}
	
	@Override
	public void addRoletoUser(String userId,String roleName) {
		Role role = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role Not Found "+roleName));
		User user = userRepositary.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found for userId "+userId));
		user.getRoles().add(role);
		userRepositary.save(user);
	}

}
