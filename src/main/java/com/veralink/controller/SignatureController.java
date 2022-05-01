package com.veralink.controller;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.veralink.model.SignatureRequest;
import com.veralink.model.SignatureResponse;
import com.veralink.model.VerifyRequest;
import com.veralink.model.VerifyResponse;
import com.veralink.service.KeyService;
import com.veralink.service.SignatureService;
import com.veralink.service.VerifierService;

import COSE.OneKey;
import nl.minvws.encoding.Base45;

@RestController
@RequestMapping("/api/signature")
public class SignatureController {
	
	private KeyService keyService;
	private String filePath;
	
	private ECPublicKey ecPublicKey;
	private ECPrivateKey ecPrivateKey;

	public SignatureController() {
		this.keyService = new KeyService();
		this.filePath = keyService.keyStorePath;
		
		this.ecPublicKey = (ECPublicKey) keyService.getPublicKeyFromStore(filePath);
		this.ecPrivateKey = (ECPrivateKey) keyService.getPrivateKeyFromStore(filePath);
	}
	
	@PostMapping("/sign")
	@ResponseBody
	public ResponseEntity<SignatureResponse> sign(@RequestBody SignatureRequest jsonEntity) {

		OneKey key = null;
	
		byte[] signedMessage = null;
		SignatureResponse response = new SignatureResponse();

		try {
			key = KeyService.generateOneKeyPair(this.ecPublicKey, this.ecPrivateKey);
			signedMessage = SignatureService.signCBORMessage(jsonEntity.payload, key);

			response.status = "OK";
			response.encodedPayload = Base45.getEncoder().encodeToString(signedMessage);
			return ResponseEntity.ok(response);
		} catch(Exception exception) {
			System.out.println(exception);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
	
	@PostMapping("/verify")
	@ResponseBody
	public ResponseEntity<VerifyResponse> verify(@RequestBody VerifyRequest jsonEntity) {

		OneKey key = null;
	
		String encodedPayload = jsonEntity.encodedPayload;
		byte[] decodedPayload = Base45.getDecoder().decode(encodedPayload);
		VerifyResponse response = new VerifyResponse();

		try {
			key = KeyService.generateOneKeyPair(this.ecPublicKey, this.ecPrivateKey);
			boolean isVerified = VerifierService.validateCoseBytes(decodedPayload, key);
			
			response.status = "OK";
			response.isVerified = isVerified;
			return ResponseEntity.ok(response);

		} catch(Exception exception) {
			System.out.print(exception);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
}
