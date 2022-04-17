package com.veralink.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veralink.data.UserRepository;
import com.veralink.model.User;
import com.veralink.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	private UserService userService = new UserService();

	@PostMapping("create")
	public ResponseEntity<User> signUp(@RequestParam("user") String username, @RequestParam("password") String pass) {
	    try {
			User newUser = userService.createNewUserWithCreds(username, pass);
	        User _user = userRepository.save(newUser);
	        return new ResponseEntity<>(_user, HttpStatus.CREATED);
	      } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	}

	// TODO: authenticate user against database creds
	public ResponseEntity<User> signIn(@RequestParam("user") String username, @RequestParam("password") String pass) {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
