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
import fr.my.home.exception.global.ReCaptchaException;
import fr.my.home.exception.users.EmailException;
import fr.my.home.exception.users.LoginSizeException;
import fr.my.home.exception.users.NewPassException;
import fr.my.home.exception.users.NoMatchPassException;
import fr.my.home.exception.users.PassSizeException;
import fr.my.home.exception.users.UsedEmailException;
import fr.my.home.exception.users.UsedLoginException;
import fr.my.home.manager.UsersManager;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion de l'inscription d'un nouvel utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(RegisterServlet.class);

	// Attributes

	private UsersManager usersMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public RegisterServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	// Methods

	/**
	 * Redirection vers la page d'inscription
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Register Servlet [GET] -->");

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
		redirectToRegisterJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire d'inscription
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Register Servlet [POST] -->");
		logger.info("Inscription en cours ..");

		// Récupère les informations saisies dans le formulaire
		String username = new String(request.getParameter("user_name").trim().getBytes("ISO-8859-1"), "UTF-8");
		String password = new String(request.getParameter("user_pass").trim().getBytes("ISO-8859-1"), "UTF-8");
		String confirmPassword = new String(request.getParameter("user_confirm_pass").trim().getBytes("ISO-8859-1"), "UTF-8");
		String email = request.getParameter("user_email").trim();
		String reCaptcha = request.getParameter("g-recaptcha-response");

		// Récupère la langue de l'utilisateur (pour mail et messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Test la validation du formulaire
		try {
			// Tentative d'inscription de l'utilisateur via login/password/confirm_password/email/reCaptcha/lang
			User user = usersMgr.checkSignUp(username, password, confirmPassword, email, reCaptcha, lang);
			logger.info("Utilisateur < " + user.getName() + " > inscrit");

			// Redirection vers la page de confirmation d'inscription
			redirectToRegisterOKJSP(request, response);
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			if (fex instanceof LoginSizeException) {
				// Le login doit contenir entre 3 et 30 caractères
				request.getSession().setAttribute("error", Messages.getProperty("error.user.login.size", lang));
			} else if (fex instanceof PassSizeException) {
				// Les mots de passe doivent contenir entre 6 et 30 caractères
				request.getSession().setAttribute("error", Messages.getProperty("error.user.pass.size", lang));
			} else if (fex instanceof NoMatchPassException) {
				// La confirmation du nouveau mot de passe n'est pas identique
				request.getSession().setAttribute("error", Messages.getProperty("error.user.pass.match", lang));
			} else if (fex instanceof NewPassException) {
				// Le mot de passe n'est pas valide
				request.getSession().setAttribute("error", Messages.getProperty("error.user.pass.invalid", lang));
			} else if (fex instanceof EmailException) {
				// L'email n'est pas valide
				request.getSession().setAttribute("error", Messages.getProperty("error.user.email.invalid", lang));
			} else if (fex instanceof ReCaptchaException) {
				// Le reCaptcha n'est pas valide
				request.getSession().setAttribute("error", Messages.getProperty("error.missing.recaptcha", lang));
			} else if (fex instanceof UsedLoginException) {
				// Le login est déjà utilisé
				request.getSession().setAttribute("error", Messages.getProperty("error.user.login.used", lang));
			} else if (fex instanceof UsedEmailException) {
				// L'email est déjà utilisé
				request.getSession().setAttribute("error", Messages.getProperty("error.user.email.used", lang));
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
	 * Redirige la requête vers la JSP Register
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToRegisterJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Register JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/register/register.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Confirmation
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToRegisterOKJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Register OK JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/register/register_ok.jsp");
		dispatcher.forward(request, response);
	}

}
