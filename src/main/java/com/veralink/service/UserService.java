package com.veralink.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.veralink.data.UserRepository;
import com.veralink.factory.UserFactory;
import com.veralink.model.User;

@Service
@Transactional
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	private UserFactory factory;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private final TokenService tokenService = new TokenService();
	
	public void createFactory() {
		if (factory == null) {
			factory = new UserFactory();
		}
	}

	public User createNewUserWithCreds(User jsonEntity) {
		createFactory();
		String secpass = bCryptPasswordEncoder.encode(jsonEntity.getPassword());
		User user = factory.createUser(
				jsonEntity.getName(),
				jsonEntity.getEmail(),
				secpass,
				jsonEntity.getBillingPlan());

		String token = tokenService.generateJWTToken(user.getId(), user.getName());
		user.setToken(token);
		return user;
	}
	
	public boolean checkUserPassword(User user, String password) {
		return bCryptPasswordEncoder.matches(password, user.getPassword());
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
