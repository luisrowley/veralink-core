package com.veralink.model;

public class User {

	private String user;
	private String passwd;
	private String token;
	private SignerEntity[] entities;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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
