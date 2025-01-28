package fr.my.home.servlet.snake;

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
 * Servlet qui prends en charge la gestion du Snake
 * 
 * @author Jonathan
 * @version 1.1
 * @since 151/01/2025
 */
@WebServlet("/snake")
public class SnakeServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(SnakeServlet.class);

	/**
	 * Constructeur
	 */
	public SnakeServlet() {
		super();
	}

	/**
	 * Redirection vers la JSP
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Snake Servlet [GET] -->");

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Redirection
		redirectToSnakeJSP(request, response);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Snake
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSnakeJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Snake JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/snake/snake.jsp");
		dispatcher.forward(request, response);
	}

}
