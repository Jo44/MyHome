package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.SnakeScore;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Settings;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

/**
 * Classe SnakeScoreDAO
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class SnakeScoreDAO implements HibernateDAO<SnakeScore> {

	/**
	 * Attributs
	 */

	private static final String SNAKESCOREDAO_GET_WR = Settings.getStringProperty("snake.score.get.wr");
	private static final String SNAKESCOREDAO_GET_PB = Settings.getStringProperty("snake.score.get.pb");

	/**
	 * Constructeur
	 */
	public SnakeScoreDAO() {}

	/**
	 * Récupère la liste des 3 meilleurs scores tous utilisateurs confondus
	 * 
	 * @return List<SnakeScore>
	 * @throws TechnicalException
	 */
	public List<SnakeScore> getWR() throws TechnicalException {
		List<SnakeScore> listScore = new ArrayList<SnakeScore>();
		Session session = HibernateUtil.getInstance().openSession();
		Query<SnakeScore> query = session.createQuery(SNAKESCOREDAO_GET_WR, SnakeScore.class);
		query.setMaxResults(3);
		listScore = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
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
		Session session = HibernateUtil.getInstance().openSession();
		Query<SnakeScore> query = session.createQuery(SNAKESCOREDAO_GET_PB, SnakeScore.class);
		query.setParameter("id_user", userId);
		query.setMaxResults(1);
		try {
			score = query.getSingleResult();
		} catch (NoResultException nre) {
			score = null;
		} finally {
			HibernateUtil.getInstance().validateSession(session);
		}
		return score;

	}

	/**
	 * Ajoute un nouveau score, ou exception fonctionnelle si impossible
	 * 
	 * @param score
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(SnakeScore score) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.persist(score);
			session.flush();
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le score");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Met à jour un score, ou exception fonctionnelle si impossible
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(SnakeScore score) throws FonctionnalException, TechnicalException {
		// Utilisation interdite, uniquement pour test
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.merge(score);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le score");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Supprime un score, ou exception fonctionnelle si impossible
	 * 
	 * @param score
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(SnakeScore score) throws FonctionnalException, TechnicalException {
		// Utilisation interdite, uniquement pour test
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.remove(score);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le score");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

}
