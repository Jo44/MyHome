package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.YouTubeVideo;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion des vidéos YouTube
 * 
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
@WebServlet("/youtube_videos")
public class VideosServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(VideosServlet.class);

	// Attributes

	private YouTubeManager ytMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public VideosServlet() {
		super();
		// Initialisation du manager
		ytMgr = new YouTubeManager();
	}

	// Methods

	/**
	 * Redirection vers l'ajout ou la suppression de vidéo
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Videos Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut error si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Récupère l'attribut success si il existe
		String success = (String) request.getSession().getAttribute("success");
		request.getSession().removeAttribute("success");
		view.addAttributeToList(new ViewAttribut("success", success));

		// Récupère le mode de recherche si il existe
		String searchMethod = (String) request.getSession().getAttribute("searchMethod");
		request.getSession().removeAttribute("searchMethod");
		view.addAttributeToList(new ViewAttribut("searchMethod", searchMethod));

		// Récupère le champs de recherche si il existe
		String searchInput = (String) request.getSession().getAttribute("searchInput");
		request.getSession().removeAttribute("searchInput");
		view.addAttributeToList(new ViewAttribut("searchInput", searchInput));

		// Récupère la page actuelle si elle existe
		String currentPage = (String) request.getSession().getAttribute("currentPage");
		request.getSession().removeAttribute("currentPage");
		view.addAttributeToList(new ViewAttribut("currentPage", currentPage));

		// Récupère la liste des résultats de la recherche si ils existent
		@SuppressWarnings("unchecked")
		List<YouTubeVideo> listVideo = (List<YouTubeVideo>) request.getSession().getAttribute("listVideo");
		request.getSession().removeAttribute("listVideo");
		view.addAttributeToList(new ViewAttribut("listVideo", listVideo));

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les paramètres de la requête
		String action = request.getParameter("action");
		String playlistId = request.getParameter("idPlaylist");
		String playlistItemId = request.getParameter("idPlaylistItem");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					// Redirige vers la JSP d'ajout d'une vidéo
					redirectToVideoJSP(request, response, view, playlistId);
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer la vidéo
					deleteFunction(request, playlistItemId, lang);

					// Redirection
					redirectToPlaylistUpdateServlet(request, response, playlistId);
					break;
				// Si action non reconnu
				default:
					// Redirection
					redirectToPlaylistUpdateServlet(request, response, playlistId);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			redirectToPlaylistUpdateServlet(request, response, playlistId);
		}
	}

	/**
	 * Traitement du formulaire d'ajout ou de recherche de vidéos
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Videos Servlet [POST] -->");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		String action = request.getParameter("action");
		String playlistId = request.getParameter("idPlaylist");
		String searchMethod = request.getParameter("searchMethod");
		String searchInput = request.getParameter("searchInput");
		String currentPageStr = request.getParameter("currentPage");
		String prevPageToken = request.getParameter("prevPageToken");
		String nextPageToken = request.getParameter("nextPageToken");
		String submitMethod = request.getParameter("submitMethod");
		String[] listUrlId = request.getParameterValues("cbUrlId");
		int currentPage = 1;
		if (currentPageStr != null) {
			currentPage = Integer.parseInt(currentPageStr);
		}

		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					logger.info("Tentative d'ajout des vidéos en cours ..");

					// Essaye d'ajouter les vidéos
					addFunction(request, playlistId, listUrlId, lang);

					// Redirige vers le détail de la playlist
					redirectToPlaylistUpdateServlet(request, response, playlistId);
					break;
				// Si action modifier
				case "search":
					logger.info("Tentative de recherche des vidéos en cours ..");

					// Recherche les vidéos
					searchFunction(request, searchMethod, searchInput, currentPage, prevPageToken, nextPageToken, submitMethod, lang);

					// Redirige vers la servlet en get
					redirectToAddVideoServlet(request, response, playlistId);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Ajoute la liste des vidéos à la playlist, charge le message succes/erreur
	 * 
	 * @param request
	 * @param playlistId
	 * @param idUrl
	 * @param lang
	 */
	private void addFunction(HttpServletRequest request, String playlistId, String[] listUrlId, String lang) {
		if (listUrlId != null && listUrlId.length > 0) {
			int count = 0;
			// Pour chaque vidéo ..
			for (String urlId : listUrlId) {
				// Ajoute la vidéo à la playlist YouTube Data de l'utilisateur OAuth
				String videoId = ytMgr.addVideoPlaylistYTData(playlistId, urlId);
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
			// Charge l'erreur dans la view
			request.getSession().setAttribute("error", Messages.getProperty("error.yt.video.cant.delete", lang));
		}
	}

	/**
	 * Redirige la requête vers la JSP Video
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param playlistId
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToVideoJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view, String playlistId)
			throws ServletException, IOException {
		// Charge l'ID de la playlist dans la view
		view.addAttributeToList(new ViewAttribut("idPlaylist", playlistId));

		// Charge la view dans la requête
		request.setAttribute("view", view);

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

}
