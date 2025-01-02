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
 * Bean SnakeScore qui stocke le score d'une partie de Snake
 * 
 * @author Jonathan
 * @version 1.0
 * @since 31/12/2024
 */
@Entity
@Table(name = "snake")
public class SnakeScore implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private int id;
	private int idUser;
	private int value;
	private Timestamp saveDate;

	// Constructors

	/**
	 * Default Constructor
	 */
	public SnakeScore() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param idUser
	 * @param value
	 * @param saveDate
	 */
	public SnakeScore(int idUser, int value, Timestamp saveDate) {
		this();
		this.idUser = idUser;
		this.value = value;
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
		sb.append(" , Value: ");
		sb.append(String.valueOf(value));
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
	@Column(name = "score_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "score_id_user")
	public int getIdUser() {
		return idUser;
	}

	@SuppressWarnings("unused")
	private void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "score_value")
	public int getValue() {
		return value;
	}

	@SuppressWarnings("unused")
	private void setValue(int value) {
		this.value = value;
	}

	@Column(name = "score_save_date")
	public Timestamp getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Timestamp saveDate) {
		this.saveDate = saveDate;
	}

}
