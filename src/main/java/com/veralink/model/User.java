package com.veralink.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.veralink.core.enums.BillingPlan;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "token", columnDefinition = "LONGBLOB")
	private String token;
	@Column(name = "billingPlan")
	private String billingPlan;

	@OneToMany(cascade = CascadeType.ALL)
	@OrderColumn
	private List<SignerEntity> entities = new ArrayList<>();

	public User() {
		
	}

	public User(String name,  String email, String password, String billingPlan) {
		this.setName(name);
		this.setEmail(email);
		this.setPassword(password);
		this.setBillingPlan(billingPlan);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getBillingPlan() {
		return billingPlan;
	}

	public void setBillingPlan(String billingPlan) {
		this.billingPlan = billingPlan;
	}

	public List<SignerEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<SignerEntity> entities) {
		this.entities = entities;
	}
}
