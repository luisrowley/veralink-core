package com.veralink.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.veralink.model.User;

@Service
@Transactional
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private TokenService tokenService = new TokenService();

	public User createNewUserWithCreds(String username, String pass) {
		String secpass = bCryptPasswordEncoder.encode(pass);
		String token = tokenService.generateJWTToken(username);
		User user = new User(username, secpass);
		user.setToken(token);
		return user;
	}
	
	public boolean checkUserPassword(User user, String password) {
		return bCryptPasswordEncoder.matches(password, user.getPasswd());
	}
}
