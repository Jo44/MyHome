package fr.my.home.servlet.status;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.tool.HibernateUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la gestion des status (au sein de l'application)
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/status_inside")
public class StatusInsideServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(StatusInsideServlet.class);

	/**
	 * Constructeur
	 */
	public StatusInsideServlet() {
		super();
	}

	/**
	 * Redirection vers la page du status (inside)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Status Inside Servlet [GET] -->");

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Récupère l'heure de début de session
		String sessionStart = "";
		if (request.getSession() != null) {
			Timestamp creationTime = new Timestamp(request.getSession().getCreationTime());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
			sessionStart = formatter.format(creationTime);
		}
		request.setAttribute("sessionStart", sessionStart);

		// Récupère le status de la base de données
		boolean databaseOnline = HibernateUtil.getInstance().testConnection();
		request.setAttribute("databaseOnline", databaseOnline);

		// Redirection
		redirectToStatusInsideJSP(request, response);
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
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToStatusInsideJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Status (Inside) JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/status/status_inside.jsp");
		dispatcher.forward(request, response);
	}

}
