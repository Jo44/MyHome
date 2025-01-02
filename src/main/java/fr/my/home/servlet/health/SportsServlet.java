package fr.my.home.servlet.health;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Sport;
import fr.my.home.bean.User;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.health.sports.ActivityException;
import fr.my.home.exception.health.sports.CantDeleteException;
import fr.my.home.exception.health.sports.DateHourException;
import fr.my.home.exception.health.sports.NotExistException;
import fr.my.home.manager.SportsManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion des activités sportives
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/health/sports")
public class SportsServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(SportsServlet.class);

	// Attributes

	private SportsManager sportMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public SportsServlet() {
		super();
		// Initialisation du manager
		sportMgr = new SportsManager();
	}

	// Methods

	/**
	 * Redirection vers la liste et la suppression des activités sportives
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sport Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Récupère l'attribut success si il existe
		String success = (String) request.getSession().getAttribute("success");
		request.getSession().removeAttribute("success");
		view.addAttributeToList(new ViewAttribut("success", success));

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère le nombre de lignes max d'un tableau à afficher (pour traitement javascript)
		int maxRows = GlobalTools.getMaxRows(request);
		view.addAttributeToList(new ViewAttribut("maxRows", maxRows));

		// Récupère la date / heure actuelle en string et la charge dans la view
		LocalDateTime now = LocalDateTime.now();
		String today = GlobalTools.formatDateToString(now);
		view.addAttributeToList(new ViewAttribut("today", today));

		// Récupère les paramètres de la requête
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		int sportId;
		try {
			sportId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			sportId = 0;
		}
		// Récupère les dates de la période voulue et les charge dans la view
		Timestamp from = Timestamp.valueOf(now);
		Timestamp to = Timestamp.valueOf(now);
		try {
			from.setTime(Long.parseLong(request.getParameter("from")));

		} catch (NumberFormatException nfe) {
			from = Timestamp.valueOf(now.minusMonths(3L));
		}
		try {
			to.setTime(Long.parseLong(request.getParameter("to")));
		} catch (NumberFormatException nfe) {
			to = Timestamp.valueOf(now);
		}
		view.addAttributeToList(new ViewAttribut("from", GlobalTools.formatDateToString(from.toLocalDateTime())));
		view.addAttributeToList(new ViewAttribut("to", GlobalTools.formatDateToString(to.toLocalDateTime())));
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action supprimer
				case "delete":
					// Essaye de supprimer l'activité sportive
					deleteFunction(request, sportId, userId, lang);

					// Puis renvoi à la liste des activités sportives
					redirectToThisServletAfterPostOrDelete(request, response);
					break;
				// Si action lister
				case "list":
					// Renvoi à la liste des activités sportives
					redirectToSportsList(request, response, view, userId, from, to, orderBy, dir, lang);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des activités sportives
					redirectToSportsList(request, response, view, userId, from, to, null, null, lang);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des activités sportives
			redirectToSportsList(request, response, view, userId, from, to, null, null, lang);
		}
	}

	/**
	 * Traitement du formulaire d'ajout d'une activité sportive
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sports Servlet [POST] -->");
		logger.info("Tentative d'ajout d'une activité sportive en cours ..");

		// Récupère de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		Timestamp dateTime = GlobalTools.formatStringToTimestamp(request.getParameter("dateTime"));
		String activity = new String(request.getParameter("activity").trim().getBytes("ISO-8859-1"), "UTF-8");

		try {
			// Ajoute la nouvelle activité sportive
			sportMgr.addSport(user.getId(), dateTime, activity);

			// Ajoute le message de succès en session
			request.getSession().setAttribute("success", Messages.getProperty("success.sport.add", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof DateHourException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.sport.date.hour", lang));
			} else if (fex instanceof ActivityException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.sport.activity", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		} finally {
			// Redirection vers la servlet en GET
			redirectToThisServletAfterPostOrDelete(request, response);
		}
	}

	/**
	 * Récupère l'activité sportive selon son ID et essaye de la supprimer de la base de donnée, charge le message succes/erreur
	 * 
	 * @param request
	 * @param sportId
	 * @param userId
	 * @param lang
	 */
	private void deleteFunction(HttpServletRequest request, int sportId, int userId, String lang) {
		Sport sport = null;
		try {
			// Récupère l'activité sportive selon son ID et l'ID de l'utilisateur
			sport = sportMgr.getSport(sportId, userId);

			// Supprime l'activité sportive
			sportMgr.deleteSport(sport);

			// Ajoute le message de succès dans la view
			request.getSession().setAttribute("success", Messages.getProperty("success.sport.delete", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof NotExistException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.sport.not.exist", lang));
			} else if (fex instanceof CantDeleteException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.sport.cant.delete", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Récupère la liste des activités sportives de l'utilisateur pour la période sélectionnée et renvoi vers la JSP sports avec message d'erreur si
	 * besoin
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param userId
	 * @param from
	 * @param to
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToSportsList(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, Timestamp from,
			Timestamp to, String orderBy, String dir, String lang) throws ServletException, IOException {
		List<Sport> listSport = null;
		try {
			// Récupère la liste des activités sportives de l'utilisateur
			listSport = sportMgr.getSportsFromPeriod(userId, from, to);

			// Tri la liste en fonction des paramètres
			listSport = sportMgr.orderBy(listSport, orderBy, dir);

		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}
		// Ajoute la liste dans la view
		view.addAttributeToList(new ViewAttribut("listSport", listSport));
		// Redirection
		redirectToSportJSP(request, response, view);
	}

	/**
	 * Redirige la requête vers la JSP List
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSportJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Sport JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/health/sport/sport.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet en Get après un Post ou un Delete
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServletAfterPostOrDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString());
	}

}
