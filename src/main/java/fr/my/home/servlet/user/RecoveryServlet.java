package fr.my.home.servlet.user;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.global.ReCaptchaException;
import fr.my.home.exception.users.EmailException;
import fr.my.home.exception.users.UnusedEmailException;
import fr.my.home.manager.UsersManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Messages;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la gestion de récupération des identifiants d'un utilisateur
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/recovery")
public class RecoveryServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(RecoveryServlet.class);
	private UsersManager usersMgr;

	/**
	 * Constructeur
	 */
	public RecoveryServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	/**
	 * Redirection vers la page de récupération des identifiants de l'utilisateur
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Recovery Servlet [GET] -->");

		// Test de la connection à la base de données
		boolean databaseOnline = HibernateUtil.getInstance().testConnection();
		request.setAttribute("databaseOnline", databaseOnline);

		// Récupère la langue de l'utilisateur (pour messages error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère et ajoute dans la requête l'attribut erreur si il existe et surcharge si problème de base de données
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		if (!databaseOnline) {
			error = Messages.getProperty("error.database", lang);
		}
		request.setAttribute("error", error);

		// Redirection
		redirectToRecoveryJSP(request, response);
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
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToRecoveryJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
