package fr.my.home.servlet.snake;

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
 * Servlet qui prends en charge la gestion du Snake
 * 
 * @author Jonathan
 * @version 1.0
 * @since 30/12/2024
 */
@WebServlet("/snake")
public class SnakeServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(SnakeServlet.class);

	// Attributes

	// Constructors

	/**
	 * Default Constructor
	 */
	public SnakeServlet() {
		super();
	}

	// Methods

	/**
	 * Redirection vers la JSP
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Snake Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Supprime l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Récupère l'ID de l'utilisateur en session
		// int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		// String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Redirection
		redirectToSnakeJSP(request, response, view);
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
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSnakeJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Snake JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/snake/snake.jsp");
		dispatcher.forward(request, response);
	}

}
