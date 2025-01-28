package fr.my.home.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import fr.my.home.bean.SnakeScore;
import fr.my.home.dao.implementation.SnakeScoreDAO;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Manager qui prends en charge la gestion des scores de Snake
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class SnakeManager {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(SnakeManager.class);
	private SnakeScoreDAO scoreDAO;
	private UserDAO userDAO;

	/**
	 * Constructeur
	 */
	public SnakeManager() {
		super();
		scoreDAO = new SnakeScoreDAO();
		userDAO = new UserDAO();
	}

	/**
	 * Récupère la liste des 3 meilleurs scores
	 *
	 * @return List<SnakeScore>
	 * @throws TechnicalException
	 */
	public List<SnakeScore> getScoresWR() throws TechnicalException {
		logger.debug("Tentative de récupération des 3 meilleurs scores ..");
		List<SnakeScore> listScore = null;
		try {
			// Récupère la liste des scores
			listScore = scoreDAO.getWR();
			logger.debug("Récupération des 3 meilleurs scores");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listScore;
	}

	/**
	 * Récupère le meilleur score à partir de l'ID de l'utilisateur
	 * 
	 * @param userId
	 * @return SnakeScore
	 * @throws TechnicalException
	 */
	public SnakeScore getScorePB(int userId) throws TechnicalException {
		logger.debug("Tentative de récupération du meilleur score de l'utilisateur ..");
		SnakeScore score = null;
		try {
			// Récupère le score
			score = scoreDAO.getPB(userId);
			logger.debug("Récupération du meilleur score de l'utilisateur");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return score;
	}

	/**
	 * Récupère le nom de l'utilisateur à partir de son ID
	 * 
	 * @param userId
	 * @return String
	 * @throws TechnicalException
	 */
	private String getScoreName(int userId) throws TechnicalException {
		logger.debug("Tentative de récupération du nom de l'utilisateur ..");
		String name = null;
		try {
			// Récupère le nom de l'utilisateur
			name = userDAO.getNameById(userId);
			logger.debug("Récupération du nom de l'utilisateur");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return name;
	}

	/**
	 * Génère un JSON avec les scores en format : [["PB", "Nom Utilisateur", "Score"], ["#1", "Nom Utilisateur", "Score"],...]
	 * 
	 * @param scorePB
	 * @param scoresWR
	 * @return String
	 * @throws TechnicalException
	 */
	public String generateJSON(SnakeScore scorePB, List<SnakeScore> scoresWR) throws TechnicalException {
		List<List<String>> scoresList = new ArrayList<List<String>>();

		// Ajoute le PB
		List<String> entryPB = new ArrayList<String>();
		if (scorePB != null) {
			String name = getScoreName(scorePB.getIdUser()).trim();
			entryPB.add("PB");
			entryPB.add(name.length() > 8 ? name.substring(0, 6) + ".." : name);
			entryPB.add(String.valueOf(scorePB.getValue()));
		} else {
			entryPB.add("PB");
			entryPB.add("Undefined");
			entryPB.add("0");
		}
		scoresList.add(entryPB);

		// Ajoute les 3 WR, jusqu'à 3 au total
		int pos = 1;
		if (scoresWR != null) {
			for (SnakeScore score : scoresWR) {
				List<String> entryWR = new ArrayList<String>();
				String name = getScoreName(score.getIdUser()).trim();
				entryWR.add("#" + pos);
				entryWR.add(name.length() > 8 ? name.substring(0, 6) + ".." : name);
				entryWR.add(String.valueOf(score.getValue()));
				scoresList.add(entryWR);
				pos++;
			}
		}

		// Complète avec des indéfinis si nécessaire
		while (scoresList.size() < 4) {
			List<String> entryUndefined = new ArrayList<String>();
			entryUndefined.add("#" + pos);
			entryUndefined.add("Undefined");
			entryUndefined.add("0");
			scoresList.add(entryUndefined);
			pos++;
		}

		// Conversion en JSON
		return new Gson().toJson(scoresList);
	}

	/**
	 * Ajoute le score en base, ou renvoi exception fonctionnelle / technique
	 * 
	 * @param snakeScore
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addScore(SnakeScore snakeScore) throws FonctionnalException, TechnicalException {
		try {
			// Ajout du nouveau score
			scoreDAO.add(snakeScore);
			logger.debug("Ajout d'un nouveau score réussi");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

}
