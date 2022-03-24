package com.veralink.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.veralink.core.enums.BillingPlan;
import com.veralink.factory.SignerFactory;
import com.veralink.model.SignerEntity;

@Service
public class SignerEntityService {
	
	private SignerFactory factory;
	private List<SignerEntity> signerEntities;
	
	public void createFactory() {
		if(factory == null) {
			factory = new SignerFactory();
		}
	}

	public void createEntityList() {
		if(signerEntities == null) {
			signerEntities = new ArrayList<>();
		}
	}

	public List<SignerEntity> find() {
		createEntityList();
		return signerEntities;
	}

	public SignerEntity create(SignerEntity jsonEntity) {
		createFactory();
		var billingPlan = BillingPlan.valueOf(jsonEntity.billingPlan);
		SignerEntity entity = factory.createEntity(jsonEntity.name, billingPlan);
		return entity;
	}

	public void add(SignerEntity entity) {
		createEntityList();
		signerEntities.add(entity);
	}
}
