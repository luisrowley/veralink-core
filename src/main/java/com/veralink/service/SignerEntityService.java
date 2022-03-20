package com.veralink.service;

import java.util.ArrayList;
import java.util.List;
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

	public void add(SignerEntity entity) {
		createEntityList();
		signerEntities.add(entity);
	}
}
