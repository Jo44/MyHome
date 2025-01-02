package fr.my.home.servlet.status;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.tool.DatabaseAccess;

/*
 * Cette servlet est chargée au lancement du serveur d'application web (Tomcat/etc). Lors de son initialisation, elle instancie la classe
 * DatabaseAccess permettant de charger la configuration Hibernate. En fonction de l'état d'Hibernate, elle valorise la donnée 'databaseOnline' qui
 * est renvoyé à la page de config si demandée
 */

/**
 * Servlet qui charge Hibernate et prends en charge la gestion de la page de status (accessible sans utilisateur loggé)
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet(urlPatterns = "/status", loadOnStartup = 1)
public class StatusServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(StatusServlet.class);

	// Attributes

	private boolean databaseOnline = false;

	// Methods

	/**
	 * Initialisation de la servlet (charge Hibernate au lancement du serveur d'application web (aka TomCat))
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			databaseOnline = DatabaseAccess.getInstance().testConnection();
			logger.info("Hibernate est correctement configuré et chargé.");
		} catch (HibernateException he) {
			databaseOnline = false;
			logger.error("Problème lors de l'initialisation d'Hibernate !!");
			logger.error("Base de données offline ?? hibernate.cfg.xml correct ??");
		}
	}

	/**
	 * Redirection vers la page de status
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Status Servlet [GET] -->");

		// Réinitialise la servlet pour actualiser le status de la base de données
		init();

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut databaseOnline
		view.addAttributeToList(new ViewAttribut("databaseOnline", databaseOnline));

		// Redirection
		redirectToStatusJSP(request, response, view);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Status
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToStatusJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Status JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/status/status.jsp");
		dispatcher.forward(request, response);
	}

}
