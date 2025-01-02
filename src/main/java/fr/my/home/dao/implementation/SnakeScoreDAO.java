package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.SnakeScore;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe SnakeScoreDAO qui gère le stockage des scores de Snake
 * 
 * @author Jonathan
 * @version 1.0
 * @since 31/12/2024
 */
public class SnakeScoreDAO implements HibernateDAO<SnakeScore> {

	// Attributes

	private static final String SNAKESCOREDAO_GET_WR = Settings.getStringProperty("snake.score.get.wr");
	private static final String SNAKESCOREDAO_GET_PB = Settings.getStringProperty("snake.score.get.pb");

	// Constructor

	/**
	 * Default Constructor
	 */
	public SnakeScoreDAO() {
		super();
	}

	// Methods

	/**
	 * Récupère la liste des 3 meilleurs scores tous utilisateurs confondus
	 * 
	 * @return List<SnakeScore>
	 * @throws TechnicalException
	 */
	public List<SnakeScore> getWR() throws TechnicalException {
		List<SnakeScore> listScore = new ArrayList<SnakeScore>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<SnakeScore> query = session.createQuery(SNAKESCOREDAO_GET_WR);
		query.setMaxResults(3);
		listScore = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
		return listScore;
	}

	/**
	 * Récupère le meilleur score de l'utilisateur
	 * 
	 * @param userId
	 * @return SnakeScore
	 * @throws TechnicalException
	 */
	public SnakeScore getPB(int userId) throws TechnicalException {
		SnakeScore score = null;
		List<SnakeScore> listScore = new ArrayList<SnakeScore>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<SnakeScore> query = session.createQuery(SNAKESCOREDAO_GET_PB);
		query.setParameter("score_id_user", userId);
		query.setMaxResults(1);
		listScore = query.getResultList();
		if (!listScore.isEmpty()) {
			score = listScore.get(0);
		}
		DatabaseAccess.getInstance().validateSession(session);
		return score;

	}

	/**
	 * Ajoute un nouveau score
	 * 
	 * @param score
	 * @return int
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public int add(SnakeScore score) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(score);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le score");
		}
		return id;
	}

	/**
	 * Met à jour un score
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(SnakeScore score) throws FonctionnalException, TechnicalException {
		// Utilisation interdite, uniquement pour test
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(score);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le score");
		}
	}

	/**
	 * Supprime un score
	 * 
	 * @param score
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(SnakeScore score) throws FonctionnalException, TechnicalException {
		// Utilisation interdite, uniquement pour test
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(score);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le score");
		}
	}

}
