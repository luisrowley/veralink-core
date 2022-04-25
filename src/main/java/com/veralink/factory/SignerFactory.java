package com.veralink.factory;

import com.veralink.factory.types.SignerFactoryType;
import com.veralink.model.SignerEntity;


public class SignerFactory implements SignerFactoryType {

	@Override
	public SignerEntity createEntity(String name) {
		return new SignerEntity(name);
		/*switch (billingPlan) {
			case PAY_PER_USE:
				return new SignerEntity(name, email, BillingPlan.PAY_PER_USE);
			case FIXED_MONTHLY:
				return new SignerEntity(name, email, BillingPlan.FIXED_MONTHLY);
			default:
				return new SignerEntity(name, email, BillingPlan.STARTER);
		}*/
	}
}
