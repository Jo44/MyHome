package fr.my.home.servlet.status;

import java.io.IOException;
import java.sql.Timestamp;

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
import fr.my.home.tool.DatabaseAccess;

/**
 * Servlet qui prends en charge la gestion des status (au sein de l'application)
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/status_inside")
public class StatusInsideServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(StatusInsideServlet.class);

	// Constructors

	/**
	 * Default Constructor
	 */
	public StatusInsideServlet() {
		super();
	}

	// Methods

	/**
	 * Redirection vers la page du status (inside)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Status Inside Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Récupère l'heure de début de session
		Timestamp sessionStart = null;
		if (request.getSession() != null) {
			sessionStart = new Timestamp(request.getSession().getCreationTime());
		}
		view.addAttributeToList(new ViewAttribut("sessionStart", sessionStart));

		// Récupère le status de la base de données
		boolean databaseOnline = DatabaseAccess.getInstance().testConnection();
		view.addAttributeToList(new ViewAttribut("databaseOnline", databaseOnline));

		// Redirection
		redirectToStatusInsideJSP(request, response, view);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Status (Inside)
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToStatusInsideJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Status (Inside) JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/status/status_inside.jsp");
		dispatcher.forward(request, response);
	}

}
