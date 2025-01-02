package fr.my.home.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Bean User qui stocke les utilisateurs de l'application
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private int id;
	private String name;
	private String pass;
	private String email;
	private String rememberMeToken;
	private String validationToken;
	private boolean active;
	private String reInitToken;
	private Timestamp reInitDate;
	private Timestamp lastLogDate;
	private Timestamp inscriptionDate;

	// Constructors

	/**
	 * Default Constructor
	 */
	public User() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param pass
	 * @param email
	 * @param rememberMeToken
	 * @param validationToken
	 * @param active
	 * @param reInitToken
	 * @param reInitDate
	 * @param lastLogDate
	 * @param inscriptionDate
	 */
	public User(String name, String pass, String email, String rememberMeToken, String validationToken, boolean active, String reInitToken,
			Timestamp reInitDate, Timestamp lastLogDate, Timestamp inscriptionDate) {
		this();
		this.name = name;
		this.pass = pass;
		this.email = email;
		this.rememberMeToken = rememberMeToken;
		this.validationToken = validationToken;
		this.active = active;
		this.reInitToken = reInitToken;
		this.reInitDate = reInitDate;
		this.lastLogDate = lastLogDate;
		this.inscriptionDate = inscriptionDate;
	}

	// Methods

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ID: ");
		sb.append(String.valueOf(id));
		sb.append(" , Name: ");
		if (name != null) {
			sb.append(name.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Email: ");
		if (email != null) {
			sb.append(email.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , RememberMe Token: ");
		if (rememberMeToken != null) {
			sb.append(rememberMeToken.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Validation Token: ");
		if (validationToken != null) {
			sb.append(validationToken.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Active: ");
		sb.append(String.valueOf(active));
		sb.append(" , ReInit Token: ");
		if (reInitToken != null) {
			sb.append(reInitToken.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , ReInit Date: ");
		if (reInitDate != null) {
			sb.append(reInitDate.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Last Log Date: ");
		if (reInitDate != null) {
			sb.append(lastLogDate.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Inscription Date: ");
		if (inscriptionDate != null) {
			sb.append(inscriptionDate.toString());
		} else {
			sb.append("null");
		}
		sb.append(" }");
		return sb.toString();
	}

	// Getters/Setters (setters en privé car seulement utilisé par Hibernate)

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "user_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "user_name")
	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	private void setName(String name) {
		this.name = name;
	}

	@Column(name = "user_pass")
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Column(name = "user_email")
	public String getEmail() {
		return email;
	}

	@SuppressWarnings("unused")
	private void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "user_remember_me_token")
	private String getRememberMeToken() {
		return rememberMeToken;
	}

	public void setRememberMeToken(String rememberMeToken) {
		this.rememberMeToken = rememberMeToken;
	}

	@Column(name = "user_validation_token")
	public String getValidationToken() {
		return validationToken;
	}

	@SuppressWarnings("unused")
	private void setValidationToken(String validationToken) {
		this.validationToken = validationToken;
	}

	@Column(name = "user_active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "user_reinit_token")
	public String getReInitToken() {
		return reInitToken;
	}

	public void setReInitToken(String reInitToken) {
		this.reInitToken = reInitToken;
	}

	@Column(name = "user_reinit_date")
	public Timestamp getReInitDate() {
		return reInitDate;
	}

	public void setReInitDate(Timestamp reInitDate) {
		this.reInitDate = reInitDate;
	}

	@Column(name = "user_last_log_date")
	public Timestamp getLastLogDate() {
		return lastLogDate;
	}

	public void setLastLogDate(Timestamp lastLogDate) {
		this.lastLogDate = lastLogDate;
	}

	@Column(name = "user_inscription_date")
	public Timestamp getInscriptionDate() {
		return inscriptionDate;
	}

	@SuppressWarnings("unused")
	private void setInscriptionDate(Timestamp inscriptionDate) {
		this.inscriptionDate = inscriptionDate;
	}

}
