package fr.my.home.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Bean YouTubePlaylist qui stocke les informations d'une playlist YouTube (pour transfert JSP)
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class YouTubePlaylist implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String id;
	private String title;
	private String description;
	private String urlImage;
	private String privacy;
	private boolean active;
	private Timestamp publishedAt;
	private int totalResults;
	private String prevPageToken;
	private String nextPageToken;

	// Constructors

	/**
	 * Default Constructor
	 */
	public YouTubePlaylist() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param title
	 * @param description
	 * @param urlImage
	 * @param privacy
	 * @param active
	 * @param publishedAt
	 * @param totalResults
	 * @param prevPageToken
	 * @param nextPageToken
	 */
	public YouTubePlaylist(String id, String title, String description, String urlImage, String privacy, boolean active, Timestamp publishedAt,
			int totalResults, String prevPageToken, String nextPageToken) {
		this();
		this.id = id;
		this.title = title;
		this.description = description;
		this.urlImage = urlImage;
		this.privacy = privacy;
		this.active = active;
		this.publishedAt = publishedAt;
		this.totalResults = totalResults;
		this.prevPageToken = prevPageToken;
		this.nextPageToken = nextPageToken;
	}

	// Methods

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ID: ");
		sb.append(id);
		sb.append(" , Title: ");
		sb.append(title);
		sb.append(" , Description: ");
		sb.append(description);
		sb.append(" , URL Image: ");
		sb.append(urlImage);
		sb.append(" , Privacy: ");
		sb.append(privacy);
		sb.append(" , Active: ");
		sb.append(String.valueOf(active));
		sb.append(" , Published At: ");
		if (publishedAt != null) {
			sb.append(publishedAt.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Total Results: ");
		sb.append(String.valueOf(totalResults));
		sb.append(" , Previous Page Token: ");
		sb.append(prevPageToken);
		sb.append(" , Next Page Token: ");
		sb.append(nextPageToken);
		sb.append(" }");
		return sb.toString();
	}

	// Getters/Setters

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public String getPrivacy() {
		return privacy;
	}

	public boolean isActive() {
		return active;
	}

	public Timestamp getPublishedAt() {
		return publishedAt;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public String getPrevPageToken() {
		return prevPageToken;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

}
