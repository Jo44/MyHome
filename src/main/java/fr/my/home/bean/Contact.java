package fr.my.home.bean;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Bean Contact
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@Entity
@Table(name = "contacts")
public class Contact {

	/**
	 * Attributs
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "id_user", nullable = false)
	private int id_user;
	@Column(name = "firstname", nullable = false)
	private String firstname;
	@Column(name = "lastname", nullable = true)
	private String lastname;
	@Column(name = "email", nullable = true)
	private String email;
	@Column(name = "phone", nullable = true)
	private String phone;
	@Column(name = "twitter", nullable = true)
	private String twitter;
	@Column(name = "save_date", nullable = false)
	private Timestamp save_date;

	/**
	 * Constructeur
	 */
	public Contact() {}

	/**
	 * Constructeur
	 * 
	 * @param id_user
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param phone
	 * @param twitter
	 * @param save_date
	 */
	public Contact(int id_user, String firstname, String lastname, String email, String phone, String twitter, Timestamp save_date) {
		this.id_user = id_user;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.twitter = twitter;
		this.save_date = save_date;
	}

	/**
	 * Getters/Setters
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUser() {
		return id_user;
	}

	public void setIdUser(int id_user) {
		this.id_user = id_user;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public Timestamp getSaveDate() {
		return save_date;
	}

	public void setSaveDate(Timestamp save_date) {
		this.save_date = save_date;
	}

}
