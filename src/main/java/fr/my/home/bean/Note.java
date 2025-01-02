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
 * Bean Note que peut enregistrer un utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@Entity
@Table(name = "note")
public class Note implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private int id;
	private int idUser;
	private String title;
	private String message;
	private Timestamp saveDate;

	// Constructors

	/**
	 * Default Constructor
	 */
	public Note() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param idUser
	 * @param title
	 * @param message
	 * @param saveDate
	 */
	public Note(int idUser, String title, String message, Timestamp saveDate) {
		this();
		this.idUser = idUser;
		this.title = title;
		this.message = message;
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
		sb.append(" , Title: ");
		if (title != null) {
			sb.append(title);
		} else {
			sb.append("null");
		}
		sb.append(" , Message: ");
		if (message != null) {
			sb.append(message);
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
	@Column(name = "note_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "note_id_user")
	public int getIdUser() {
		return idUser;
	}

	@SuppressWarnings("unused")
	private void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "note_title")
	public String getTitle() {
		return title;
	}

	@SuppressWarnings("unused")
	private void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "note_message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "note_save_date")
	public Timestamp getSaveDate() {
		return saveDate;
	}

	@SuppressWarnings("unused")
	private void setSaveDate(Timestamp saveDate) {
		this.saveDate = saveDate;
	}

}
