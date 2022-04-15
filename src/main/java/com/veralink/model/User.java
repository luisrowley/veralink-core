package com.veralink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "passwd")
	private String passwd;
	@Column(name = "token")
	private String token;
	@Column(name = "entities")
	private SignerEntity[] entities;

	public User() {
		
	}

	public User(String name, String passwd) {
		this.name = name;
		this.passwd = passwd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public SignerEntity[] getEntities() {
		return entities;
	}

	public void setEntities(SignerEntity[] entities) {
		this.entities = entities;
	}
}
