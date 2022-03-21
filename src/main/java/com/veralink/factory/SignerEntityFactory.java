package com.veralink.factory;

import com.veralink.model.SignerEntity;

/**
 * Interface that provides method for manipulate an object SignerEntity.
 */
public interface SignerEntityFactory {
	
	SignerEntity createEntity(String name);
}
