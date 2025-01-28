package fr.my.home.manager;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Weight;
import fr.my.home.dao.implementation.WeightDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.health.weights.CantDeleteException;
import fr.my.home.exception.health.weights.DateHourException;
import fr.my.home.exception.health.weights.InvalidWeightException;
import fr.my.home.exception.health.weights.NotExistException;

/**
 * Manager qui prends en charge la gestion des poids
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class WeightManager {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(WeightManager.class);
	private WeightDAO weightDAO;

	/**
	 * Constructeur
	 */
	public WeightManager() {
		super();
		weightDAO = new WeightDAO();
	}

	/**
	 * Récupère la liste des poids pour l'utilisateur connecté pour la période précisée
	 *
	 * @param userId
	 * @param from
	 * @param to
	 * @return List<Weight>
	 * @throws TechnicalException
	 */
	public List<Weight> getWeightsFromPeriod(int userId, Timestamp from, Timestamp to) throws TechnicalException {
		logger.debug("Tentative de récupération des poids en cours pour la période précisée ..");
		List<Weight> listWeight = null;
		try {
			// Récupère la liste des poids
			listWeight = weightDAO.getAllWeightsFromPeriod(userId, from, to);
			logger.debug("Récupération des poids enregistrés");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listWeight;
	}

	/**
	 * Récupère le poids à partir de son ID et de l'ID de l'utilisateur, ou erreur fonctionnelle si aucun poids
	 * 
	 * @param weightId
	 * @param userId
	 * @return Weight
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Weight getWeight(int weightId, int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de récupération d'un poids en cours ..");
		Weight weight = null;
		try {
			// Récupère le poids
			weight = weightDAO.getOneWeight(weightId, userId);
			logger.debug("Récupération d'un poids enregistré");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new NotExistException("Poids Inexistant");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return weight;
	}

	/**
	 * Vérifie les champs du formulaire puis ajoute le poids en base, ou renvoi exception fonctionnelle / technique
	 * 
	 * @param userId
	 * @param dateTime
	 * @param weightValueStr
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addWeight(int userId, Timestamp dateTime, String weightValueStr) throws FonctionnalException, TechnicalException {
		// Vérifie si les champs sont bien tous valides
		checkParamsWeight(dateTime, weightValueStr);

		try {
			// Ajout du nouveau poids
			Weight weight = new Weight(userId, Float.parseFloat(weightValueStr), dateTime);
			weightDAO.add(weight);
			logger.debug("Ajout d'un nouveau poids réussi");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Vérifie les paramètres d'ajout d'un poids et renvoi une exception fonctionnelle si problème
	 * 
	 * @param dateTime
	 * @param weightValueStr
	 * @throws FonctionnalException
	 */
	private void checkParamsWeight(Timestamp dateTime, String weightValueStr) throws FonctionnalException {
		float weightValue = 0f;
		if (dateTime == null) {
			throw new DateHourException("Date / Heure manquante");
		} else if (weightValueStr == null || weightValueStr.trim().isEmpty()) {
			throw new InvalidWeightException("Poids incorrect");
		}
		try {
			weightValue = Float.parseFloat(weightValueStr);
		} catch (NumberFormatException nbe) {
			throw new InvalidWeightException("Poids incorrect");
		}
		if (weightValue < 1f || weightValue > 500f) {
			throw new InvalidWeightException("Poids incorrect");
		}
	}

	/**
	 * Supprime le poids, ou renvoi exception fonctionnelle / technique
	 *
	 * @param weight
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deleteWeight(Weight weight) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'un poids en cours ..");
		try {
			// Supprime le poids
			weightDAO.delete(weight);
			logger.debug("Suppression du poids {" + String.valueOf(weight.getId()) + "} de la base");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new CantDeleteException("Impossible de supprimer le poids");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Organise la liste en fonction des paramètres
	 *
	 * @param listWeight
	 * @param orderBy
	 * @param dir
	 * @return List<Weight>
	 */
	public List<Weight> orderBy(List<Weight> listWeight, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null && !orderBy.trim().isEmpty() && (orderBy.equals("date") | orderBy.equals("value")) && dir != null && !dir.trim().isEmpty()
				&& (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("date")) {
				// Si ordre par date
				listWeight.sort(Comparator.comparing(Weight::getSaveDate).thenComparing(Weight::getValue).thenComparingInt(Weight::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listWeight);
				}
			} else {
				// Si ordre par valeur
				listWeight.sort(Comparator.comparing(Weight::getValue).thenComparing(Weight::getSaveDate).thenComparingInt(Weight::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listWeight);
				}
			}
		} else {
			// Ordre par défaut
			listWeight.sort(Comparator.comparing(Weight::getSaveDate).thenComparing(Weight::getValue).thenComparingInt(Weight::getId));
			Collections.reverse(listWeight);
		}
		return listWeight;
	}

}
