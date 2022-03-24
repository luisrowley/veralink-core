package com.veralink.factory;

import com.veralink.core.enums.BillingPlan;
import com.veralink.model.SignerEntity;

public class SignerFactory implements SignerFactoryType {

	@Override
	public SignerEntity createEntity(String name, BillingPlan billingPlan) {
		switch (billingPlan) {
			case PAY_PER_USE:
				return new SignerEntity(name, BillingPlan.PAY_PER_USE);
			case FIXED_MONTHLY:
				return new SignerEntity(name, BillingPlan.FIXED_MONTHLY);
			default:
				return new SignerEntity(name, BillingPlan.STARTER);
		}
	}
}
