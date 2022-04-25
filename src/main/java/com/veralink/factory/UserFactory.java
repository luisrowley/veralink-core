package com.veralink.factory;

import com.veralink.factory.types.UserFactoryType;
import com.veralink.model.User;

public class UserFactory implements UserFactoryType {

	@Override
	public User createUser(String name, String email, String password, String billingPlan) {
		return new User(name, email, password, billingPlan);
	}
}
