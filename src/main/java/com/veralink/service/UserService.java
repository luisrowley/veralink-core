package com.veralink.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.veralink.model.User;

@Service
@Transactional
public class UserService {

	private TokenService tokenService = new TokenService();

	public User createNewUserWithCreds(String username, String pass) {
		String token = tokenService.generateJWTToken(username);
		User user = new User(username, pass);
		user.setToken(token);
		return user;
	} 
}
