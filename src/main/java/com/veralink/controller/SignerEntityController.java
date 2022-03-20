package com.veralink.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veralink.service.SignerEntityService;
import com.veralink.model.SignerEntity;


@RestController
@RequestMapping("/api/V1/signer")
public class SignerEntityController {

	private static final Logger logger = LogManager.getLogger(SignerEntityController.class);

	@Autowired
	private SignerEntityService signerEntityService;

	@GetMapping
	public ResponseEntity<List<SignerEntity>> find() {
		if(signerEntityService.find().isEmpty()) {
			return ResponseEntity.notFound().build(); 
		}
		logger.info(signerEntityService.find());
		return ResponseEntity.ok(signerEntityService.find());
	}
}
