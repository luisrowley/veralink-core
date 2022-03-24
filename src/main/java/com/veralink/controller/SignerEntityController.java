package com.veralink.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.veralink.service.SignerEntityService;
import com.veralink.model.SignerEntity;


@RestController
@RequestMapping("/api/entities")
public class SignerEntityController {

	private static final Logger logger = LogManager.getLogger(SignerEntityController.class);

	@Autowired
	private SignerEntityService signerEntityService;

	@GetMapping("/list")
	public ResponseEntity<List<SignerEntity>> find() {
		if(signerEntityService.find().isEmpty()) {
			return ResponseEntity.notFound().build(); 
		}
		logger.info(signerEntityService.find());
		return ResponseEntity.ok(signerEntityService.find());
	}

	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<SignerEntity> create(@RequestBody SignerEntity jsonEntity) {
		try {
			if(jsonEntity != null) {
				SignerEntity newEntity = signerEntityService.create(jsonEntity);
				signerEntityService.add(newEntity);
				var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(newEntity.getUUID()).build().toUri();
				return ResponseEntity.created(uri).body(null);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch(Exception jsonException) {
			logger.error("JSON fields are not parsable. " + jsonException);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
}
