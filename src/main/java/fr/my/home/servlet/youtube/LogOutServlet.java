package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;

import fr.my.home.bean.User;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la déconnexion de l'utilisateur via OAuth Google
 * 
 * @author Jonathan
 * @version 1.0
 * @since 20/01/2025
 */
@WebServlet("/youtube_logout")
public class LogOutServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(LogOutServlet.class);

	/**
	 * Constructeur
	 */
	public LogOutServlet() {
		super();
	}

	/**
	 * Déconnexion de l'utilisateur
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Log Out Servlet [GET] -->");

		// Récupère l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupération des informations d'identification
		String refreshToken = user.getAccessToken();
		String accessToken = (String) request.getSession().getAttribute("oauth_access_token");

		// Revoke les tokens si présents
		if ((refreshToken != null && refreshToken.trim().isEmpty()) | (accessToken != null && accessToken.trim().isEmpty())) {
			final String revokeUrl = "https://oauth2.googleapis.com/revoke";
			if (accessToken != null && accessToken.trim().isEmpty()) {
				// Revoke l'Access Token
				HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
				Map<String, String> params = new HashMap<>();
				params.put("token", refreshToken);
				HttpResponse googleResponse = requestFactory.buildPostRequest(new GenericUrl(revokeUrl), new UrlEncodedContent(params)).execute();
				if (googleResponse.getStatusCode() == 200) {
					logger.debug("Access token revoké");
				}
			}
			if (refreshToken != null && refreshToken.trim().isEmpty()) {
				// Revoke le Refresh Token
				HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
				Map<String, String> params = new HashMap<>();
				params.put("token", refreshToken);
				HttpResponse googleResponse = requestFactory.buildPostRequest(new GenericUrl(revokeUrl), new UrlEncodedContent(params)).execute();
				if (googleResponse.getStatusCode() == 200) {
					logger.debug("Refresh token revoké");
				}
			}
		}
		// Redirection vers la page de connexion OAuth 2.0
		redirectToLogInServlet(request, response, user);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
		response.sendRedirect(request.getContextPath() + "/youtube");
	}

}
