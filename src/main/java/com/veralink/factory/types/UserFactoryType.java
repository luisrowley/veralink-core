package com.veralink.factory.types;

import com.veralink.model.User;

public interface UserFactoryType {
	User createUser(String name, String email, String password, String billingPlan);
}
