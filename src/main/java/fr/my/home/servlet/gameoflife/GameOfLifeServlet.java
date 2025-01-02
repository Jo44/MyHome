package fr.my.home.servlet.gameoflife;

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
 * Servlet qui prends en charge la gestion du jeu de la vie
 * 
 * @author Jonathan
 * @version 1.1
 * @since 30/12/2024
 */
@WebServlet("/game_of_life")
public class GameOfLifeServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(GameOfLifeServlet.class);

	// Attributes

	// Constructors

	/**
	 * Default Constructor
	 */
	public GameOfLifeServlet() {
		super();
	}

	// Methods

	/**
	 * Redirection vers la JSP
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Game of Life Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Récupère l'ID de l'utilisateur en session
		// int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		// String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Redirection
		redirectToGameOfLifeJSP(request, response, view);
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
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToGameOfLifeJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Game of Life JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/gameoflife/gameoflife.jsp");
		dispatcher.forward(request, response);
	}

}
