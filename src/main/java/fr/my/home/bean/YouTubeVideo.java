package fr.my.home.bean;

import java.io.Serializable;

/**
 * Bean YouTubeVideo qui stocke les informations d'une vidéo YouTube (pour transfert JSP)
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class YouTubeVideo implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String id;
	private String playlistId;
	private String itemPlaylistId;
	private String title;
	private String urlImage;
	private int totalResults;
	private String prevPageToken;
	private String nextPageToken;

	// Constructors

	/**
	 * Default Constructor
	 */
	public YouTubeVideo() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param playlistId
	 * @param itemPlaylistId
	 * @param title
	 * @param urlImage
	 * @param totalResults
	 * @param prevPageToken
	 * @param nextPageToken
	 */
	public YouTubeVideo(String id, String playlistId, String itemPlaylistId, String title, String urlImage, int totalResults, String prevPageToken,
			String nextPageToken) {
		this();
		this.id = id;
		this.playlistId = playlistId;
		this.itemPlaylistId = itemPlaylistId;
		this.title = title;
		this.urlImage = urlImage;
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
		sb.append(" , Playlist ID: ");
		sb.append(playlistId);
		sb.append(" , Item Playlist ID: ");
		sb.append(itemPlaylistId);
		sb.append(" , Title: ");
		sb.append(title);
		sb.append(" , URL Image: ");
		sb.append(urlImage);
		sb.append(" , Total Results: ");
		sb.append(String.valueOf(totalResults));
		sb.append(" , Previous Page Token: ");
		sb.append(prevPageToken);
		sb.append(" , Next Page Token: ");
		sb.append(nextPageToken);
		sb.append(" }");
		return sb.toString();
	}

	// Getters

	public String getId() {
		return id;
	}

	public String getPlaylistId() {
		return playlistId;
	}

	public String getItemPlaylistId() {
		return itemPlaylistId;
	}

	public String getTitle() {
		return title;
	}

	public String getUrlImage() {
		return urlImage;
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
