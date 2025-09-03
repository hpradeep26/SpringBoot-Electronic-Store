package com.lcwd.electronic.store.config.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler{
	
	Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.debug("request = {} ",request);
		logger.debug("response = {} ",response);
		logger.debug("accessDeniedException = {} ",accessDeniedException);
		response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    response.getOutputStream().println("{ \"error\": \"" + accessDeniedException.getMessage() + "\" }");
	}

}
