package com.veralink.model;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import com.veralink.core.enums.BillingPlan;
import com.veralink.service.SignatureService;
import com.veralink.service.TokenService;

import COSE.CoseException;
import COSE.OneKey;
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
	private Date creationDate;
	private BillingPlan billingPlan;
	private OneKey signKey;
	private TokenService tokenService = new TokenService();

	public SignerEntity(String name, String email, BillingPlan billingPlan) {
		this._UUID = tokenService.generateUUID();
		this.setName(name);
		this.setEmail(email);
		this.setBillingPlan(billingPlan);
		this.setSignKey();
		this.setCreationDate(new Date());
	}
	
	public OneKey getSignKey() {
		return signKey;
	}

	public void setSignKey() {
		try {
			this.signKey = SignatureService.generateOneKeyForSigning();
		} catch (CoseException e) {
			e.printStackTrace();
		}
	}
	
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
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public BillingPlan getBillingPlan() {
		return billingPlan;
	}

	public void setBillingPlan(BillingPlan billingPlan) {
		this.billingPlan = billingPlan;
	}
}
