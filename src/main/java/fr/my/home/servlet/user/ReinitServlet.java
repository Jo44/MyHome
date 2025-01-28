package fr.my.home.servlet.user;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.users.NewPassException;
import fr.my.home.exception.users.NoMatchPassException;
import fr.my.home.exception.users.PassSizeException;
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
 * Servlet qui prends en charge la gestion de modification du mot de passe d'un utilisateur
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/reinit")
public class ReinitServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ReinitServlet.class);
	private UsersManager usersMgr;

	/**
	 * Constructeur
	 */
	public ReinitServlet() {
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
		logger.info("--> Reinit Servlet [GET] -->");

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

		// Récupère le paramètre token et le charge dans la requête
		String reInitToken = request.getParameter("token");
		request.setAttribute("token", reInitToken);

		// Redirection
		redirectToReInitPassJSP(request, response);
	}

	/**
	 * Traitement du formulaire de modification du mot de passe
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Reinit Servlet [POST] -->");
		logger.info("Reinitialisation en cours ..");

		// Récupère les informations saisies dans le formulaire
		String newPassword = new String(request.getParameter("newpassword").trim().getBytes("ISO-8859-1"), "UTF-8");
		String newPasswordConfirm = new String(request.getParameter("newpassword-confirm").trim().getBytes("ISO-8859-1"), "UTF-8");
		String reInitToken = request.getParameter("token");

		// Récupère la langue de l'utilisateur (pour mail et messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Test la validation du formulaire
		try {
			// Vérification des paramètres du formulaire de ré-initialisation et renvoi le hashPass si tout est ok
			String hashPass = usersMgr.checkParamsReInit(newPassword, newPasswordConfirm, reInitToken);

			try {
				// Tentative de modification du mot de passe de l'utilisateur
				usersMgr.checkReInit(hashPass, reInitToken);

				// Redirection OK
				redirectToReInitPassOKJSP(request, response);
			} catch (FonctionnalException fex) {
				logger.debug(fex.getMessage());

				// Redirection Error
				redirectToTryLaterJSP(request, response);
			}
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			if (fex instanceof PassSizeException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.pass.size", lang));
			} else if (fex instanceof NoMatchPassException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.pass.match", lang));
			} else if (fex instanceof NewPassException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.user.new.pass", lang));
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
	 * Redirige la requête vers la JSP ReInit Pass
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToReInitPassJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> ReInit Pass JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/reinit_pass.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP ReInit Pass OK
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToReInitPassOKJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> ReInit Pass OK JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/reinit_pass_ok.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP ReInit Try Later
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToTryLaterJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> ReInit Try Later JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/reinit_try_later.jsp");
		dispatcher.forward(request, response);
	}

}
