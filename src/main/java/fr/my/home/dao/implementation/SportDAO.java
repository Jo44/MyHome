package fr.my.home.dao.implementation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Sport;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe SportDAO qui gère le stockage des activités sportives
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class SportDAO implements HibernateDAO<Sport> {

	// Attributes

	private static final String SPORTDAO_GET_ALL_FROM_PERIOD = Settings.getStringProperty("sport.get.all.from.period");
	private static final String SPORTDAO_GET_ONE = Settings.getStringProperty("sport.get.one");

	// Constructor

	/**
	 * Default Constructor
	 */
	public SportDAO() {
		super();
	}

	// Methods

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
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Sport> query = session.createQuery(SPORTDAO_GET_ALL_FROM_PERIOD);
		query.setParameter("sport_id_user", userId);
		query.setParameter("from", from);
		query.setParameter("to", to);
		listSport = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
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
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Sport> query = session.createQuery(SPORTDAO_GET_ONE);
		query.setParameter("sport_id", sportId);
		query.setParameter("sport_id_user", userId);
		try {
			sport = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("L'activité sportive n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return sport;
	}

	/**
	 * Ajoute une nouvelle activité sportive en base et renvoi son ID, ou exception fonctionnelle si impossible
	 * 
	 * @param sport
	 * @return int
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public int add(Sport sport) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(sport);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter l'activité sportive");
		}
		return id;
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
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(sport);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour l'activité sportive");
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
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(sport);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer l'activité sportive");
		}
	}

}
