package com.veralink.model;

import java.util.Date;
import com.veralink.core.enums.BillingPlan;
import com.veralink.service.TokenService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignerEntity {

	private String _UUID;
	private String name;
	private String email;
	public Date creationDate;
	private BillingPlan billingPlan;
	private TokenService tokenService = new TokenService();

	public SignerEntity(String name, String email, BillingPlan billingPlan) {
		this.setName(name);
		this.setEmail(email);
		this.setBillingPlan(billingPlan);
		this._UUID = tokenService.generateUUID();
		this.creationDate = new Date();
	}

	/*public String getApiToken() {
		return _apiToken;
	}*/
	
	public String getUUID() {
		return this._UUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BillingPlan getBillingPlan() {
		return billingPlan;
	}

	public void setBillingPlan(BillingPlan billingPlan) {
		this.billingPlan = billingPlan;
	}
}
