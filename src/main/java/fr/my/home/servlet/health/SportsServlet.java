package fr.my.home.servlet.health;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Sport;
import fr.my.home.bean.User;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.health.sports.ActivityException;
import fr.my.home.exception.health.sports.CantDeleteException;
import fr.my.home.exception.health.sports.DateHourException;
import fr.my.home.exception.health.sports.NotExistException;
import fr.my.home.manager.SportsManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge la gestion des activités sportives
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/health/sports")
public class SportsServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(SportsServlet.class);
	private SportsManager sportMgr;

	/**
	 * Constructeur
	 */
	public SportsServlet() {
		super();
		// Initialisation du manager
		sportMgr = new SportsManager();
	}

	/**
	 * Redirection vers la liste et la suppression des activités sportives
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sport Servlet [GET] -->");

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		request.setAttribute("error", error);

		// Récupère l'attribut success si il existe
		String success = (String) request.getSession().getAttribute("success");
		request.getSession().removeAttribute("success");
		request.setAttribute("success", success);

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère le nombre de lignes max d'un tableau à afficher (pour traitement javascript)
		int maxRows = GlobalTools.getMaxRows(request);
		request.setAttribute("maxRows", maxRows);

		// Récupère la date / heure actuelle en string et la charge dans la requête
		LocalDateTime now = LocalDateTime.now();
		String today = GlobalTools.formatDateToString(now);
		request.setAttribute("today", today);

		// Récupère les paramètres de la requête
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		int sportId;
		try {
			sportId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			sportId = 0;
		}
		// Récupère les dates de la période voulue et les charge dans la requête
		Timestamp from = GlobalTools.getFrom(request.getParameter("from"), 3L);
		Timestamp to = GlobalTools.getTo(request.getParameter("to"));
		request.setAttribute("from", GlobalTools.formatDateToString(from.toLocalDateTime()));
		request.setAttribute("to", GlobalTools.formatDateToString(to.toLocalDateTime()));

		// Récupère l'ordre de tri et le sens
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
					redirectToSportsList(request, response, userId, from, to, orderBy, dir, lang);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des activités sportives
					redirectToSportsList(request, response, userId, from, to, null, null, lang);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des activités sportives
			redirectToSportsList(request, response, userId, from, to, null, null, lang);
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

			// Ajoute le message de succès dans la requête
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
	 * @param userId
	 * @param from
	 * @param to
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToSportsList(HttpServletRequest request, HttpServletResponse response, int userId, Timestamp from, Timestamp to,
			String orderBy, String dir, String lang) throws ServletException, IOException {
		List<Sport> listSport = null;
		try {
			// Récupère la liste des activités sportives de l'utilisateur
			listSport = sportMgr.getSportsFromPeriod(userId, from, to);

			// Tri la liste en fonction des paramètres
			listSport = sportMgr.orderBy(listSport, orderBy, dir);

		} catch (TechnicalException tex) {
			request.setAttribute("error", Messages.getProperty("error.database", lang));
		}
		// Prépare la liste inversée pour le graphique JS
		List<Sport> jsListSport = new ArrayList<Sport>(listSport);
		Collections.reverse(jsListSport);
		// Ajoute les attributs à la requête
		request.setAttribute("listSport", listSport);
		request.setAttribute("jsListSport", jsListSport);
		request.setAttribute("formatterDate", new SimpleDateFormat("dd/MM/yyyy - HH:mm"));
		// Redirection
		redirectToSportJSP(request, response);
	}

	/**
	 * Redirige la requête vers la JSP List
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSportJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Sport JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/health/sports.jsp");
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
