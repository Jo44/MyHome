package fr.my.home.servlet.home;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la gestion de l'accueil
 * 
 * @author Jonathan
 * @version 3.0
 * @since 15/01/2025
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(HomeServlet.class);

	/**
	 * Constructeur
	 */
	public HomeServlet() {
		super();
	}

	/**
	 * Redirection vers la page d'accueil
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Home Servlet [GET] -->");

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Redirection
		redirectToHomeJSP(request, response);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Home
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToHomeJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Home JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/home/home.jsp");
		dispatcher.forward(request, response);
	}

}
