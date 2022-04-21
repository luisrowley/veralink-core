package com.veralink.factory;

import com.veralink.core.enums.BillingPlan;
import com.veralink.model.SignerEntity;
import com.veralink.model.User;

/**
 * Interface that provides method for manipulate an object SignerEntity.
 */
public interface SignerFactoryType {
	
	SignerEntity createEntity(String name, String email, BillingPlan billingPlan);
}
