package fr.my.home.bean;

import fr.my.home.tool.GlobalTools;

/**
 * Bean YouTubeVideo
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class YouTubeVideo {

	/**
	 * Attributs
	 */

	private String id;
	private String playlist_id;
	private String item_id;
	private String title;
	private String url_image;
	private int total_results;
	private String prev_page_token;
	private String next_page_token;

	/**
	 * Constructeur
	 */
	public YouTubeVideo() {}

	/**
	 * Constructeur
	 * 
	 * @param id
	 * @param playlist_id
	 * @param item_id
	 * @param title
	 * @param url_image
	 * @param total_results
	 * @param prev_page_token
	 * @param next_page_token
	 */
	public YouTubeVideo(String id, String playlist_id, String item_id, String title, String url_image, int total_results, String prev_page_token,
			String next_page_token) {
		this.id = id;
		this.playlist_id = playlist_id;
		this.item_id = item_id;
		this.title = title;
		this.url_image = url_image;
		this.total_results = total_results;
		this.prev_page_token = prev_page_token;
		this.next_page_token = next_page_token;
	}

	/**
	 * Getters
	 */

	public String getId() {
		return id;
	}

	public String getPlaylistId() {
		return playlist_id;
	}

	public String getItemPlaylistId() {
		return item_id;
	}

	public String getTitle() {
		return title;
	}

	public String getShortTitle() {
		return title.length() > 30 ? title.substring(0, 30) + ".." : title;
	}

	public String getYoutubeTitle() {
		return GlobalTools.formattedYTTitle(title);
	}

	public String getUrlImage() {
		return url_image;
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
