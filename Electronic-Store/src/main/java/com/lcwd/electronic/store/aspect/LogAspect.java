package com.lcwd.electronic.store.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
	
 

	@Pointcut("within(com.lcwd.electronic.store.services..*)")
	public void inServiceLayer() {
		System.out.println("Aspects log");
	}
	
	@Before("target(com.lcwd.electronic.store.services.UserService)")
	public void targetUserServiceBefore() {
		System.out.println("Aspect log before Service");
	}
	
	@After("target(com.lcwd.electronic.store.services.UserService)")
	public void targetUserServiceAfter() {
		System.out.println("Aspect log after Service");
	}
}
