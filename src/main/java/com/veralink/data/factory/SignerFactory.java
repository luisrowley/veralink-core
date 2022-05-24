package com.veralink.data.factory;

import com.veralink.data.factory.types.SignerFactoryType;
import com.veralink.model.SignerEntity;


public class SignerFactory implements SignerFactoryType {

	@Override
	public SignerEntity createEntity(String name) {
		return new SignerEntity(name);
	}
}
