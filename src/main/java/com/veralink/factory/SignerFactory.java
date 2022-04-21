package com.veralink.factory;

import com.veralink.core.enums.BillingPlan;
import com.veralink.model.SignerEntity;
import com.veralink.model.User;

public class SignerFactory implements SignerFactoryType {

	@Override
	public SignerEntity createEntity(String name, String email, BillingPlan billingPlan) {
		switch (billingPlan) {
			case PAY_PER_USE:
				return new SignerEntity(name, email, BillingPlan.PAY_PER_USE);
			case FIXED_MONTHLY:
				return new SignerEntity(name, email, BillingPlan.FIXED_MONTHLY);
			default:
				return new SignerEntity(name, email, BillingPlan.STARTER);
		}
	}
}
