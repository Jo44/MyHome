package fr.my.home.bean;

import java.sql.Timestamp;

/**
 * Bean YouTubePlaylist
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class YouTubePlaylist {

	/**
	 * Attributs
	 */

	private String id;
	private String title;
	private String description;
	private String url_image;
	private String privacy;
	private boolean active;
	private Timestamp published_at;
	private int total_results;
	private String prev_page_token;
	private String next_page_token;

	/**
	 * Constructeur
	 */
	public YouTubePlaylist() {}

	/**
	 * Constructeur
	 */
	public YouTubePlaylist(String id, String title, String description, String url_image, String privacy, boolean active, Timestamp published_at,
			int total_results, String prev_page_token, String next_page_token) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.url_image = url_image;
		this.privacy = privacy;
		this.active = active;
		this.published_at = published_at;
		this.total_results = total_results;
		this.prev_page_token = prev_page_token;
		this.next_page_token = next_page_token;
	}

	/**
	 * Getters/Setters
	 */

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getShortTitle() {
		return title.length() > 30 ? title.substring(0, 30) + ".." : title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrlImage() {
		return url_image;
	}

	public String getPrivacy() {
		return privacy;
	}

	public boolean isActive() {
		return active;
	}

	public Timestamp getPublishedAt() {
		return published_at;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getTotalResults() {
		return total_results;
	}

	public String getPrevPageToken() {
		return prev_page_token;
	}

	public String getNextPageToken() {
		return next_page_token;
	}

}
