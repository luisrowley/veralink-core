package com.veralink.controller;

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
	public ResponseEntity<User> create(@RequestParam("user") String username, @RequestParam("password") String pass) {
    	User existingUser = userRepository.findByName(username);

    	if (existingUser == null) {
			try {
				User newUser = userService.createNewUserWithCreds(username, pass);
		        User _user = userRepository.save(newUser);
		        return new ResponseEntity<>(_user, HttpStatus.CREATED);
	
		      } catch (Exception e) {
		        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		      }
    	}
    	return new ResponseEntity<>(null, HttpStatus.ALREADY_REPORTED);
	}

	@PostMapping("signin")
	public ResponseEntity<User> signin(@RequestParam("user") String username, @RequestParam("password") String pass) {
        User existingUser = userRepository.findByName(username);
    
        if (existingUser == null) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
        	if (userService.checkUserPassword(existingUser, pass)) {
        		return new ResponseEntity<>(existingUser, HttpStatus.ACCEPTED);
        	}
        	else {
        		return new ResponseEntity<>(existingUser, HttpStatus.UNAUTHORIZED);
        	}
        }
	}
}
