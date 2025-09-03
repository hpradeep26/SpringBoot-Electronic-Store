package com.lcwd.electronic.store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepositary;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepositary userRepositary;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		List<Role> roles = new ArrayList<>();
		Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
		Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
		if(!adminRole.isPresent()) {
			Role role1 = new Role();
			role1.setName("ROLE_ADMIN");
			roleRepository.save(role1);
			roles.add(role1);
		}
		
		if(!userRole.isPresent()) {
			Role role2 = new Role();
			role2.setName("ROLE_USER");
			roleRepository.save(role2);
			roles.add(role2);
		}
		
		User user = userRepositary.findByusername("test").orElse(null);
        if (user == null) {
        	String dateString = "1990-07-02";
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setUsername("test");
            user.setPassword(passwordEncoder.encode("test123"));
            user.setEmail("test1234@gmail.com");
    		user.setDateOfBirth(formatter.parse(dateString));
    		user.setPhoneNumber("123456789");
    		user.setGender("Male");
            user.setRoles(roles);
            userRepositary.save(user);
            System.out.println("User is created:");

        }
	}

}
