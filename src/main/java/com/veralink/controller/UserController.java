package com.veralink.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veralink.model.User;
import com.veralink.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserService userService = new UserService();

	@PostMapping("create")
	public User signup(@RequestParam("user") String username, @RequestParam("password") String pass) {
		
		// TODO: authenticate user against database creds
		User newUser = userService.createNewUserWithCreds(username, pass);
		return newUser;
	}
}
