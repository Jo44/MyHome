package fr.my.home.servlet.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;
import fr.my.home.manager.UsersManager;

/**
 * Servlet en charge de la déconnexion de l'utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/disconnect")
public class SignOutServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(SignOutServlet.class);

	// Attributes

	private UsersManager usersMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public SignOutServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	// Methods

	/**
	 * Déconnexion et retour login
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sign Out Servlet [GET] -->");

		// Récupère l'utilisateur
		User user = (User) request.getSession().getAttribute("user");

		// Si utilisateur connecté
		if (user != null) {
			logger.debug("Déconnection de l'utilisateur : " + user.getName());

			// Supprime le cookie et met à jour l'utilisateur en base
			usersMgr.signOut(request, response, user);

			// Supprime l'utilisateur de la session
			request.getSession().removeAttribute("user");

			logger.debug("Déconnexion réussie");
		}

		// Ferme la session
		if (request.getSession(false) != null) {
			request.getSession(false).invalidate();
		}

		// Redirection vers la page de login
		redirectToSignIn(request, response);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la servlet Sign In
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSignIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Récupère la base de l'url pour redirection page de connexion
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		String base = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
		response.sendRedirect(base);
	}

}
