package com.veralink.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.veralink.model.SignerEntity;

public class SignerEntityService {
	
	private List<SignerEntity> signerEntities;
	
	public void createEntityList() {
		if(signerEntities == null) {
			signerEntities = new ArrayList<>();
		}
	}

	public List<SignerEntity> find() {
		createEntityList();
		return signerEntities;
	}
	
	public SignerEntity create(JSONObject jsonEntity) {
		
	}

	public void add(SignerEntity entity) {
		createEntityList();
		signerEntities.add(entity);
	}
}
