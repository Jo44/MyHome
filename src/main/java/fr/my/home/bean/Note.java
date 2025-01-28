package fr.my.home.bean;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Bean Note
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@Entity
@Table(name = "notes")
public class Note {

	/**
	 * Attributs
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "id_user", nullable = false)
	private int id_user;
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "message", nullable = false)
	private String message;
	@Column(name = "save_date", nullable = false)
	private Timestamp save_date;

	/**
	 * Constructeur
	 */
	public Note() {}

	/**
	 * Constructeur
	 * 
	 * @param id_user
	 * @param title
	 * @param message
	 * @param save_date
	 */
	public Note(int id_user, String title, String message, Timestamp save_date) {
		this.id_user = id_user;
		this.title = title;
		this.message = message;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getSaveDate() {
		return save_date;
	}

	public void setSaveDate(Timestamp save_date) {
		this.save_date = save_date;
	}

}
