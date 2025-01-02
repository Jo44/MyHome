package fr.my.home.servlet.location;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.api.ObjectIPAPI;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.manager.LocationManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion de la localisation d'une IP ou nom de domaine
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/location")
public class LocationServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(LocationServlet.class);

	// Attributes

	private LocationManager locMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public LocationServlet() {
		super();
		// Initialisation du manager
		locMgr = new LocationManager();
	}

	// Methods

	/**
	 * Redirection vers la page de localisation
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Location Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Récupère la recherche si elle existe
		String search = (String) request.getSession().getAttribute("search");
		request.getSession().removeAttribute("search");
		view.addAttributeToList(new ViewAttribut("search", search));

		// Récupère l'objet IPAPI si il existe
		ObjectIPAPI objectIPAPI = (ObjectIPAPI) request.getSession().getAttribute("objectIPAPI");
		request.getSession().removeAttribute("objectIPAPI");
		view.addAttributeToList(new ViewAttribut("objectIPAPI", objectIPAPI));

		// Redirection
		redirectToLocationJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire de localisation
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Location Servlet [POST] -->");
		logger.info("Traitement du formulaire de localisation d'une Ip ou d'un nom de domaine ..");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère le nom de domaine ou IP à localiser
		String input = request.getParameter("websiteIP");

		// Traitement du formulaire et renvoi de l'ObjectIPAPI dans la requête
		ObjectIPAPI objectIPAPI = null;
		String search = "";
		try {
			// Formatte la recherche
			search = locMgr.formatLocString(input);
			// Récupère l'objet IPAPI en réponse de la recherche
			objectIPAPI = locMgr.getIPAPIFunction(search);
			if (!objectIPAPI.getStatus().equals("success")) {
				throw new FonctionnalException("ObjectIPAPI en échec");
			}
			// Si récupération succès, renvoi l'objet dans la requête
			request.getSession().setAttribute("objectIPAPI", objectIPAPI);
			// Renvoi également la recherche pour affichage sur la carte
			request.getSession().setAttribute("search", search);
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.location.target", lang));
		}
		// Redirection
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Location
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToLocationJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Location JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/location/location.jsp");
		dispatcher.forward(request, response);
	}

}
