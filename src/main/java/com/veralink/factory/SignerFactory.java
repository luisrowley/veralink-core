package com.veralink.factory;

import com.veralink.factory.types.SignerFactoryType;
import com.veralink.model.SignerEntity;


public class SignerFactory implements SignerFactoryType {

	@Override
	public SignerEntity createEntity(String name) {
		return new SignerEntity(name);
	}
}
