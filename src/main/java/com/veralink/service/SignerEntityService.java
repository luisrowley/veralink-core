package com.veralink.service;

import org.springframework.stereotype.Service;

import com.veralink.factory.SignerFactory;
import com.veralink.model.SignerEntity;

@Service
public class SignerEntityService {
	
	private SignerFactory factory;
	
	public void createFactory() {
		if (factory == null) {
			factory = new SignerFactory();
		}
	}

	public SignerEntity create(SignerEntity jsonEntity) {
		createFactory();
		SignerEntity entity = factory.createEntity(
				jsonEntity.getName()
		);
		return entity;
	}
}
