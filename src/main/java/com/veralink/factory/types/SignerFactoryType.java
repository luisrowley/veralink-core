package com.veralink.factory.types;

import com.veralink.model.SignerEntity;

/**
 * Interface that provides method for manipulate an object SignerEntity.
 */
public interface SignerFactoryType {
	
	SignerEntity createEntity(String name);
}
