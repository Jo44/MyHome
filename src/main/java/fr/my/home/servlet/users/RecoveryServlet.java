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

import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.global.ReCaptchaException;
import fr.my.home.exception.users.EmailException;
import fr.my.home.exception.users.UnusedEmailException;
import fr.my.home.manager.UsersManager;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion de récupération des identifiants d'un utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/recovery")
public class RecoveryServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(RecoveryServlet.class);

	// Attributes

	private UsersManager usersMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public RecoveryServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	// Methods

	/**
	 * Redirection vers la page de récupération des identifiants de l'utilisateur
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Recovery Servlet [GET] -->");
		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Test de la connection à la base de données
		boolean databaseOnline = DatabaseAccess.getInstance().testConnection();
		view.addAttributeToList(new ViewAttribut("databaseOnline", databaseOnline));

		// Récupère la langue de l'utilisateur (pour messages error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère et ajoute dans la view l'attribut erreur si il existe et surcharge si problème de base de données
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		if (!databaseOnline) {
			error = Messages.getProperty("error.database", lang);
		}
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToRecoveryJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire de l'email de récupération
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Recovery Servlet [POST] -->");
		logger.info("Récupération des identifiants en cours ..");

		// Récupère les informations saisies dans le formulaire
		String email = request.getParameter("user_email").trim();
		String reCaptcha = request.getParameter("g-recaptcha-response");

		// Récupère la langue de l'utilisateur (pour mail et messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Test la validation du formulaire
		try {
			// Tentative de ré-initialisation de l'utilisateur via email/reCaptcha/lang
			usersMgr.checkRecovery(email.trim(), reCaptcha, lang);

			// Redirection
			redirectToRecoveryOKJSP(request, response);
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			if (fex instanceof EmailException) {
				// L'email n'est pas valide
				request.getSession().setAttribute("error", Messages.getProperty("error.user.email.invalid", lang));
			} else if (fex instanceof ReCaptchaException) {
				// Le reCaptcha n'est pas valide
				request.getSession().setAttribute("error", Messages.getProperty("error.missing.recaptcha", lang));
			} else if (fex instanceof UnusedEmailException) {
				// L'email n'est pas enregistré
				request.getSession().setAttribute("error", Messages.getProperty("error.user.email.unused", lang));
			}

			// Redirection GET
			doGet(request, response);
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));

			// Redirection GET
			doGet(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP Recovery
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToRecoveryJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Recovery JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/recovery.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Recovery OK
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToRecoveryOKJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> Recovery OK JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/recovery_ok.jsp");
		dispatcher.forward(request, response);
	}

}
