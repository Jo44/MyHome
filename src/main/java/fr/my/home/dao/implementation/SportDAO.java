package fr.my.home.dao.implementation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Sport;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Settings;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

/**
 * Classe SportDAO
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class SportDAO implements HibernateDAO<Sport> {

	/**
	 * Attributs
	 */

	private static final String SPORTDAO_GET_ALL_FROM_PERIOD = Settings.getStringProperty("sport.get.all.from.period");
	private static final String SPORTDAO_GET_ONE = Settings.getStringProperty("sport.get.one");

	/**
	 * Constructeur
	 */
	public SportDAO() {}

	/**
	 * Récupère la liste de toutes les activités sportives de l'utilisateur pour la période précisée
	 * 
	 * @param userId
	 * @param from
	 * @param to
	 * @return List<Sport>
	 * @throws TechnicalException
	 */
	public List<Sport> getAllSportsFromPeriod(int userId, Timestamp from, Timestamp to) throws TechnicalException {
		List<Sport> listSport = new ArrayList<Sport>();
		Session session = HibernateUtil.getInstance().openSession();
		Query<Sport> query = session.createQuery(SPORTDAO_GET_ALL_FROM_PERIOD, Sport.class);
		query.setParameter("id_user", userId);
		query.setParameter("from", from);
		query.setParameter("to", to);
		listSport = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
		return listSport;
	}

	/**
	 * Récupère l'activité sportive selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si elle n'existe pas
	 * 
	 * @param sportId
	 * @param userId
	 * @return Sport
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Sport getOneSport(int sportId, int userId) throws FonctionnalException, TechnicalException {
		Sport sport = null;
		Session session = HibernateUtil.getInstance().openSession();
		Query<Sport> query = session.createQuery(SPORTDAO_GET_ONE, Sport.class);
		query.setParameter("id", sportId);
		query.setParameter("id_user", userId);
		query.setMaxResults(1);
		try {
			sport = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("L'activité sportive n'existe pas");
		} finally {
			HibernateUtil.getInstance().validateSession(session);
		}
		return sport;
	}

	/**
	 * Ajoute une nouvelle activité sportive, ou exception fonctionnelle si impossible
	 * 
	 * @param sport
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(Sport sport) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.persist(sport);
			session.flush();
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter l'activité sportive");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Met à jour une activité sportive, ou exception fonctionnelle si impossible
	 * 
	 * @param sport
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(Sport sport) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.merge(sport);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour l'activité sportive");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Supprime une activité sportive, ou exception fonctionnelle si impossible
	 * 
	 * @param sport
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(Sport sport) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.remove(sport);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer l'activité sportive");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

}
