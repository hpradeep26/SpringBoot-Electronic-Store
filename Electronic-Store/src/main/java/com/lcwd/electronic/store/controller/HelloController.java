package com.lcwd.electronic.store.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HelloController {
	
	@GetMapping
	public String test() {
		return "Welcome to Electronic Store";
	}
}
