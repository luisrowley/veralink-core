package com.veralink.factory;

import com.veralink.core.enums.BillingPlan;
import com.veralink.factory.types.UserFactoryType;
import com.veralink.model.User;

public class UserFactory implements UserFactoryType {
	
	private final String PAY_PER_USE = "PAY_PER_USE";
	private final String FIXED_MONTHLY = "FIXED_MONTHLY";

	@Override
	public User createUser(String name, String email, String password, String billingPlan) {
		switch (billingPlan) {
		case PAY_PER_USE:
			return new User(name, email, password, BillingPlan.PAY_PER_USE.toString());
		case FIXED_MONTHLY:
			return new User(name, email, password, BillingPlan.FIXED_MONTHLY.toString());
		default:
			return new User(name, email, password, BillingPlan.STARTER.toString());
		}
	}
}
