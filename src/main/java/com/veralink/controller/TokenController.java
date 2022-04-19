package com.veralink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veralink.model.User;

@RestController
@RequestMapping("/api/token")
public class TokenController {
	
	// TODO: delete this class if not used

	@PostMapping("create")
	public ResponseEntity<User> create(@RequestParam("user") User user) {
		return null;
	}
}
