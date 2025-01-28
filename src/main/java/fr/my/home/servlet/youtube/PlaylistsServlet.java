package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.services.youtube.model.Playlist;

import fr.my.home.bean.ActivePlaylist;
import fr.my.home.bean.User;
import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.dao.implementation.UserDAO;
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
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la gestion des playlists YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/youtube_playlists")
public class PlaylistsServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(PlaylistsServlet.class);
	private YouTubeManager ytMgr;

	/**
	 * Constructeur
	 */
	public PlaylistsServlet() {
		super();
	}

	/**
	 * Redirection vers la liste, l'ajout, la modification ou la suppression de playlist
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Playlists Servlet [GET] -->");
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
						redirectToPlaylistList(request, response, userId, currentPage, pageToken, orderBy, dir, lang);
						break;
					// Si action ajouter
					case "add":
						// Redirection vers la JSP d'ajout
						redirectToNewYouTubePlaylistJSP(request, response);
						break;
					// Si action modifier
					case "update":
						// Récupère la playlist
						if (getOneFunction(request, userId, youtubeId, currentPage, pageToken, orderBy, dir, lang)) {
							// Redirige vers la JSP de modification
							redirectToUpdateYouTubePlaylistJSP(request, response);
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
		} else {
			// Redirection vers la page de connexion OAuth 2.0
			redirectToLogInServlet(request, response, user);
		}
	}

	/**
	 * Traitement d'ajout / modification d'un formulaire d'une playlist
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Playlists Servlet [POST] -->");
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
		} else {
			// Redirection vers la page de connexion OAuth 2.0
			redirectToLogInServlet(request, response, user);
		}
	}

	/**
	 * Récupère la playlist de l'utilisateur, renvoi un boolean si elle existe et la charge dans la requête, ou erreur si besoin
	 * 
	 * @param request
	 * @param userId
	 * @param playlistId
	 * @param currentPage
	 * @param pageToken
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @return boolean
	 */
	private boolean getOneFunction(HttpServletRequest request, int userId, String playlistId, String currentPage, String pageToken, String orderBy,
			String dir, String lang) {
		boolean exist = false;
		YouTubePlaylist playlist = null;
		List<YouTubeVideo> listVideo = null;
		ActivePlaylist activePlaylist = null;

		try {
			// Récupère la playlist YouTube Data de l'utilisateur OAuth selon la playlist ID
			playlist = ytMgr.getPlaylistYTData(playlistId);

			// La playlist existe
			if (playlist != null) {
				// Récupère la liste des vidéos YouTube Data de l'utilisateur OAuth selon la playlist ID et le token de page
				listVideo = ytMgr.getVideosYTData(playlistId, pageToken);

				// Récupère la playlist de l'utilisateur
				activePlaylist = ytMgr.getPlaylist(userId, playlistId);

				// Met à jour l'activation de la playlist
				if (activePlaylist != null && activePlaylist.isActive()) {
					playlist.setActive(true);
				}

				// Prépare les attributs de la JSP
				setupAttributes(request, playlist, currentPage, listVideo);
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
			ActivePlaylist playlist = ytMgr.getPlaylist(userId, playlistId);
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
			ActivePlaylist playlist = ytMgr.getPlaylist(userId, playlistId);

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
	 * @param userId
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToPlaylistList(HttpServletRequest request, HttpServletResponse response, int userId, String currentPage, String pageToken,
			String orderBy, String dir, String lang) throws ServletException, IOException {
		String channelName = "???";
		List<YouTubePlaylist> listPlaylistYTData = new ArrayList<YouTubePlaylist>();
		List<ActivePlaylist> listActivPlaylist = new ArrayList<ActivePlaylist>();
		try {
			// Récupère le nom de l'utilisateur YouTube
			channelName = ytMgr.getChannelNameYTData();

			// Récupère la liste des playlists YouTube Data de l'utilisateur OAuth
			listPlaylistYTData = ytMgr.getPlaylistsYTData(pageToken);

			// Récupère la liste des playlists actives de l'utilisateur en base
			listActivPlaylist = ytMgr.getPlaylists(userId);

			// Met à jour l'activation des playlists
			listPlaylistYTData = ytMgr.checkActive(listPlaylistYTData, listActivPlaylist);

		} catch (TechnicalException tex) {
			request.setAttribute("error", Messages.getProperty("error.database", lang));
		}

		// Prépare la pagination
		List<Object> listObject = new ArrayList<Object>(listPlaylistYTData);
		setupPaging(request, currentPage, listObject);

		// Ajoute les attributs à la requête
		request.setAttribute("channelName", channelName);
		request.setAttribute("listPlaylist", listPlaylistYTData);
		request.setAttribute("formatterDate", new SimpleDateFormat("dd/MM/yyyy - HH:mm"));

		// Redirection
		redirectToYouTubePlaylistsJSP(request, response);
	}

	/**
	 * Prépare les attributs envoyés à la JSP
	 * 
	 * @param request
	 * @param playlist
	 * @param currentPage
	 * @param listVideo
	 */
	private void setupAttributes(HttpServletRequest request, YouTubePlaylist playlist, String currentPage, List<YouTubeVideo> listVideo) {
		// Prépare la pagination
		List<Object> listObject = new ArrayList<Object>(listVideo);
		setupPaging(request, currentPage, listObject);
		// Ajoute les attributs à la requête
		request.setAttribute("playlist", playlist);
		request.setAttribute("listVideo", listVideo);
	}

	/**
	 * Prépare la pagination
	 * 
	 * @param request
	 * @param currentPageStr
	 * @param listObject
	 */
	private void setupPaging(HttpServletRequest request, String currentPageStr, List<Object> listObject) {
		// Prépare la pagination
		String prevPageToken = "";
		String nextPageToken = "";
		String displayPagination = "";
		int currentPage = (currentPageStr == null || currentPageStr.isEmpty()) ? 1 : Integer.parseInt(currentPageStr);
		int pageResults = 0;
		if (listObject != null && listObject.size() > 0) {
			Object firstElement = listObject.get(0);
			if (firstElement instanceof YouTubeVideo) {
				YouTubeVideo video = (YouTubeVideo) firstElement;
				if (video.getPrevPageToken() != null && !video.getPrevPageToken().isEmpty()) {
					prevPageToken = video.getPrevPageToken();
				}
				if (video.getNextPageToken() != null && !video.getNextPageToken().isEmpty()) {
					nextPageToken = video.getNextPageToken();
				}
			} else if (firstElement instanceof YouTubePlaylist) {
				YouTubePlaylist playlist = (YouTubePlaylist) firstElement;
				if (playlist.getPrevPageToken() != null && !playlist.getPrevPageToken().isEmpty()) {
					prevPageToken = playlist.getPrevPageToken();
				}
				if (playlist.getNextPageToken() != null && !playlist.getNextPageToken().isEmpty()) {
					nextPageToken = playlist.getNextPageToken();
				}
			}
			pageResults = listObject.size();
			if (listObject.size() > 1) {
				displayPagination = String.valueOf(((currentPage - 1) * 10) + 1) + " - " + String.valueOf(((currentPage - 1) * 10) + pageResults);
			}
		}
		// Ajoute les attributs à la requête
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("displayPagination", displayPagination);
		request.setAttribute("prevPageToken", prevPageToken);
		request.setAttribute("nextPageToken", nextPageToken);
	}

	/**
	 * Redirige la requête vers la JSP Youtube Playlists
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToYouTubePlaylistsJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNewYouTubePlaylistJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToUpdateYouTubePlaylistJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
