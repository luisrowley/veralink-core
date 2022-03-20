package com.veralink.model;

import java.util.Date;
import java.util.UUID;
import com.veralink.core.enums.BillingPlan;
import com.veralink.service.TokenService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignerEntity {

	private String _UUID;
	public String companyName;
	public String managerEmail;
	public Date creationDate;
	public BillingPlan billingPlan;
	private TokenService tokenService = new TokenService();
	private String _apiToken;

	public SignerEntity(BillingPlan plan, String company, String managerEmail, String apiToken) {
		this._UUID = tokenService.generateUUID();
		this.billingPlan = plan;
		this.companyName = company;
		this.managerEmail = managerEmail;
		this._apiToken = apiToken;
		this.creationDate = new Date();
	}

	public String getApiToken() {
		return _apiToken;
	}
	
	public String getUUID() {
		return this._UUID;
	}
}

