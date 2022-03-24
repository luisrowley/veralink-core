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
	public String name;
	public Date creationDate;
	public String billingPlan;
	private TokenService tokenService = new TokenService();

	public SignerEntity(String name, BillingPlan billingPlan) {
		this.name = name;
		this.billingPlan = billingPlan.toString();
		this._UUID = tokenService.generateUUID();
		this.creationDate = new Date();
	}

	/*public String getApiToken() {
		return _apiToken;
	}*/
	
	public String getUUID() {
		return this._UUID;
	}
}
