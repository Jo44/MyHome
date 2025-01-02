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

import fr.my.home.bean.User;
import fr.my.home.bean.YouTubeActivPlaylist;
import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion du player YouTube
 * 
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
@WebServlet("/youtube_player")
public class PlayerServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(PlayerServlet.class);

	// Attributes

	private YouTubeManager ytMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public PlayerServlet() {
		super();
		// Initialisation du manager
		ytMgr = new YouTubeManager();
	}

	// Methods

	/**
	 * Redirection vers la page du lecteur de la playlist
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Player Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère la liste des playlists actives de l'utilisateur et leurs vidéos
		getActivePlaylistsAndVideosFunction(view, userId, lang);

		// Redirection
		redirectToYouTubePlayerJSP(request, response, view);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Récupère la liste des playlists actives et leurs vidéos et les charge dans la view, et erreur si besoin
	 * 
	 * @param view
	 * @param userId
	 * @param lang
	 */
	private void getActivePlaylistsAndVideosFunction(ViewJSP view, int userId, String lang) {
		List<YouTubePlaylist> listPlaylist = new ArrayList<YouTubePlaylist>();
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		try {
			// Récupère la liste des playlists YouTube Data de l'utilisateur OAuth (les 10 premières max)
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
			List<YouTubeActivPlaylist> listActivPlaylist = ytMgr.getPlaylists(userId);

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
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}

		// Charge la liste des playlists actives dans la view
		view.addAttributeToList(new ViewAttribut("listPlaylist", listPlaylist));
		// Charge la liste des vidéos des playlists actives dans la view
		view.addAttributeToList(new ViewAttribut("listVideo", listVideo));
	}

	/**
	 * Redirige la requête vers la JSP Youtube Player
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToYouTubePlayerJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> YouTube Player JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/player.jsp");
		dispatcher.forward(request, response);
	}

}
