package fr.my.home.servlet.users;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.manager.UsersManager;

/**
 * Servlet qui prends en charge la validation de la création d'un utilisateur via retour de token de validation ou redirection vers la page d'erreur
 * si validation impossible
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/validation")
public class ValidationServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ValidationServlet.class);

	// Constructors

	/**
	 * Default Constructor
	 */
	public ValidationServlet() {
		super();
	}

	// Methods

	/**
	 * Valide la création de l'utilisateur via son token de validation
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Validation Servlet [GET] -->");

		// Récupère le paramètre token
		String validationToken = request.getParameter("token");

		// Si un token de validation est présent
		if (validationToken != null && validationToken.trim().length() > 0) {
			// Charge le Manager
			UsersManager usersMgr = new UsersManager();

			// Essaye de valider l'utilisateur grâce au token de validation
			boolean valid = usersMgr.validateUser(validationToken);

			if (valid) {
				// Redirection vers la page de confirmation de validation
				redirectToValidationOk(request, response);
			} else {
				// Redirection vers la page d'erreur d'activation
				redirectToValidationTryLater(request, response);
			}
		} else {
			// Redirection vers la page de login
			redirectToSignIn(request, response);
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
	 * Redirige la requête vers la servlet Login
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSignIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/check");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Validation OK
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToValidationOk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> Validation OK JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/register/validation_ok.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Validation Try Later
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToValidationTryLater(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> Validation Try Later JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/register/validation_try_later.jsp");
		dispatcher.forward(request, response);
	}

}
