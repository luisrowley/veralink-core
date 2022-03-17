package com.veralink.model;

import java.util.Date;
import java.util.UUID;
import com.veralink.core.enums.BillingPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignerEntity {

	public String _UUID;
	public String companyName;
	public String managerEmail;
	public Date creationDate;
	private BillingPlan billingPlan;
	private String apiToken;

	public SignerEntity(BillingPlan plan, String company, String managerEmail) {
		this.billingPlan = plan;
		this.companyName = company;
		this.managerEmail = managerEmail;
		this._UUID = generateUUID();
	}

	private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr;
	}
}

