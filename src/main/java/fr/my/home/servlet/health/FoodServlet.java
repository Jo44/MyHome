package fr.my.home.servlet.health;

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
 * Servlet qui prends en charge la gestion de la nourriture
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/health/food")
public class FoodServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(FoodServlet.class);

	/**
	 * Constructeur
	 */
	public FoodServlet() {
		super();
	}

	/**
	 * Redirection vers la page de nourriture
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Food Servlet [GET] -->");

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		request.setAttribute("error", error);

		// Récupère l'attribut success si il existe
		String success = (String) request.getSession().getAttribute("success");
		request.getSession().removeAttribute("success");
		request.setAttribute("success", success);

		// Redirect to JSP
		redirectToFoodJSP(request, response);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Food
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToFoodJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Food JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/health/food.jsp");
		dispatcher.forward(request, response);
	}

}
