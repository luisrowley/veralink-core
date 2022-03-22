package com.veralink.factory;

import com.veralink.core.enums.BillingPlan;
import com.veralink.model.SignerEntity;

public class SignerFactory implements SignerFactoryType {

	@Override
	public SignerEntity createEntity(String billingPlan) {
		/*switch (billingPlan) {
		case STARTER:*/
		if(BillingPlan.PAY_PER_USE.toString().equals(billingPlan)) {
			return new SignerEntity(BillingPlan.PAY_PER_USE);
		}
		else if(BillingPlan.FIXED_MONTHLY.toString().equals(billingPlan)) {
			return new SignerEntity(BillingPlan.PAY_PER_USE);
		}	
		return new SignerEntity(BillingPlan.STARTER);
	}
}
