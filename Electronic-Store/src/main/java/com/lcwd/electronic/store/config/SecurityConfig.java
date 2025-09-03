package com.lcwd.electronic.store.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.lcwd.electronic.store.config.security.jwt.JwtAccessDeniedHandler;
import com.lcwd.electronic.store.config.security.jwt.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.config.security.jwt.JwtAuthenticationRequestFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	/*
		@Bean
		public UserDetailsService userDetailsService() {
			UserDetails userDetails = User.withDefaultPasswordEncoder()
			.username("test")
			.password("test123")
			.roles("ADMIN,GUEST")
			.build();
			
			InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager(userDetails);
			return detailsManager;
		}
	*/
	@Autowired
	JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	JwtAuthenticationRequestFilter authenticationRequestFilter;
	
	@Autowired
	private JwtAccessDeniedHandler accessDeniedHandler;
	
	private final String[] PUBLIC_URLS = { 
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-resources/**",
			"/v3/api-docs"
			
	};
	//SecurityFilterChain Configuration
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
			
		httpSecurity.cors(cors -> cors.disable());
		//httpSecurity.cors(cors -> cors .configurationSource(corsConfigurationSource()));
		httpSecurity.csrf(csrf -> csrf.disable());
		
		// URL Configuration
		httpSecurity
	        .authorizeHttpRequests(request -> 
			{
				//request.requestMatchers("api/v1/products/**").authenticated();
				//request.anyRequest().permitAll();
				
				request.requestMatchers(PUBLIC_URLS).permitAll()
					   .requestMatchers(HttpMethod.POST,"api/v1/users/**").permitAll()
					   .requestMatchers(HttpMethod.GET,"api/v1/users/**").permitAll()
					   .requestMatchers(HttpMethod.DELETE,"api/v1/users/**").hasRole("ADMIN")
					   .requestMatchers(HttpMethod.PUT,"api/v1/users/**").hasAnyRole("ADMIN","USER")
					   .requestMatchers(HttpMethod.GET,"api/v1/products/**").permitAll()
					   .requestMatchers("api/v1/products/**").hasRole("ADMIN")
					   .requestMatchers(HttpMethod.GET,"api/v1/categories/**").permitAll()
					   .requestMatchers("api/v1/categories/**").hasRole("ADMIN")
					   .requestMatchers(HttpMethod.POST,"api/v1/auth/generate-token").permitAll()
					   .requestMatchers("api/v1/auth/**").authenticated()
					   .anyRequest().permitAll();
				
			});
		//Configuring Entry Point
		httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler));
		//Configuring Stateless Session 
		httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		// Confiuring Authentication Filter 
		httpSecurity.addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
		//httpSecurity.httpBasic(Customizer.withDefaults());
		return httpSecurity.build();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(); 
	}
	
	
	@Bean
	public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://yourfrontend.com"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed methods
		configuration.setAllowedHeaders(Arrays.asList("*")); // Allowed headers
		configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // Apply to all paths
		return source;
	}
}
