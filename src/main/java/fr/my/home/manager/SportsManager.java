package fr.my.home.manager;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Sport;
import fr.my.home.dao.implementation.SportDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.health.sports.ActivityException;
import fr.my.home.exception.health.sports.CantDeleteException;
import fr.my.home.exception.health.sports.DateHourException;
import fr.my.home.exception.health.sports.NotExistException;

/**
 * Manager qui prends en charge la gestion des activités sportives
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class SportsManager {
	private static final Logger logger = LogManager.getLogger(SportsManager.class);

	// Attributes

	private SportDAO sportDAO;

	// Constructors

	/**
	 * Default Constructor
	 */
	public SportsManager() {
		super();
		sportDAO = new SportDAO();
	}

	// Methods

	/**
	 * Récupère la liste des activités sportives pour l'utilisateur connecté pour la période précisée
	 * 
	 * @param userId
	 * @param from
	 * @param to
	 * @return List<Sport>
	 * @throws TechnicalException
	 */
	public List<Sport> getSportsFromPeriod(int userId, Timestamp from, Timestamp to) throws TechnicalException {
		logger.debug("Tentative de récupération des activités sportives en cours pour la période précisée ..");
		List<Sport> listSport = null;
		try {
			// Récupère la liste des activités sportives
			listSport = sportDAO.getAllSportsFromPeriod(userId, from, to);
			logger.debug("Récupération des activités sportives enregistrées");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listSport;
	}

	/**
	 * Récupère l'activité sportive à partir de son ID et de l'ID de l'utilisateur, ou erreur fonctionnelle si aucune note
	 * 
	 * @param sportId
	 * @param userId
	 * @return Sport
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Sport getSport(int sportId, int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de récupération d'une activité sportive en cours ..");
		Sport sport = null;
		try {
			// Récupère l'activité sportive
			sport = sportDAO.getOneSport(sportId, userId);
			logger.debug("Récupération d'une activité sportive enregistrée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new NotExistException("Activité Sportive Inexistante");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return sport;
	}

	/**
	 * Vérifie les champs du formulaire puis ajoute l'activité sportive en base, ou renvoi exception fonctionnelle / technique
	 * 
	 * @param userId
	 * @param dateTime
	 * @param activity
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addSport(int userId, Timestamp dateTime, String activity) throws FonctionnalException, TechnicalException {
		// Vérifie si les champs sont bien tous valides
		checkParamsSport(dateTime, activity);

		try {
			// Ajout de la nouvelle activité sportive
			Sport sport = new Sport(userId, activity.trim(), dateTime);
			sportDAO.add(sport);
			logger.debug("Ajout d'une nouvelle activité sportive réussi");
		} catch (FonctionnalException | TechnicalException ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Supprime l'activité sportive ou message d'erreur dans la view si besoin
	 * 
	 * @param sport
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deleteSport(Sport sport) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'une activité sportive en cours ..");
		try {
			// Supprime l'activité sportive
			sportDAO.delete(sport);
			logger.debug("Suppression de l'activité sportive {" + String.valueOf(sport.getId()) + "} de la base");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new CantDeleteException("Impossible de supprimer l'activité sportive");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Vérifie les paramètres d'ajout d'une activité sportive et renvoi une exception fonctionnelle si problème
	 * 
	 * @param dateTime
	 * @param activity
	 * @throws FonctionnalException
	 */
	private void checkParamsSport(Timestamp dateTime, String activity) throws FonctionnalException {
		if (dateTime == null) {
			throw new DateHourException("Date / Heure manquante");
		} else if (activity == null || activity.trim().length() < 1 || activity.trim().length() > 200) {
			throw new ActivityException("Activité incorrecte");
		}
	}

	/**
	 * Organise la liste en fonction des paramètres
	 * 
	 * @param listSport
	 * @param orderBy
	 * @param dir
	 * @return List<Sport>
	 */
	public List<Sport> orderBy(List<Sport> listSport, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null && !orderBy.trim().isEmpty() && (orderBy.equals("date") | orderBy.equals("activity")) && dir != null
				&& !dir.trim().isEmpty() && (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("date")) {
				// Si ordre par date
				listSport.sort(Comparator.comparing(Sport::getSaveDate).thenComparing(Sport::getActivity, String.CASE_INSENSITIVE_ORDER)
						.thenComparingInt(Sport::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listSport);
				}
			} else {
				// Si ordre par activité
				listSport.sort(Comparator.comparing(Sport::getActivity, String.CASE_INSENSITIVE_ORDER).thenComparing(Sport::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listSport);
				}
			}
		} else {
			// Ordre par défaut
			listSport.sort(Comparator.comparing(Sport::getSaveDate).thenComparing(Sport::getActivity, String.CASE_INSENSITIVE_ORDER)
					.thenComparingInt(Sport::getId));
			Collections.reverse(listSport);
		}
		return listSport;
	}

}
