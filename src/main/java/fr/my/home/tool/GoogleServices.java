package fr.my.home.tool;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;

import fr.my.home.bean.User;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.properties.Settings;
import jakarta.servlet.http.HttpSession;

/**
 * Classe GoogleServices en charge de la récupération du YouTube Service (via authentication OAuth 2.0 Google)
 * 
 * @author Jonathan
 * @version 1.0
 * @since 20/01/2025
 */
public class GoogleServices {

	/**
	 * Attributs
	 */

	private static final String CLIENT_ID = Settings.getStringProperty("oauth.client.id");
	private static final String CLIENT_SECRET = Settings.getStringProperty("oauth.client.secret");
	private static final List<String> SCOPES = Collections.singletonList(YouTubeScopes.YOUTUBE);
	private static final String REDIRECT_URI = Settings.getStringProperty("oauth.client.redirect");
	private static final String APPLICATION_NAME = Settings.getStringProperty("global.website.name");
	private static GoogleAuthorizationCodeFlow flow = null;
	private static boolean initialized = false;

	/**
	 * Initialisation du flux Google OAuth 2.0
	 * 
	 * @throws TechnicalException
	 */
	private static void initializeFlow() throws TechnicalException {
		try {
			flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), CLIENT_ID,
					CLIENT_SECRET, SCOPES).setAccessType("offline").build();
			initialized = true;
		} catch (Exception ex) {
			throw new TechnicalException("Erreur lors de l'initialisation du flux Google OAuth 2.0");
		}
	}

	/**
	 * Récupération du service YouTube
	 * 
	 * @param session
	 * @return YouTube
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public static YouTube getYouTubeService(HttpSession session) throws FonctionnalException, TechnicalException {
		YouTube youtube = null;

		// Récupération de l'utilisateur
		User user = (User) session.getAttribute("user");
		try {
			// Récupération des informations d'identification
			String refreshToken = user.getAccessToken();
			String accessToken = (String) session.getAttribute("oauth_access_token");
			Instant expiryDate = (Instant) session.getAttribute("oauth_expiryDate");
			boolean isExpired = expiryDate == null ? true : Instant.now().isAfter(expiryDate);

			// Vérifie si le flux est initialisé
			if (!initialized) {
				initializeFlow();
			}

			// Vérifie si le token d'actualisation est présent
			if (refreshToken == null || refreshToken.trim().isEmpty()) {
				throw new FonctionnalException("Utilisateur OAuth non identifié");
			}

			// Vérifie si le token d'accès est expiré ou non
			if (accessToken == null || isExpired) {
				try {
					// Refresh le token d'accès à partir du token d'actualisation
					TokenResponse tokenResponse = flow.newTokenRequest(refreshToken).setRedirectUri(REDIRECT_URI).execute();
					Credential credential = flow.createAndStoreCredential(tokenResponse, user.getEmail());
					accessToken = credential.getAccessToken();
					if (accessToken != null && !accessToken.trim().isEmpty()) {
						// Met à jour l'utilisateur
						user.setAccessToken(GlobalTools.hash256(credential.getRefreshToken()));
						new UserDAO().update(user);
						session.setAttribute("user", user);
						// Met à jour les informations d'identification dans la session
						session.setAttribute("oauth_access_token", accessToken);
						session.setAttribute("oauth_expiryDate", Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds()));
					}
				} catch (IOException ioex) {
					throw new TechnicalException("Erreur lors de la récupération du token d'accès");
				}
			}

			// Récupération du credential à partir du token d'accès
			Credential credential = new Credential(BearerToken.queryParameterAccessMethod()).setAccessToken(accessToken);
			if (credential == null || credential.getAccessToken() == null || credential.getAccessToken().trim().isEmpty()) {
				throw new TechnicalException("Erreur lors de la récupération du credential");
			}

			// Récupération du service YouTube à partir du credential
			try {
				youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
						.setApplicationName(APPLICATION_NAME).build();
			} catch (IOException | GeneralSecurityException ex) {
				throw new TechnicalException("Erreur lors de la récupération du service YouTube");
			}
		} catch (FonctionnalException | TechnicalException ex) {
			// Met à jour l'utilisateur
			user.setAccessToken(null);
			new UserDAO().update(user);
			session.setAttribute("user", user);
			// Supprime les informations d'identification de la session
			session.removeAttribute("oauth_access_token");
			session.removeAttribute("oauth_expiryDate");
			throw ex;
		}

		// Retourne le service YouTube
		return youtube;
	}

}
