package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import fr.my.home.bean.ActivePlaylist;
import fr.my.home.bean.User;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.youtube.playlists.TitleException;
import fr.my.home.manager.YouTubeManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge les requêtes AJAX pour activer/désactiver une playlist, et renvoi la réponse JSON
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/active_youtube_playlist")
public class ActivePlaylistServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ActivePlaylistServlet.class);
	private YouTubeManager ytMgr;

	/**
	 * Constructeur
	 */
	public ActivePlaylistServlet() {
		super();
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * Traitement d'activation/désactivation d'une playlist YouTube
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Active YouTube Playlist Servlet [POST] {AJAX} -->");
		boolean authenticated = false;

		// Vérifie si l'utilisateur en session possède un token d'accès OAuth 2.0
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user.getAccessToken() != null && !user.getAccessToken().trim().isEmpty()) {
			try {
				// Initialisation du service YouTube
				ytMgr = new YouTubeManager(request.getSession());
				if (ytMgr != null) {
					authenticated = true;
				}
			} catch (Exception ex) {
				logger.error("Erreur d'initialisation du Service YouTube");
				logger.error(ex.getMessage());
			}
		}

		// Selon état de l'authentification
		if (authenticated) {
			boolean active = false;
			String playlistId = "";
			int playlistIndex = -1;
			JsonObject jsonResult = new JsonObject();
			try {
				// Récupère les paramètres de la requête
				String activeStr = request.getParameter("checkboxActive");
				String indexPlaylist = request.getParameter("indexPlaylist");
				playlistId = request.getParameter("idPlaylist");
				playlistIndex = Integer.parseInt(indexPlaylist);

				// Détermine si la playlist doit être activée ou désactivée
				if (activeStr != null && activeStr.equals("true")) {
					active = true;
				}

				// Récupère la playlist en base
				ActivePlaylist playlist = ytMgr.getPlaylist(user.getId(), playlistId);
				if (playlist == null) {
					// Ajoute la playlist en base
					ytMgr.addPlaylist(user.getId(), playlistId, active);
				} else {
					// Modifie la playlist en base
					ytMgr.updatePlaylist(playlist, active);
				}

				// Mise à jour avec succès
				logger.info("Mise à jour de la playlist {" + playlistId + "} réussie -> active: " + String.valueOf(active));
				jsonResult.addProperty("state", "success");
				jsonResult.addProperty("result", active);
			} catch (FonctionnalException fex) {
				if (fex instanceof TitleException) {
					// Mise à jour échouée car titre inexistant
					logger.error("Titre inexistant");
					jsonResult.addProperty("state", "error");
					jsonResult.addProperty("result", "title");
				} else {
					// Mise à jour échouée car utilisateur/playlist non existant
					logger.error("Utilisateur/Playlist inexistant");
					jsonResult.addProperty("state", "error");
					jsonResult.addProperty("result", "user-playlist");
				}
			} catch (TechnicalException tex) {
				// Mise à jour échouée car erreur database
				logger.error("Erreur Database");
				jsonResult.addProperty("state", "error");
				jsonResult.addProperty("result", "database");
			} catch (NullPointerException | NumberFormatException ex) {
				// Mise à jour échouée car erreur de paramètres
				logger.error("Erreur Parameters");
				jsonResult.addProperty("state", "error");
				jsonResult.addProperty("result", "parameters");
			} finally {
				// Ajoute l'index et l'ID de la playlist
				jsonResult.addProperty("index", playlistIndex);
				jsonResult.addProperty("id", playlistId);

				// Renvoi la réponse
				PrintWriter out = response.getWriter();
				response.setHeader("Cache-Control", "no-cache");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(jsonResult);
				out.flush();
			}
		} else {
			// Redirection vers la page de connexion OAuth 2.0
			redirectToLogInServlet(request, response, user);
		}
	}

	/**
	 * Redirige la requête vers la page de connexion OAuth 2.0
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToLogInServlet(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
		// Met à jour l'utilisateur
		user.setAccessToken(null);
		request.getSession().setAttribute("user", user);
		try {
			// Met à jour la base de données
			new UserDAO().update(user);
		} catch (FonctionnalException | TechnicalException ex) {
			logger.error(ex.getMessage());
		}
		// Supprime les informations d'identification de la session
		request.getSession().removeAttribute("oauth_access_token");
		request.getSession().removeAttribute("oauth_expiryDate");
		// Redirection vers la page de connexion OAuth 2.0
		logger.info("=> Redirection vers YouTube Log In");
		response.sendRedirect(request.getContextPath() + "/youtube");
	}

}
