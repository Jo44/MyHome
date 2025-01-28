package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;
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
 * Servlet qui prends en charge la gestion des vidéos YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/youtube_videos")
public class VideosServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(VideosServlet.class);
	private YouTubeManager ytMgr;

	/**
	 * Constructeur
	 */
	public VideosServlet() {
		super();
	}

	/**
	 * Redirection vers l'ajout ou la suppression de vidéo
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Videos Servlet [GET] -->");
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
			// Récupère l'attribut error si il existe
			String error = (String) request.getSession().getAttribute("error");
			request.getSession().removeAttribute("error");
			request.setAttribute("error", error);

			// Récupère l'attribut success si il existe
			String success = (String) request.getSession().getAttribute("success");
			request.getSession().removeAttribute("success");
			request.setAttribute("success", success);

			// Récupère le mode de recherche si il existe
			String searchMethod = (String) request.getSession().getAttribute("searchMethod");
			request.getSession().removeAttribute("searchMethod");
			request.setAttribute("searchMethod", searchMethod);

			// Récupère le champs de recherche si il existe
			String searchInput = (String) request.getSession().getAttribute("searchInput");
			request.getSession().removeAttribute("searchInput");
			request.setAttribute("searchInput", searchInput);

			// Récupère la page actuelle si elle existe
			String currentPage = (String) request.getSession().getAttribute("currentPage");
			request.getSession().removeAttribute("currentPage");
			request.setAttribute("currentPage", currentPage);

			// Récupère la liste des résultats de la recherche si ils existent
			@SuppressWarnings("unchecked")
			List<YouTubeVideo> listVideo = (List<YouTubeVideo>) request.getSession().getAttribute("listVideo");
			request.getSession().removeAttribute("listVideo");
			request.setAttribute("listVideo", listVideo);

			// Récupère la langue de l'utilisateur (pour messages success/error)
			String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

			// Récupère les paramètres de la requête
			String action = request.getParameter("action");
			String idPlaylist = request.getParameter("idPlaylist");
			String playlistItemId = request.getParameter("idPlaylistItem");

			// Détermine le traitement en fonction des paramètres
			// Si paramètre action est renseigné
			if (action != null) {
				switch (action) {
					// Si action ajouter
					case "add":
						// Prépare les attributs de la JSP
						setupAttributes(request, idPlaylist, currentPage, listVideo);
						// Redirige vers la JSP d'ajout d'une vidéo
						redirectToVideoJSP(request, response);
						break;
					// Si action supprimer
					case "delete":
						// Essaye de supprimer la vidéo
						deleteFunction(request, playlistItemId, lang);

						// Redirection
						redirectToPlaylistUpdateServlet(request, response, idPlaylist);
						break;
					// Si action non reconnu
					default:
						// Redirection
						redirectToPlaylistUpdateServlet(request, response, idPlaylist);
						break;
				}
			} else {
				// Si paramètre action non renseigné
				redirectToPlaylistUpdateServlet(request, response, idPlaylist);
			}
		} else {
			// Redirection vers la page de connexion OAuth 2.0
			redirectToLogInServlet(request, response, user);
		}
	}

	/**
	 * Traitement du formulaire d'ajout ou de recherche de vidéos
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Videos Servlet [POST] -->");
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
			// Récupère la langue de l'utilisateur (pour messages success/error)
			String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

			// Récupère les informations saisies dans le formulaire
			String action = request.getParameter("action");
			String idPlaylist = request.getParameter("idPlaylist");
			String searchMethod = request.getParameter("searchMethod");
			String searchInput = request.getParameter("searchInput");
			String currentPageStr = request.getParameter("currentPage");
			String prevPageToken = request.getParameter("prevPageToken");
			String nextPageToken = request.getParameter("nextPageToken");
			String submitMethod = request.getParameter("submitMethod");
			String[] listUrlId = request.getParameterValues("cbUrlId");
			int currentPage = 1;
			if (currentPageStr != null && !currentPageStr.trim().isEmpty()) {
				currentPage = Integer.parseInt(currentPageStr);
			}

			// Si paramètre action est renseigné
			if (action != null) {
				switch (action) {
					// Si action ajouter
					case "add":
						logger.info("Tentative d'ajout des vidéos en cours ..");

						// Essaye d'ajouter les vidéos
						addFunction(request, idPlaylist, listUrlId, lang);

						// Redirige vers le détail de la playlist
						redirectToPlaylistUpdateServlet(request, response, idPlaylist);
						break;
					// Si action modifier
					case "search":
						logger.info("Tentative de recherche des vidéos en cours ..");

						// Recherche les vidéos
						searchFunction(request, searchMethod, searchInput, currentPage, prevPageToken, nextPageToken, submitMethod, lang);

						// Redirige vers la servlet en get
						redirectToAddVideoServlet(request, response, idPlaylist);
						break;
					default:
						break;
				}
			}
		} else {
			// Redirection vers la page de connexion OAuth 2.0
			redirectToLogInServlet(request, response, user);
		}
	}

	/**
	 * Ajoute la liste des vidéos à la playlist, charge le message succes/erreur
	 * 
	 * @param request
	 * @param idPlaylist
	 * @param idUrl
	 * @param lang
	 */
	private void addFunction(HttpServletRequest request, String idPlaylist, String[] listUrlId, String lang) {
		if (listUrlId != null && listUrlId.length > 0) {
			int count = 0;
			// Pour chaque vidéo ..
			for (String urlId : listUrlId) {
				// Ajoute la vidéo à la playlist YouTube Data de l'utilisateur OAuth
				String videoId = ytMgr.addVideoPlaylistYTData(idPlaylist, urlId);
				// Incrémente le compteur si ajoutée avec succès
				if (videoId != null && !videoId.trim().isEmpty()) {
					count++;
				}
			}
			if (count > 1) {
				// Ajoute le message d'ajout avec succès
				request.getSession().setAttribute("success", Messages.getProperty("success.yt.video.adds", lang));
			} else if (count == 1) {
				// Ajoute le message d'ajout avec succès
				request.getSession().setAttribute("success", Messages.getProperty("success.yt.video.add", lang));
			} else {
				// Ajoute le message d'erreur
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.video.add", lang));
			}
		} else {
			// Ajoute le message d'erreur
			request.getSession().setAttribute("error", Messages.getProperty("error.yt.video.add", lang));
		}
	}

	/**
	 * Recherche la liste des vidéos en fonction de différents paramètres, charge le message succes/erreur
	 * 
	 * @param request
	 * @param searchMethod
	 * @param searchInput
	 * @param currentPage
	 * @param prevPageToken
	 * @param nextPageToken
	 * @param submitMethod
	 * @param lang
	 */
	private void searchFunction(HttpServletRequest request, String searchMethod, String searchInput, int currentPage, String prevPageToken,
			String nextPageToken, String submitMethod, String lang) {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		// Récupère pageToken en fonction de submitMethod
		String pageToken = ytMgr.getPageToken(prevPageToken, nextPageToken, submitMethod);
		int newPage = ytMgr.getNewPage(currentPage, submitMethod);
		// Récupère les vidéos YouTube Data de la recherche de l'utilisateur OAuth
		if (searchMethod != null && searchMethod.equals("url")) {
			// Recherche par URL
			listVideo = ytMgr.searchUrlResults(searchInput, pageToken);
		} else {
			// Recherche par keywords
			listVideo = ytMgr.searchKeywordsResults(searchInput, pageToken);
		}
		// Ajoute les attributs de session
		request.getSession().setAttribute("searchMethod", searchMethod);
		request.getSession().setAttribute("searchInput", searchInput);
		request.getSession().setAttribute("currentPage", String.valueOf(newPage));
		request.getSession().setAttribute("listVideo", listVideo);
		// Ajoute le message d'erreur si besoin
		if (listVideo.size() < 1) {
			request.getSession().setAttribute("error", Messages.getProperty("error.yt.video.search", lang));
		}
	}

	/**
	 * Supprime la vidéo en fonction de son ID playlist item, charge le message succes/erreur
	 * 
	 * @param request
	 * @param playlistItemId
	 * @param lang
	 */
	private void deleteFunction(HttpServletRequest request, String playlistItemId, String lang) {
		// Supprime la vidéo de la playlist YouTube Data de l'utilisateur OAuth
		boolean deleted = ytMgr.deleteVideoPlaylistYTDAta(playlistItemId);

		// Si la vidéo est supprimée
		if (deleted) {
			// Ajoute le message de suppression avec succès
			request.getSession().setAttribute("success", Messages.getProperty("success.yt.video.delete", lang));
		} else {
			// Charge l'erreur dans la requête
			request.getSession().setAttribute("error", Messages.getProperty("error.yt.video.cant.delete", lang));
		}
	}

	/**
	 * Prépare les attributs envoyés à la JSP
	 * 
	 * @param request
	 * @param idPlaylist
	 * @param currentPage
	 * @param listVideo
	 */
	private void setupAttributes(HttpServletRequest request, String idPlaylist, String currentPage, List<YouTubeVideo> listVideo) {
		// Prépare la pagination
		setupPaging(request, currentPage, listVideo);
		// Ajoute l'attribut à la requête
		request.setAttribute("idPlaylist", idPlaylist);
	}

	/**
	 * Prépare la pagination
	 * 
	 * @param request
	 * @param currentPageStr
	 * @param listVideo
	 */
	private void setupPaging(HttpServletRequest request, String currentPageStr, List<YouTubeVideo> listVideo) {
		int currentPage = (currentPageStr == null || currentPageStr.isEmpty()) ? 1 : Integer.parseInt(currentPageStr);
		int pageResults = 0;
		String prevPageToken = "";
		String nextPageToken = "";
		String displayPagination = "";
		if (listVideo != null && listVideo.size() > 0) {
			if (listVideo.get(0).getPrevPageToken() != null && !listVideo.get(0).getPrevPageToken().isEmpty()) {
				prevPageToken = listVideo.get(0).getPrevPageToken();
			}
			if (listVideo.get(0).getNextPageToken() != null && !listVideo.get(0).getNextPageToken().isEmpty()) {
				nextPageToken = listVideo.get(0).getNextPageToken();
			}
			pageResults = listVideo.size();
			if (listVideo.size() > 1) {
				displayPagination = String.valueOf(((currentPage - 1) * 10) + 1) + " - " + String.valueOf(((currentPage - 1) * 10) + pageResults);
			}
		}
		// Ajoute les attributs à la requête
		request.setAttribute("displayPagination", displayPagination);
		request.setAttribute("prevPageToken", prevPageToken);
		request.setAttribute("nextPageToken", nextPageToken);
	}

	/**
	 * Redirige la requête vers la JSP Video
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToVideoJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Video JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/video.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet avec action add en Get
	 * 
	 * @param request
	 * @param response
	 * @param idPlaylist
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToAddVideoServlet(HttpServletRequest request, HttpServletResponse response, String idPlaylist)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getContextPath().toString() + "/youtube_videos?action=add&idPlaylist=" + idPlaylist);
	}

	/**
	 * Redirige la requête vers la servlet Playlist avec action update en Get
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToPlaylistUpdateServlet(HttpServletRequest request, HttpServletResponse response, String idPlaylist)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getContextPath().toString() + "/youtube_playlists?action=update&idPlaylist=" + idPlaylist);
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
