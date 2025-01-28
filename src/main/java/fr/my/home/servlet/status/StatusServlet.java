package fr.my.home.servlet.status;

import java.io.IOException;

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
 * Servlet qui prends en charge la gestion de la page de status (accessible sans utilisateur loggé)
 * 
 * @author Jonathan
 * @version 1.1
 * @since 20/01/2025
 */
@WebServlet("/status")
public class StatusServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(StatusServlet.class);
	private boolean databaseOnline = false;

	/**
	 * Redirection vers la page de status
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Status Servlet [GET] -->");

		// Actualise le status de la base de données
		databaseOnline = HibernateUtil.getInstance().testConnection();

		// Ajoute le status dans la requête
		request.setAttribute("databaseOnline", databaseOnline);

		// Redirection
		redirectToStatusJSP(request, response);
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
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToStatusJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Status JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/status/status.jsp");
		dispatcher.forward(request, response);
	}

}
