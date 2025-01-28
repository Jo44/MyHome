package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.ActivePlaylist;
import fr.my.home.bean.User;
import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la gestion du player YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/youtube_player")
public class PlayerServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(PlayerServlet.class);
	private YouTubeManager ytMgr;

	/**
	 * Constructeur
	 */
	public PlayerServlet() {
		super();
	}

	/**
	 * Redirection vers YouTube Player ou la page de connexion (selon état de l'authentification)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Player Servlet [GET] -->");
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
			// Supprime l'attribut erreur si il existe
			request.getSession().removeAttribute("error");

			// Récupère la langue de l'utilisateur (pour messages success/error)
			String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

			// Récupère la liste des playlists actives de l'utilisateur et leurs vidéos
			getActivePlaylistsAndVideosFunction(request, user.getId(), lang);

			// Redirection vers JSP
			redirectToYouTubePlayerJSP(request, response);
		} else {
			// Redirection vers la page de connexion OAuth 2.0
			redirectToLogInServlet(request, response, user);
		}
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Récupère la liste des playlists actives et leurs vidéos et les charge dans la requête, et erreur si besoin
	 * 
	 * @param request
	 * @param userId
	 * @param lang
	 */
	private void getActivePlaylistsAndVideosFunction(HttpServletRequest request, int userId, String lang) {
		String channelName = "???";
		List<YouTubePlaylist> listPlaylist = new ArrayList<YouTubePlaylist>();
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		try {
			// Récupère le nom de l'utilisateur YouTube
			channelName = ytMgr.getChannelNameYTData();
			// Récupère la liste des playlists YouTube Data de l'utilisateur OAuth (les 30 premières max)
			List<YouTubePlaylist> listAllPlaylist = ytMgr.getPlaylistsYTData("");
			if (listAllPlaylist.size() > 0) {
				// Tant que la dernière playlist de la liste possède un token next page
				while (listAllPlaylist.get(listAllPlaylist.size() - 1).getNextPageToken() != null
						&& !listAllPlaylist.get(listAllPlaylist.size() - 1).getNextPageToken().trim().isEmpty()) {
					// Récupère la liste des playlists de la page suivante
					List<YouTubePlaylist> listAddAllPlaylist = ytMgr
							.getPlaylistsYTData(listAllPlaylist.get(listAllPlaylist.size() - 1).getNextPageToken());
					// Ajoute les nouvelles playlists récupérées à la liste globale
					listAllPlaylist.addAll(listAddAllPlaylist);
				}
			}

			// Récupère la liste des playlists actives de l'utilisateur en base
			List<ActivePlaylist> listActivPlaylist = ytMgr.getPlaylists(userId);

			// Met à jour l'activation des playlists
			listAllPlaylist = ytMgr.checkActive(listAllPlaylist, listActivPlaylist);

			// Pour chaque playlist active
			for (YouTubePlaylist playlist : listAllPlaylist) {
				if (playlist != null && playlist.isActive()) {
					// Récupère la liste des vidéos YouTube Data de la playlist (les 10 premières max)
					List<YouTubeVideo> listVideoThisPlaylist = ytMgr.getVideosYTData(playlist.getId(), "");
					if (listVideoThisPlaylist.size() > 0) {
						// Tant que la dernière vidéo de la liste possède un token next page
						while (listVideoThisPlaylist.get(listVideoThisPlaylist.size() - 1).getNextPageToken() != null
								&& !listVideoThisPlaylist.get(listVideoThisPlaylist.size() - 1).getNextPageToken().trim().isEmpty()) {
							// Récupère la liste des vidéos de la page suivante
							List<YouTubeVideo> listAddVideoThisPlaylist = ytMgr.getVideosYTData(playlist.getId(),
									listVideoThisPlaylist.get(listVideoThisPlaylist.size() - 1).getNextPageToken());
							// Ajoute les nouvelles vidéos récupérées à la liste globale
							listVideoThisPlaylist.addAll(listAddVideoThisPlaylist);
						}
					}
					// Ajoute la playlist
					listPlaylist.add(playlist);
					// Ajoute les vidéos
					listVideo.addAll(listVideoThisPlaylist);
				}
			}
		} catch (TechnicalException tex) {
			request.setAttribute("error", Messages.getProperty("error.database", lang));
		}
		// Ajoute les attributs à la requête
		request.setAttribute("channelName", channelName);
		request.setAttribute("listPlaylist", listPlaylist);
		request.setAttribute("listVideo", listVideo);
	}

	/**
	 * Redirige la requête vers la JSP Youtube Player
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToYouTubePlayerJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> YouTube Player JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/player.jsp");
		dispatcher.forward(request, response);
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
