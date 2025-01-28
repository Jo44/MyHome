package fr.my.home.servlet.help;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.global.ReCaptchaException;
import fr.my.home.exception.help.MessageException;
import fr.my.home.manager.HelpManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la gestion de la demande d'aide à l'administrateur
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/help")
public class HelpServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(HelpServlet.class);
	private HelpManager helpMgr;

	/**
	 * Constructeur
	 */
	public HelpServlet() {
		super();
		// Initialisation du manager
		helpMgr = new HelpManager();
	}

	/**
	 * Redirection vers la page d'aide
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Help Servlet [GET] -->");

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		request.setAttribute("error", error);

		// Redirection
		redirectToHelpJSP(request, response);
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
				redirectToConfirmHelpJSP(request, response);
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
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToHelpJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToConfirmHelpJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
