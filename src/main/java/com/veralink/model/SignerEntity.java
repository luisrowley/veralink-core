package com.veralink.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.veralink.core.enums.BillingPlan;
import com.veralink.service.SignatureService;
import com.veralink.service.TokenService;

import COSE.CoseException;
import COSE.OneKey;

@Entity
@Table(name="entities")
public class SignerEntity {

    @Id
    @Column(name="id")
	private String id;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "creationDate")
	private Date creationDate;
	@Embedded
	@Column(name = "billingPlan")
	private BillingPlan billingPlan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
 
	@Transient
	private String signKey;

	@Transient
	private TokenService tokenService = new TokenService();

	public SignerEntity(String name, String email, BillingPlan billingPlan) {
		this.id = tokenService.generateUUID();
		this.setName(name);
		this.setEmail(email);
		this.setBillingPlan(billingPlan);
		this.setSignKey();
		this.setCreationDate(new Date());
	}
	
	public String getSignKey() {
		return signKey;
	}

	public void setSignKey() {
		try {
			this.signKey = SignatureService.generateRandomOneKey().toString();
		} catch (CoseException e) {
			e.printStackTrace();
		}
	}
	
	public String getUUID() {
		return this.id;
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
