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
 * Bean Contact que peut enregistrer un utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@Entity
@Table(name = "contact")
public class Contact implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private int id;
	private int idUser;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;
	private String twitter;
	private Timestamp saveDate;

	// Constructors

	/**
	 * Default Constructor
	 */
	public Contact() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param idUser
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param phone
	 * @param twitter
	 * @param saveDate
	 */
	public Contact(int idUser, String firstname, String lastname, String email, String phone, String twitter, Timestamp saveDate) {
		this();
		this.idUser = idUser;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.twitter = twitter;
		this.saveDate = saveDate;
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
		sb.append(" , User: ");
		sb.append(String.valueOf(idUser));
		sb.append(" , Firstname: ");
		if (firstname != null) {
			sb.append(firstname);
		} else {
			sb.append("null");
		}
		sb.append(" , Lastname: ");
		if (lastname != null) {
			sb.append(lastname);
		} else {
			sb.append("null");
		}
		sb.append(" , Email: ");
		if (email != null) {
			sb.append(email);
		} else {
			sb.append("null");
		}
		sb.append(" , Phone: ");
		if (phone != null) {
			sb.append(phone);
		} else {
			sb.append("null");
		}
		sb.append(" , Twitter: ");
		if (twitter != null) {
			sb.append(twitter);
		} else {
			sb.append("null");
		}
		sb.append(" , Save Date: ");
		if (saveDate != null) {
			sb.append(saveDate.toString());
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
	@Column(name = "contact_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "contact_id_user")
	public int getIdUser() {
		return idUser;
	}

	@SuppressWarnings("unused")
	private void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "contact_firstname")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "contact_lastname")
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(name = "contact_email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "contact_phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "contact_twitter")
	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	@Column(name = "contact_save_date")
	public Timestamp getSaveDate() {
		return saveDate;
	}

	@SuppressWarnings("unused")
	private void setSaveDate(Timestamp saveDate) {
		this.saveDate = saveDate;
	}

}
