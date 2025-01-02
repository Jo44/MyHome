package fr.my.home.service;

import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.common.collect.Lists;

import fr.my.home.tool.OAuth;
import fr.my.home.tool.properties.Settings;

/**
 * Classe YouTube Service permettant de récupérer le service avec authentification OAuth 2.0
 * 
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
public class YouTubeService {

	// Methods

	/**
	 * Retourne le service YouTube authentifié
	 * 
	 * @return YouTube
	 */
	public static YouTube getYouTubeService() {
		YouTube youtubeService = null;
		// Scopes
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
		try {
			// Authorise la requête
			Credential credential = OAuth.authorize(scopes, "youtube");

			// YouTube Data Service
			youtubeService = new YouTube.Builder(OAuth.HTTP_TRANSPORT, OAuth.JSON_FACTORY, credential)
					.setApplicationName(Settings.getStringProperty("global.website.name")).build();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return youtubeService;
	}
}
