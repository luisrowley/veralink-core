package com.veralink.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.context.SecurityContextHolder;

import com.veralink.core.enums.BillingPlan;
import com.veralink.service.SignatureService;
import com.veralink.service.TokenService;

import COSE.CoseException;
import COSE.OneKey;

@Entity
@Table(name="entities")
public class SignerEntity {

    @Id
    @GeneratedValue
    @Column(name="id")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "creationDate")
	private Date creationDate;
	@Column(name = "createdBy")
	private String createdBy;
    @Column(name="totalCodes")
	private int totalCodes;
    @Column(name="last_signature_date")
	private Date lastSignature;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

	@Transient
	private String uuid;

	@Transient
	private OneKey signKey;

	@Transient
	private TokenService tokenService = new TokenService();

	public SignerEntity() {		
	}

	public SignerEntity(String name) {
		this.uuid = tokenService.generateUUID();
		this.setName(name);
		this.setSignKey();
		this.setCreationDate(new Date());
	}
	
	public OneKey getSignKey() {
		return signKey;
	}

	public void setSignKey() {
		try {
			this.signKey = SignatureService.generateRandomOneKey();
		} catch (CoseException e) {
			e.printStackTrace();
		}
	}
	
	public String getUUID() {
		return this.uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
