package com.lcwd.electronic.store.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDTO;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepositary;
import com.lcwd.electronic.store.util.Helper;

@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	private UserRepositary userRepositary;
	
	@MockBean
	private RoleRepository roleRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	ModelMapper mapper;
	
	 private User user;
	 
	 private Role role;
	 
	 @BeforeEach
	 public void setup() {
		 user = new User();
		 user.setUsername("pradeep");
		 user.setEmail("pradeep.huded@gmail.com");
		 user.setGender("Male");
		 user.setPassword("password123");
		 user.setDateOfBirth(new Date());
		 user.setImageName("abc.png");
		 user.setPhoneNumber("123456789");
		 
		 role = new Role();
		 role.setRoleId("1234");
		 role.setName("ROLE_USER");
	 }
	
	 @Test
	 public void testCreateUser() {
		 
		 UserDTO userDTO = new UserDTO();
		 userDTO.setUsername("pradeep");
		 userDTO.setEmail("pradeep.example@com");
		 userDTO.setGender("Male");
		 userDTO.setPassword("password123");
		 userDTO.setDateOfBirth(new Date());
		 userDTO.setImageName("abc.png");
		 userDTO.setPhoneNumber("123456789");
		 Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
		 Mockito.when(userRepositary.save(Mockito.any())).thenReturn(user);
		 UserDTO savedUser = userService.saveUser(userDTO);
		 System.out.println(savedUser.getUsername());
		 System.out.println(savedUser.getEmail());
		 Assertions.assertNotNull(savedUser);
		 Assertions.assertEquals(savedUser.getUsername(), user.getUsername());
		 Assertions.assertEquals(savedUser.getEmail(), user.getEmail());
	 }
	 
	 @Test
	 public void updateUserTest() {
		 UserDTO userDTO = new UserDTO();
		 userDTO.setUsername("Durgesh Kumar Tiwari");
		 userDTO.setGender("Male");
		 userDTO.setImageName("xyz.png");
		 Mockito.when(userRepositary.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		 Mockito.when(userRepositary.save(Mockito.any())).thenReturn(user);
		UserDTO updatedUser = userService.updateUser(userDTO, "12345");
		 //UserDTO updatedUser=mapper.map(user,UserDTO.class);
		 System.out.println(updatedUser.getUsername());
		 System.out.println(updatedUser.getImageName());
		 Assertions.assertNotNull(updatedUser);
	    Assertions.assertEquals(userDTO.getUsername(), updatedUser.getUsername(), "Name is not validated !!");
		 
	 }
	 
	 @Test
	 public void deleteUserTest() {
		 String userId = "123456";
		 Mockito.when(userRepositary.findById(userId)).thenReturn(Optional.of(user));
		 userService.deleteUser(userId);
		 Mockito.verify(userRepositary, Mockito.times(1)).save(user);
	 }
	 
	 @Test
	 public void findAllTest() {
		 Page<User> page = new PageImpl<>(List.of(user));
		 Mockito.when(userRepositary.findAll(Mockito.any(Pageable.class))).thenReturn(page);
		 PageableResponse<UserDTO> pageableResponse = userService.findAll(0, 10, "username", "asc");
		 assertEquals(1, pageableResponse.getContent().size());
	 }
	 
	 @Test
	 public void findByIdTest() {
		 String userId = "123456";
		 Mockito.when(userRepositary.findById(userId)).thenReturn(Optional.of(user));
		 UserDTO userDTO = userService.findById(userId);
		 Assertions.assertNotNull(userDTO);
		 assertEquals(user.getUsername(), userDTO.getUsername());
	 }
	 
	 @Test
	 public void findByUsernameTest() {
		 String name = "pradeep";
		 Mockito.when(userRepositary.findByUsernameContainingIgnoreCase(name)).thenReturn(user);
		 UserDTO userDTO = userService.findByUsername(name);
		 Assertions.assertNotNull(userDTO);
		 assertEquals(user.getUsername(), userDTO.getUsername());
	 }
	 
	 

	 
}
