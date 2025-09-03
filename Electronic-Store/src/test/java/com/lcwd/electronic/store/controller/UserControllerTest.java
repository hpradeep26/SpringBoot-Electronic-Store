package com.lcwd.electronic.store.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.UserDTO;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@MockBean
	UserService userService;
	
	private User user;
	
	private Role role;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ModelMapper mapper;
	
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
	public void createUserTest() throws Exception {
		
//      /users +POST+ user data as json
      //data as json+status created
		
		 UserDTO userDTO = mapper.map(user, UserDTO.class);
		 Mockito.when(userService.saveUser(Mockito.any())).thenReturn(userDTO);
		 
		 this.mockMvc.perform(
				post("/api/v1/users")
				 //.header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaHViaGFtIiwiaWF0IjoxNzUzMDkzOTQ2LCJleHAiOjE3NTMzOTM5NDZ9.IXOVagnfnps0SdOjFlecfBk-tbMkUCG8SdWfwtZotXk")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(convertObjectToJsonString(user))
				 .accept(MediaType.APPLICATION_JSON))
		 .andDo(print())
		 .andExpect(status().isCreated())
		 .andExpect(jsonPath("$.username").exists());
		
	}

	private String convertObjectToJsonString(Object user) {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
	
	
	@Test
	public void updateUserTest() throws Exception {
		String userId = "12345";
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		Mockito.when(userService.updateUser(Mockito.any(), Mockito.any())).thenReturn(userDTO);

		this.mockMvc.perform(
				put("/api/v1/users/"+userId)
				.header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaHViaGFtIiwiaWF0IjoxNzUzMDkzOTQ2LCJleHAiOjE3NTMzOTM5NDZ9.IXOVagnfnps0SdOjFlecfBk-tbMkUCG8SdWfwtZotXk")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonString(userDTO))
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.username").exists());
			
	}
}
