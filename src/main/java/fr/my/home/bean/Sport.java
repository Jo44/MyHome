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
 * Bean Sport que peut enregistrer une activité
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@Entity
@Table(name = "sport")
public class Sport implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private int id;
	private int idUser;
	private String activity;
	private Timestamp saveDate;

	// Constructors

	/**
	 * Default Constructor
	 */
	public Sport() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param idUser
	 * @param activity
	 * @param saveDate
	 */
	public Sport(int idUser, String activity, Timestamp saveDate) {
		this();
		this.idUser = idUser;
		this.activity = activity;
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
		sb.append(" , Activity: ");
		if (activity != null) {
			sb.append(activity);
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
	@Column(name = "sport_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "sport_id_user")
	public int getIdUser() {
		return idUser;
	}

	@SuppressWarnings("unused")
	private void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "sport_activity")
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Column(name = "sport_save_date")
	public Timestamp getSaveDate() {
		return saveDate;
	}

	@SuppressWarnings("unused")
	private void setSaveDate(Timestamp saveDate) {
		this.saveDate = saveDate;
	}

}
