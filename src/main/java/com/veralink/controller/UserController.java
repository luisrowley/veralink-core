package com.veralink.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veralink.model.User;
import com.veralink.service.TokenService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private TokenService tokenService = new TokenService();

	@PostMapping("create")
	public User signup(@RequestParam("user") String username, @RequestParam("password") String pwd) {
		
		// TODO: authenticate user against database creds
		String token = tokenService.generateJWTToken(username);
		User user = new User();
		user.setName(username);
		user.setToken(token);	
		return user;
	}
}
