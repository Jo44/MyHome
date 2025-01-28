package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Settings;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui initie la connexion de l'utilisateur via OAuth Google
 * 
 * @author Jonathan
 * @version 1.0
 * @since 20/01/2025
 */
@WebServlet("/youtube")
public class LogInServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(LogInServlet.class);
	private static final String CLIENT_ID = Settings.getStringProperty("oauth.client.id");
	private static final String REDIRECT_URI = Settings.getStringProperty("oauth.client.redirect");

	/**
	 * Constructeur
	 */
	public LogInServlet() {
		super();
	}

	/**
	 * Redirection vers YouTube Player ou la page de connexion (selon état de l'authentification)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Log In Servlet [GET] -->");
		boolean authenticated = false;

		// Vérifie si l'utilisateur en session possède un token d'accès OAuth 2.0
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user.getAccessToken() != null && !user.getAccessToken().trim().isEmpty()) {
			try {
				// Initialisation du service YouTube
				YouTubeManager ytMgr = new YouTubeManager(request.getSession());
				if (ytMgr != null) {
					authenticated = true;
				}
				logger.info("Utilisateur identifié");
				logger.info("=> Redirection vers YouTube Player");
			} catch (Exception ex) {
				logger.error("Erreur d'initialisation du Service YouTube");
				logger.error(ex.getMessage());
				logger.info("=> Redirection vers YouTube Log In");
			}
		}

		// Selon état de l'authentification
		if (authenticated) {
			// Redirection vers YouTube Player
			response.sendRedirect(request.getContextPath() + "/youtube_player");
		} else {
			// Stock l'ID client dans la requête
			request.setAttribute("clientID", CLIENT_ID);

			// Stock l'URL de redirection OAuth (encoder en URL) dans la requête
			String redirectURI = URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8);
			request.setAttribute("redirectURI", redirectURI);

			// Stocke un token d'état OAuth dans la session (Protection CSRF)
			String state = GlobalTools.generateRandomString(100);
			request.getSession().setAttribute("state", state);

			// Redirection vers JSP
			redirectToYouTubeLogInJSP(request, response);
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
	 * Redirige la requête vers la JSP Youtube Log In
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToYouTubeLogInJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> YouTube Log In JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/log_in.jsp");
		dispatcher.forward(request, response);
	}

}
