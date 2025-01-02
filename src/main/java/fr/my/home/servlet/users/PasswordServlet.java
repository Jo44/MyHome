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

import fr.my.home.bean.User;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.users.InvalidUserException;
import fr.my.home.exception.users.NewPassException;
import fr.my.home.exception.users.NoMatchPassException;
import fr.my.home.exception.users.OldPassException;
import fr.my.home.exception.users.PassSizeException;
import fr.my.home.manager.UsersManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la modification du mot de passe de l'utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/change")
public class PasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(PasswordServlet.class);

	// Attributes

	private UsersManager usersMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public PasswordServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	// Methods

	/**
	 * Redirection vers la page de modification du mot de passe de l'utilisateur
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Password Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToPasswordJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire de modification du mot de passe de l'utilisateur
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Password Servlet [POST] -->");
		logger.info("Tentative de modification du mot de passe de l'utilisateur en cours ..");

		// Récupère l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		String oldPassword = new String(request.getParameter("oldpassword").trim().getBytes("ISO-8859-1"), "UTF-8");
		String newPassword = new String(request.getParameter("newpassword").trim().getBytes("ISO-8859-1"), "UTF-8");
		String newPasswordConfirm = new String(request.getParameter("newpassword-confirm").trim().getBytes("ISO-8859-1"), "UTF-8");

		try {
			// Vérifie si la modification du mot de passe est possible fonctionnellement,
			// renvoi newHash si possible, sinon exception fonctionnelle
			String newHash = usersMgr.checkUpdatePassword(user, oldPassword, newPassword, newPasswordConfirm);

			// Met à jour l'utilisateur local
			user.setPass(newHash);

			// Enregistre l'utilisateur en base
			usersMgr.updatePassword(user);

			// Met à jour l'utilisateur de session
			request.getSession().removeAttribute("user");
			request.getSession().setAttribute("user", user);

			// Ajoute le message de succès à la session
			request.getSession().setAttribute("success", Messages.getProperty("success.settings.password", lang));

			// Redirection vers la servlet Settings
			redirectToSettingsServlet(request, response);
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			if (fex instanceof InvalidUserException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.invalid", lang));
			} else if (fex instanceof PassSizeException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.pass.size", lang));
			} else if (fex instanceof NoMatchPassException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.pass.match", lang));
			} else if (fex instanceof NewPassException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.new.pass", lang));
			} else if (fex instanceof OldPassException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.old.pass", lang));
			}
			// Redirige vers la page de modification
			redirectToThisServlet(request, response);
		} catch (TechnicalException tex) {
			logger.debug(tex.getMessage());
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
			// Redirige vers la page de modification
			redirectToThisServlet(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP Password
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToPasswordJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Password JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/settings/change_password.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la servlet Settings
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSettingsServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/settings");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet en Get après un Post
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString());
	}

}
