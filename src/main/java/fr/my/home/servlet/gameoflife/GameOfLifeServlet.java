package fr.my.home.servlet.gameoflife;

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
 * Servlet qui prends en charge la gestion du jeu de la vie
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/game_of_life")
public class GameOfLifeServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(GameOfLifeServlet.class);

	/**
	 * Constructeur
	 */
	public GameOfLifeServlet() {
		super();
	}

	/**
	 * Redirection vers la JSP
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Game of Life Servlet [GET] -->");

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Redirection
		redirectToGameOfLifeJSP(request, response);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Game of Life
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToGameOfLifeJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Game of Life JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/gameoflife/gameoflife.jsp");
		dispatcher.forward(request, response);
	}

}
