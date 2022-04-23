package com.veralink.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "passwd")
	private String passwd;
	@Column(name = "token")
	private String token;
	@OneToMany(cascade = CascadeType.ALL)
	@OrderColumn
	private List<SignerEntity> entities = new ArrayList<>();

	public User() {
		
	}

	public User(String name, String passwd) {
		this.name = name;
		this.passwd = passwd;
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

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<SignerEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<SignerEntity> entities) {
		this.entities = entities;
	}
}
