package com.veralink.service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.veralink.model.User;

@Service
@Transactional
public class UserService {

	private TokenService tokenService = new TokenService();

	public User createNewUserWithCreds(String username, String pass) {
		String secpass = bytesToString(generateSecurePasswordHash(pass));
		String token = tokenService.generateJWTToken(username);
		User user = new User(username, secpass);
		user.setToken(token);
		return user;
	}

	private byte[] generateSecurePasswordHash(String pass) {
		try {
			// get fixed-length salt
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			// compute PBKDF2 key from params
			KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			// return hash from SecretKeyFactory
			byte[] hash = factory.generateSecret(spec).getEncoded();
			return hash;
		} 
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] stringToBytes(String input) {
		return input.getBytes(StandardCharsets.UTF_8);
	}

	private String bytesToString(byte[] input) {
		return new String(input, StandardCharsets.UTF_8);
	}
}
