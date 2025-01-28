package fr.my.home.servlet.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;

import fr.my.home.bean.User;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.properties.Settings;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui vérifie la réponse de l'authentification OAuth Google
 * 
 * @author Jonathan
 * @version 1.0
 * @since 20/01/2025
 */
@WebServlet("/oauth2callback")
public class CallbackServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(CallbackServlet.class);
	private static final String CLIENT_ID = Settings.getStringProperty("oauth.client.id");
	private static final String CLIENT_SECRET = Settings.getStringProperty("oauth.client.secret");
	private static final String REDIRECT_URI = Settings.getStringProperty("oauth.client.redirect");

	/**
	 * Constructeur
	 */
	public CallbackServlet() {
		super();
	}

	/**
	 * Traitement de la réponse de l'authentification OAuth Google
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Callback Servlet [GET] -->");

		// Récupération des paramètres de la réponse
		String code = request.getParameter("code");
		String stateReceived = request.getParameter("state");

		// Récupération des paramètres de la session
		User user = (User) request.getSession().getAttribute("user");
		String stateStored = (String) request.getSession().getAttribute("state");

		try {
			// Vérification des paramètres
			if (code == null || code.trim().isEmpty() || stateReceived == null || stateReceived.trim().isEmpty() || stateStored == null
					|| !stateReceived.equals(stateStored)) {
				throw new TechnicalException("La réponse de l'authentification OAuth Google n'est pas valide");
			}
			// Récupération des tokens à partir du code
			Map<String, Object> tokenResponse = exchangeCodeForTokens(code);
			String accessToken = (String) tokenResponse.get("access_token");
			String refreshToken = (String) tokenResponse.get("refresh_token");
			Long expiresIn = ((Number) tokenResponse.get("expires_in")).longValue();
			Instant expiryDate = Instant.now().plus(expiresIn, ChronoUnit.SECONDS);
			// Vérification des tokens
			if (accessToken == null || accessToken.trim().isEmpty() || refreshToken == null || refreshToken.trim().isEmpty() || expiryDate == null) {
				throw new TechnicalException("Les tokens OAuth fournis ne sont pas valides");
			}
			// Met à jour l'utilisateur
			user.setAccessToken(refreshToken);
			new UserDAO().update(user);
			request.getSession().setAttribute("user", user);

			// Met à jour les informations d'identification dans la session
			request.getSession().setAttribute("oauth_access_token", accessToken);
			request.getSession().setAttribute("oauth_expiryDate", expiryDate);

			// Redirection vers YouTube Player
			response.sendRedirect(request.getContextPath() + "/youtube_player");

		} catch (Exception ex) {
			logger.error(ex.getMessage());
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
	 * Récupération des tokens à partir du code
	 * 
	 * @param code
	 * @return Map<String, Object>
	 * @throws TechnicalException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> exchangeCodeForTokens(String code) throws TechnicalException {
		try {
			// Préparation de la requête POST
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost("https://oauth2.googleapis.com/token");
			String requestBody = String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code", code, CLIENT_ID,
					CLIENT_SECRET, URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8));
			post.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// Lecture de la réponse JSON
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.execute(post).getEntity().getContent()));
			String jsonResponse = reader.lines().collect(Collectors.joining("\n"));
			JsonObjectParser parser = new JsonObjectParser(GsonFactory.getDefaultInstance());
			return parser.parseAndClose(new InputStreamReader(new StringEntity(jsonResponse).getContent()), Map.class);
		} catch (IOException ioex) {
			throw new TechnicalException("Impossible de récupérer les tokens à partir du code");
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
		response.sendRedirect(request.getContextPath() + "/youtube");
	}

}
