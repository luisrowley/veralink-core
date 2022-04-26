package com.veralink.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.veralink.service.SignerEntityService;
import com.veralink.data.SignerEntityRepository;
import com.veralink.data.UserRepository;
import com.veralink.model.SignerEntity;
import com.veralink.model.User;


@RestController
@RequestMapping("/api/entities")
public class SignerEntityController {

	private static final Logger logger = LogManager.getLogger(SignerEntityController.class);

	@Autowired
	private SignerEntityService signerEntityService;
	
	@Autowired
	private SignerEntityRepository entityRepository;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/find")
	@ResponseBody
	public ResponseEntity<SignerEntity> find(@RequestParam Long id) {
		Optional<SignerEntity> entity = entityRepository.findById(id);
		User userDetails = getCurrentSignedUser();
	
		if(entity.isEmpty()) {
			logger.info(entityRepository.findById(id));
			return ResponseEntity.notFound().build();
		}
		if(userDetails.getId() != entity.get().getUser().getId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		else {
			return ResponseEntity.ok(entity.get());
		}
	}

	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<SignerEntity> create(@RequestBody SignerEntity jsonEntity) {
		try {
			if(jsonEntity != null) {
				User userDetails = getCurrentSignedUser();
				SignerEntity newEntity = signerEntityService.create(jsonEntity);
				// set entity with user details
				newEntity.setCreatedBy(userDetails.getName());
				newEntity.setUser(userDetails);
				// persist to DB
				entityRepository.save(newEntity);
				var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(newEntity.getUUID()).build().toUri();
				return ResponseEntity.created(uri).body(null);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch(Exception nullException) {
			logger.error("JSON fields are null. " + nullException);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
	
	private User getCurrentSignedUser() {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByName(currentUserName);
	}
}
