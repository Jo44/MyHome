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
 * Bean YouTubeActivPlaylist qui stocke les informations d'une playlist YouTube activée
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@Entity
@Table(name = "youtube_playlist")
public class YouTubeActivPlaylist implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private int id;
	private int idUser;
	private String idYouTube;
	private boolean active;
	private Timestamp saveDate;

	// Constructors

	/**
	 * Default Constructor
	 */
	public YouTubeActivPlaylist() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param idUser
	 * @param idYouTube
	 * @param active
	 * @param saveDate
	 */
	public YouTubeActivPlaylist(int idUser, String idYouTube, boolean active, Timestamp saveDate) {
		this();
		this.idUser = idUser;
		this.idYouTube = idYouTube;
		this.active = active;
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
		sb.append(" , YouTube: ");
		sb.append(idYouTube);
		sb.append(" , Active: ");
		sb.append(String.valueOf(active));
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
	@Column(name = "ytpl_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "ytpl_id_user")
	public int getIdUser() {
		return idUser;
	}

	@SuppressWarnings("unused")
	private void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "ytpl_id_yt")
	public String getIdYouTube() {
		return idYouTube;
	}

	public void setIdYouTube(String idYouTube) {
		this.idYouTube = idYouTube;
	}

	@Column(name = "ytpl_active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "ytpl_save_date")
	public Timestamp getSaveDate() {
		return saveDate;
	}

	@SuppressWarnings("unused")
	private void setSaveDate(Timestamp saveDate) {
		this.saveDate = saveDate;
	}

}
