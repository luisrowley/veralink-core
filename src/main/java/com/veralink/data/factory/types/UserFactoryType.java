package com.veralink.data.factory.types;

import com.veralink.core.enums.BillingPlan;
import com.veralink.model.User;

public interface UserFactoryType {
	User createUser(String name, String email, String password, String billingPlan);
}
