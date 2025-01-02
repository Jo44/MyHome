package fr.my.home.servlet.help;

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
import fr.my.home.exception.global.ReCaptchaException;
import fr.my.home.exception.help.MessageException;
import fr.my.home.manager.HelpManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion de la demande d'aide à l'administrateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/help")
public class HelpServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(HelpServlet.class);

	// Attributes

	private HelpManager helpMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public HelpServlet() {
		super();
		// Initialisation du manager
		helpMgr = new HelpManager();
	}

	// Methods

	/**
	 * Redirection vers la page d'aide
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Help Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToHelpJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire de demande d'aide à l'administrateur
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Help Servlet [POST] -->");
		logger.info("Tentative d'envoi du formulaire d'aide ..");
		boolean sent = false;

		// Récupère l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		String message = new String(request.getParameter("message").trim().getBytes("ISO-8859-1"), "UTF-8");
		String reCaptcha = request.getParameter("g-recaptcha-response");

		try {
			// Envoi le message à l'administrateur
			helpMgr.sendHelpMessage(user, message, reCaptcha);

			// Met à jour la variable de confirmation d'envoi
			sent = true;
		} catch (FonctionnalException fex) {
			sent = false;
			if (fex instanceof MessageException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.help.message", lang));
			} else if (fex instanceof ReCaptchaException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.missing.recaptcha", lang));
			}
		} finally {
			// En fonction de l'état de l'envoi du message
			if (sent) {
				// Redirection vers la JSP de confirmation
				redirectToConfirmHelpJSP(request, response, new ViewJSP());
			} else {
				// Redirection vers la servlet en GET
				redirectToThisServletAfterPost(request, response);
			}
		}

	}

	/**
	 * Redirige la requête vers la JSP Help
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToHelpJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Help JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/help/help.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Confirm Help
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToConfirmHelpJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Confirm Help JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/help/help_ok.jsp");
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
	private void redirectToThisServletAfterPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString());
	}

}
