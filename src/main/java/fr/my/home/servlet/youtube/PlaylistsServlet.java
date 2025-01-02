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

import com.google.api.services.youtube.model.Playlist;

import fr.my.home.bean.User;
import fr.my.home.bean.YouTubeActivPlaylist;
import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.youtube.playlists.CantAddException;
import fr.my.home.exception.youtube.playlists.CantDeleteActiveException;
import fr.my.home.exception.youtube.playlists.CantDeleteException;
import fr.my.home.exception.youtube.playlists.CantUpdateException;
import fr.my.home.exception.youtube.playlists.DescriptionException;
import fr.my.home.exception.youtube.playlists.TitleException;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion des playlists YouTube
 * 
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
@WebServlet("/youtube_playlists")
public class PlaylistsServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(PlaylistsServlet.class);

	// Attributes

	private YouTubeManager ytMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public PlaylistsServlet() {
		super();
		// Initialisation du manager
		ytMgr = new YouTubeManager();
	}

	// Methods

	/**
	 * Redirection vers la liste, l'ajout, la modification ou la suppression de playlist
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Playlists Servlet [GET] -->");

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

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les paramètres de la requête
		String action = request.getParameter("action");
		String youtubeId = request.getParameter("idPlaylist");
		String currentPage = request.getParameter("current");
		String pageToken = request.getParameter("pageToken");
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action list
				case "list":
					// Renvoi à la liste des playlists de l'utilisateur
					redirectToPlaylistList(request, response, view, userId, currentPage, pageToken, orderBy, dir, lang);
					break;
				// Si action ajouter
				case "add":
					// Redirection vers la JSP d'ajout
					redirectToNewYouTubePlaylistJSP(request, response, view);
					break;
				// Si action modifier
				case "update":
					// Récupère la playlist
					boolean exist = getOneFunction(request, view, userId, youtubeId, currentPage, pageToken, orderBy, dir, lang);
					if (exist) {
						// Redirige vers la JSP de modification
						redirectToUpdateYouTubePlaylistJSP(request, response, view);
					} else {
						// Renvoi à la liste des playlists de l'utilisateur
						redirectToThisServletWithList(request, response);
					}
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer la playlist
					deleteFunction(request, userId, youtubeId, lang);

					// Renvoi à la liste des playlists de l'utilisateur
					redirectToThisServletWithList(request, response);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des playlists de l'utilisateur
					redirectToThisServletWithList(request, response);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des playlists de l'utilisateur
			redirectToThisServletWithList(request, response);
		}
	}

	/**
	 * Traitement d'ajout / modification d'un formulaire d'une playlist
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Playlists Servlet [POST] -->");

		// Récupère l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		String action = request.getParameter("action");
		String title = new String(request.getParameter("titlePlaylist").trim().getBytes("ISO-8859-1"), "UTF-8");
		String description = new String(request.getParameter("descriptionPlaylist").trim().getBytes("ISO-8859-1"), "UTF-8");
		String privacy = request.getParameter("radioPrivacy");
		String active = request.getParameter("checkboxActive");
		String playlistId = request.getParameter("idPlaylist");

		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					logger.info("Tentative d'ajout d'une playlist en cours ..");

					// Essaye d'ajouter la playlist
					playlistId = addFunction(request, user.getId(), title, description, privacy, active, lang);

					// Redirection en fonction de l'ID retour
					if (!playlistId.trim().isEmpty()) {
						// Redirige vers la page de détails de la playlist
						redirectToThisServletWithUpdate(request, response, playlistId);
					} else {
						// Redirige vers la page de liste de playlist
						redirectToThisServletWithList(request, response);
					}
					break;
				// Si action modifier
				case "update":
					logger.info("Tentative de modification d'une playlist en cours ..");

					// Essaye de modifier la playlist
					updateFunction(request, user.getId(), playlistId, title, description, privacy, active, lang);

					// Redirige vers la page de détails de la playlist
					redirectToThisServletWithUpdate(request, response, playlistId);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Récupère la playlist de l'utilisateur, renvoi un boolean si elle existe et la charge dans la view, ou erreur si besoin
	 * 
	 * @param request
	 * @param view
	 * @param userId
	 * @param playlistId
	 * @param currentPage
	 * @param pageToken
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @return boolean
	 */
	private boolean getOneFunction(HttpServletRequest request, ViewJSP view, int userId, String playlistId, String currentPage, String pageToken,
			String orderBy, String dir, String lang) {
		boolean exist = false;
		YouTubePlaylist playlistYTData = null;
		List<YouTubeVideo> listVideoYTData = null;
		YouTubeActivPlaylist playlist = null;

		try {
			// Récupère la playlist YouTube Data de l'utilisateur OAuth selon la playlist ID
			playlistYTData = ytMgr.getPlaylistYTData(playlistId);

			// La playlist existe
			if (playlistYTData != null) {
				// Récupère la liste des vidéos YouTube Data de l'utilisateur OAuth selon la playlist ID et le token de page
				listVideoYTData = ytMgr.getVideosYTData(playlistId, pageToken);

				// Récupère la playlist de l'utilisateur
				playlist = ytMgr.getPlaylist(userId, playlistId);

				// Met à jour l'activation de la playlist
				if (playlist != null && playlist.isActive()) {
					playlistYTData.setActive(true);
				}

				// Ajoute les objets dans la view
				view.addAttributeToList(new ViewAttribut("current", currentPage));
				view.addAttributeToList(new ViewAttribut("playlist", playlistYTData));
				view.addAttributeToList(new ViewAttribut("listVideo", listVideoYTData));
				exist = true;
			} else {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.not.exist", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
		return exist;
	}

	/**
	 * Ajoute la playlist en fonction de divers paramètres, charge message succès/erreur
	 * 
	 * @param request
	 * @param userId
	 * @param title
	 * @param description
	 * @param privacy
	 * @param activeStr
	 * @param lang
	 * @return String
	 */
	private String addFunction(HttpServletRequest request, int userId, String title, String description, String privacy, String activeStr,
			String lang) {
		String id = "";
		try {
			// Vérifie si les paramètres sont valides
			ytMgr.checkParamsPlaylist(title, description);

			// Détermine si la playlist doit être activée ou désactivée
			boolean active = false;
			if (activeStr != null && activeStr.trim().equals("on")) {
				active = true;
			}

			// Ajoute la playlist sur YouTube Data
			String youtubeId = ytMgr.addPlaylistYTData(title, description, privacy);

			// Si playlist ajoutée correctement
			if (!youtubeId.trim().isEmpty()) {
				// Ajoute la playlist en base
				id = ytMgr.addPlaylist(userId, youtubeId, active);

				// Ajoute le message d'ajout avec succès
				request.getSession().setAttribute("success", Messages.getProperty("success.yt.playlist.add", lang));
			} else {
				throw new CantAddException("Impossible d'ajouter la playlist");
			}
		} catch (FonctionnalException fex) {
			if (fex instanceof TitleException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.title", lang));
			} else if (fex instanceof DescriptionException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.description", lang));
			} else if (fex instanceof CantAddException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.add", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
		return id;
	}

	/**
	 * Met à jour la playlist en fonction de divers paramètres, charge message succès/erreur
	 * 
	 * @param request
	 * @param userId
	 * @param playlistId
	 * @param title
	 * @param description
	 * @param privacy
	 * @param activeStr
	 * @param lang
	 */
	private void updateFunction(HttpServletRequest request, int userId, String playlistId, String title, String description, String privacy,
			String activeStr, String lang) {
		try {
			// Vérifie si les paramètres sont valides
			ytMgr.checkParamsPlaylist(title, description);

			// Détermine si la playlist doit être activée ou désactivée
			boolean active = false;
			if (activeStr != null && activeStr.equals("on")) {
				active = true;
			}

			// Récupère la playlist à modifier
			Playlist playlistYTData = ytMgr.getRawPlaylistYTData(playlistId);

			// Si besoin de modifier sur YouTube Data
			if (!playlistYTData.getSnippet().getTitle().equals(title) || !playlistYTData.getSnippet().getDescription().equals(description)
					|| !playlistYTData.getStatus().getPrivacyStatus().equals(privacy)) {
				// Modifie la playlist sur YouTube Data
				String youtubeId = ytMgr.updatePlaylistYTData(playlistYTData, title, description, privacy);
				// Si erreur de modification
				if (youtubeId.trim().isEmpty()) {
					throw new CantUpdateException("Impossible de modifier la playlist");
				}
			}

			// Récupère la playlist en base
			YouTubeActivPlaylist playlist = ytMgr.getPlaylist(userId, playlistId);
			if (playlist == null) {
				// Ajoute la playlist en base
				ytMgr.addPlaylist(userId, playlistId, active);
			} else {
				// Modifie la playlist en base
				ytMgr.updatePlaylist(playlist, active);
			}

			// Ajoute le message de modification avec succès
			request.getSession().setAttribute("success", Messages.getProperty("success.yt.playlist.update", lang));

		} catch (FonctionnalException fex) {
			if (fex instanceof TitleException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.title", lang));
			} else if (fex instanceof DescriptionException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.description", lang));
			} else {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.update", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Récupère la playlist selon son ID et essaye de la supprimer de YouTubeData et de la base de donnée
	 * 
	 * @param request
	 * @param userId
	 * @param playlistId
	 * @param lang
	 */
	private void deleteFunction(HttpServletRequest request, int userId, String playlistId, String lang) {
		try {
			// Récupère la playlist avant sa suppression
			YouTubeActivPlaylist playlist = ytMgr.getPlaylist(userId, playlistId);

			// Si la playlist n'est pas active
			if (playlist == null || !playlist.isActive()) {
				// Supprime la playlist YouTube Data de l'utilisateur OAuth
				boolean deleted = ytMgr.deletePlaylistYTDAta(playlistId);

				// Si la playlist YouTube Data est supprimée
				if (deleted) {
					// Supprime la playlist si besoin
					if (playlist != null) {
						ytMgr.deletePlaylist(playlist);
					}

					// Ajoute le message de suppression avec succès
					request.getSession().setAttribute("success", Messages.getProperty("success.yt.playlist.delete", lang));
				} else {
					throw new CantDeleteException("Impossible de supprimer la playlist");
				}
			} else {
				throw new CantDeleteActiveException("Impossible de supprimer la playlist active");
			}
		} catch (FonctionnalException fex) {
			if (fex instanceof CantDeleteActiveException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.delete.active", lang));
			} else {
				request.getSession().setAttribute("error", Messages.getProperty("error.yt.playlist.delete", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Récupère la liste des playlists YouTubeData de l'utilisateur OAuth et la liste des playlists en base (pour déterminer si activer) et renvoi
	 * vers la JSP Youtube Playlists avec message d'erreur si besoin
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param userId
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToPlaylistList(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, String currentPage,
			String pageToken, String orderBy, String dir, String lang) throws ServletException, IOException {
		List<YouTubePlaylist> listPlaylistYTData = new ArrayList<YouTubePlaylist>();
		List<YouTubeActivPlaylist> listActivPlaylist = new ArrayList<YouTubeActivPlaylist>();
		try {
			// Récupère la liste des playlists YouTube Data de l'utilisateur OAuth
			listPlaylistYTData = ytMgr.getPlaylistsYTData(pageToken);

			// Récupère la liste des playlists actives de l'utilisateur en base
			listActivPlaylist = ytMgr.getPlaylists(userId);

			// Met à jour l'activation des playlists
			listPlaylistYTData = ytMgr.checkActive(listPlaylistYTData, listActivPlaylist);

		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}

		// Ajoute les objets dans la view
		view.addAttributeToList(new ViewAttribut("current", currentPage));
		view.addAttributeToList(new ViewAttribut("listPlaylist", listPlaylistYTData));

		// Redirection
		redirectToYouTubePlaylistsJSP(request, response, view);
	}

	/**
	 * Redirige la requête vers la JSP Youtube Playlists
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToYouTubePlaylistsJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> YouTube Playlists JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/playlists.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP New Youtube Playlist
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNewYouTubePlaylistJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> New YouTube Playlist JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/playlist_new.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Update Youtube Playlist
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToUpdateYouTubePlaylistJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Update YouTube Playlist JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/playlist_update.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet avec action list en Get
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServletWithList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString() + "?action=list");
	}

	/**
	 * Redirige la requête vers cette même servlet avec action update en Get
	 * 
	 * @param request
	 * @param response
	 * @param playlistId
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServletWithUpdate(HttpServletRequest request, HttpServletResponse response, String playlistId)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString() + "?action=update&idPlaylist=" + playlistId);
	}

}
