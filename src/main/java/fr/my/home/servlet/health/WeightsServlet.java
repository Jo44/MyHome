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

import fr.my.home.bean.User;
import fr.my.home.bean.Weight;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.health.weights.CantDeleteException;
import fr.my.home.exception.health.weights.DateHourException;
import fr.my.home.exception.health.weights.InvalidWeightException;
import fr.my.home.exception.health.weights.NotExistException;
import fr.my.home.manager.WeightManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion du poids
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/08/2021
 */
@WebServlet("/health/weights")
public class WeightsServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(WeightsServlet.class);

	// Attributes

	private WeightManager weightMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public WeightsServlet() {
		super();
		// Initialisation du manager
		weightMgr = new WeightManager();
	}

	// Methods

	/**
	 * Redirection vers la liste et la suppression des poids
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Weight Servlet [GET] -->");

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
		int weightId;
		try {
			weightId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			weightId = 0;
		}
		// Récupère les dates de la période voulue et les charge dans la view
		Timestamp from = Timestamp.valueOf(now);
		Timestamp to = Timestamp.valueOf(now);
		try {
			from.setTime(Long.parseLong(request.getParameter("from")));

		} catch (NumberFormatException nfe) {
			from = Timestamp.valueOf(now.minusYears(2L));
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
					// Essaye de supprimer le poids
					deleteFunction(request, weightId, userId, lang);

					// Puis renvoi à la liste des poids
					redirectToThisServletAfterPostOrDelete(request, response);
					break;
				// Si action lister
				case "list":
					// Renvoi à la liste des poids
					redirectToWeightsList(request, response, view, userId, from, to, orderBy, dir, lang);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des poids
					redirectToWeightsList(request, response, view, userId, from, to, null, null, lang);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des poids
			redirectToWeightsList(request, response, view, userId, from, to, null, null, lang);
		}
	}

	/**
	 * Traitement du formulaire d'ajout d'un poids
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Weight Servlet [POST] -->");
		logger.info("Tentative d'ajout d'un poids en cours ..");

		// Récupère de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		Timestamp dateTime = GlobalTools.formatStringToTimestamp(request.getParameter("dateTime"));
		String weightValueStr = request.getParameter("weightValue");

		try {
			// Ajoute le nouveau poids
			weightMgr.addWeight(user.getId(), dateTime, weightValueStr);

			// Ajoute le message de succès en session
			request.getSession().setAttribute("success", Messages.getProperty("success.weight.add", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof DateHourException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.weight.date.hour", lang));
			} else if (fex instanceof InvalidWeightException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.weight.invalid", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		} finally {
			// Redirection vers la servlet en GET
			redirectToThisServletAfterPostOrDelete(request, response);
		}
	}

	/**
	 * Récupère le poids selon son ID et essaye de le supprimer de la base de donnée, charge le message succes/erreur
	 * 
	 * @param request
	 * @param weightId
	 * @param userId
	 * @param lang
	 */
	private void deleteFunction(HttpServletRequest request, int weightId, int userId, String lang) {
		Weight weight = null;
		try {
			// Récupère le poids selon son ID et l'ID de l'utilisateur
			weight = weightMgr.getWeight(weightId, userId);

			// Supprime le poids
			weightMgr.deleteWeight(weight);

			// Ajoute le message de succès dans la view
			request.getSession().setAttribute("success", Messages.getProperty("success.weight.delete", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof NotExistException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.weight.not.exist", lang));
			} else if (fex instanceof CantDeleteException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.weight.cant.delete", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Récupère la liste des poids de l'utilisateur pour la période sélectionnée et renvoi vers la JSP poids avec message d'erreur si besoin
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
	private void redirectToWeightsList(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, Timestamp from,
			Timestamp to, String orderBy, String dir, String lang) throws ServletException, IOException {
		List<Weight> listWeight = null;
		try {
			// Récupère la liste des poids de l'utilisateur
			listWeight = weightMgr.getWeightsFromPeriod(userId, from, to);

			// Tri la liste en fonction des paramètres
			listWeight = weightMgr.orderBy(listWeight, orderBy, dir);

		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}
		// Ajoute la liste dans la view
		view.addAttributeToList(new ViewAttribut("listWeight", listWeight));
		// Redirection
		redirectToWeightJSP(request, response, view);
	}

	/**
	 * Redirige la requête vers la JSP Weight
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToWeightJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Weight JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/health/weight/weight.jsp");
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
