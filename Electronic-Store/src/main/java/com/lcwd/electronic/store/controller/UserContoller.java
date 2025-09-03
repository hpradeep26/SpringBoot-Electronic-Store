package com.lcwd.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDTO;
import com.lcwd.electronic.store.services.FileUploadService;
import com.lcwd.electronic.store.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "User", description = "User API")
public class UserContoller {
	
	Logger logger = LoggerFactory.getLogger(UserContoller.class); 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	@Operation(summary = "Save User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Created"),
			@ApiResponse(responseCode = "404", description = "Resource Not found"),
			@ApiResponse(responseCode = "403", description = "Forbidden")
	})
	@PostMapping
	public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO) {
		UserDTO saveUser = userService.saveUser(userDTO);
		return new ResponseEntity<UserDTO>(saveUser, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Get User By User Id")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
		UserDTO userDTO = userService.findById(userId);
		return ResponseEntity.ok(userDTO);
	}
	
	@Operation(summary = "Get All users")
	@GetMapping
	public ResponseEntity<PageableResponse<UserDTO>> getAllUsers(
			@RequestParam(value ="pageSize",  required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "pageNumber",required = false,defaultValue = "0") Integer pageNumber,
			@RequestParam(value = "sortBy", required = false, defaultValue = "username") String sortBy,
			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir){
		PageableResponse<UserDTO> pageableResponse = userService.findAll(pageNumber,pageSize,sortBy,sortDir);
		return ResponseEntity.ok(pageableResponse);
	}
	
	@PutMapping("/{userId}")
	@Operation(summary = "Update User")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable String userId) {
		UserDTO user = userService.updateUser(userDTO, userId);
		return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
	}
	
	@Operation(summary = "Search By Username")
	@GetMapping("/search")
	public ResponseEntity<UserDTO> serachUserByName(@RequestParam String name) {
		UserDTO userDTO = userService.findByUsername(name);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Delete User By Providing User Id")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setMessage("User successfully deleted!");
		message.setHttpStatus(HttpStatus.OK);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@PostMapping("/image/{userId}")
	@Operation(summary = "Upload Profile image for User")
	public ResponseEntity<ApiResponseMessage> uploadImage(@PathVariable String userId, 
			@RequestParam("image") MultipartFile image) throws IOException{
		logger.debug("image Name = {}, image Type= {}",image.getOriginalFilename(), image.getContentType());
		logger.debug("image Path = {}",imagePath);
		
		UserDTO userDTO = userService.findById(userId);
		String imageName = fileUploadService.uploadImage(image, imagePath);
		userDTO.setImageName(imageName);
		userService.updateUser(userDTO, userId);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setMessage("User image uploaded successful");
		message.setHttpStatus(HttpStatus.CREATED);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Get Image")
	@GetMapping("/image/{userId}")
	public void serverImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
		UserDTO userDTO = userService.findById(userId);
		logger.info("image name = {}",userDTO.getImageName());
		InputStream inputStream = fileUploadService.getResource(imagePath, userDTO.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(inputStream,response.getOutputStream());
	}
	@Operation(summary = "Add Role")
	@PatchMapping("/roles/{userId}")
	public ResponseEntity<ApiResponseMessage> addRolestoUser(@PathVariable String userId, @RequestParam("roleName") String roleName) {
		userService.addRoletoUser(userId, roleName);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setMessage("Role updated successful");
		message.setHttpStatus(HttpStatus.OK);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	

}
