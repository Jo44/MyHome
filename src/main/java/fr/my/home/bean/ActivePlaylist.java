package fr.my.home.bean;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Bean ActivePlaylist
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@Entity
@Table(name = "youtube_playlists")
public class ActivePlaylist {

	/**
	 * Attributs
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "id_user", nullable = false)
	private int id_user;
	@Column(name = "id_youtube", nullable = false)
	private String id_youtube;
	@Column(name = "active", nullable = false)
	private boolean active;
	@Column(name = "save_date", nullable = false)
	private Timestamp save_date;

	/**
	 * Constructeur
	 */
	public ActivePlaylist() {}

	/**
	 * Constructeur
	 * 
	 * @param id_user
	 * @param id_youtube
	 * @param active
	 * @param save_date
	 */
	public ActivePlaylist(int id_user, String id_youtube, boolean active, Timestamp save_date) {
		this.id_user = id_user;
		this.id_youtube = id_youtube;
		this.active = active;
		this.save_date = save_date;
	}

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

	public String getIdYouTube() {
		return id_youtube;
	}

	public void setIdYouTube(String id_youTube) {
		this.id_youtube = id_youTube;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Timestamp getSaveDate() {
		return save_date;
	}

	public void setSaveDate(Timestamp save_date) {
		this.save_date = save_date;
	}

}
