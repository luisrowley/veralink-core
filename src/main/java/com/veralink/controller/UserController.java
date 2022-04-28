package com.veralink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
	@ResponseBody
	public ResponseEntity<User> create(@RequestBody User jsonEntity) {
    	User existingUserByName = userRepository.findByName(jsonEntity.getName());
    	User existingUserByEmail = userRepository.findByEmail(jsonEntity.getEmail());

    	if (existingUserByName == null && existingUserByEmail == null) {
			try {
				User newUser = userService.createNewUserWithCreds(jsonEntity);
		        User _user = userRepository.save(newUser);
		        return new ResponseEntity<>(_user, HttpStatus.CREATED);
	
		      } catch (Exception e) {
		        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		      }
    	}
    	return new ResponseEntity<>(null, HttpStatus.ALREADY_REPORTED);
	}

	@PostMapping("signin")
	public ResponseEntity<User> signin(@RequestBody User jsonEntity) {
        User existingUser = userRepository.findByName(jsonEntity.getName());
    
        if (existingUser == null) {
        	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else {
        	if (userService.checkUserPassword(existingUser, jsonEntity.getPassword())) {
        		// UserService update API token
        		if(userService.updateApiTokenForUser(existingUser)) {
        			return new ResponseEntity<>(existingUser, HttpStatus.ACCEPTED);
        		}
        		// If not API token, server error
        		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        	}
        }
	}
}
