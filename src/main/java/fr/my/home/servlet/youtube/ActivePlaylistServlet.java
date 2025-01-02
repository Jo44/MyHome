package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import fr.my.home.bean.User;
import fr.my.home.bean.YouTubeActivPlaylist;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.youtube.playlists.TitleException;
import fr.my.home.manager.YouTubeManager;

/**
 * Servlet qui prends en charge les requêtes AJAX pour activer/désactiver une playlist, et renvoi la réponse JSON
 * 
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
@WebServlet("/active_youtube_playlist")
public class ActivePlaylistServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ActivePlaylistServlet.class);

	// Attributes

	private YouTubeManager ytMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public ActivePlaylistServlet() {
		super();
		// Initialisation du manager
		ytMgr = new YouTubeManager();
	}

	// Methods

	/**
	 * Redirection
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirection
		doPost(request, response);
	}

	/**
	 * Traitement d'activation/désactivation d'une playlist YouTube
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Active YouTube Playlist Servlet [POST] {AJAX} -->");
		boolean active = false;
		String playlistId = "";
		int playlistIndex = -1;
		JsonObject jsonResult = new JsonObject();
		try {
			// Récupère l'utilisateur connecté
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				throw new FonctionnalException("Utilisateur incorrect");
			}

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
			YouTubeActivPlaylist playlist = ytMgr.getPlaylist(user.getId(), playlistId);
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
	}

}
