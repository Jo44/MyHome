package fr.my.home.servlet.health;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.jsp.ViewJSP;

/**
 * Servlet qui prends en charge la gestion de la santé
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/health")
public class HealthServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(HealthServlet.class);

	// Constructors

	/**
	 * Default Constructor
	 */
	public HealthServlet() {
		super();
	}

	// Methods

	/**
	 * Redirection vers la page de santé
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Health Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Supprime l'attribut success si il existe
		request.getSession().removeAttribute("success");

		// Redirect to JSP
		redirectToHealthJSP(request, response, view);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Health
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToHealthJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Health JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/health/health.jsp");
		dispatcher.forward(request, response);
	}

}
