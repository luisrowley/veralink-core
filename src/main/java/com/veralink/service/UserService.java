package com.veralink.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.veralink.data.UserRepository;
import com.veralink.model.User;

@Service
@Transactional
public class UserService {
	
	@Autowired
	UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private TokenService tokenService = new TokenService();

	public User createNewUserWithCreds(String username, String pass) {
		String secpass = bCryptPasswordEncoder.encode(pass);
		User user = new User(username, secpass);

		String token = tokenService.generateJWTToken(user.getId(), username);
		user.setToken(token);
		return user;
	}
	
	public boolean checkUserPassword(User user, String password) {
		return bCryptPasswordEncoder.matches(password, user.getPasswd());
	}
	
	public boolean updateApiTokenForUser(User _user) {
		if (_user.getToken() != null) {
    		String token = tokenService.generateJWTToken(_user.getId(), _user.getName());
    		_user.setToken(token);
    		return true;
		}
		return false;
	}
}
