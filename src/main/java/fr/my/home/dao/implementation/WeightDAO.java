package fr.my.home.dao.implementation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Weight;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe WeightDAO qui gère le stockage des poids
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/08/2021
 */
public class WeightDAO implements HibernateDAO<Weight> {

	// Attributes

	private static final String WEIGHTDAO_GET_ALL_FROM_PERIOD = Settings.getStringProperty("weight.get.all.from.period");
	private static final String WEIGHTDAO_GET_ONE = Settings.getStringProperty("weight.get.one");

	// Constructor

	/**
	 * Default Constructor
	 */
	public WeightDAO() {
		super();
	}

	// Methods

	/**
	 * Récupère la liste de tous les poids de l'utilisateur pour la période précisée
	 * 
	 * @param userId
	 * @param from
	 * @param to
	 * @return List<Weight>
	 * @throws TechnicalException
	 */
	public List<Weight> getAllWeightsFromPeriod(int userId, Timestamp from, Timestamp to) throws TechnicalException {
		List<Weight> listWeight = new ArrayList<Weight>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Weight> query = session.createQuery(WEIGHTDAO_GET_ALL_FROM_PERIOD);
		query.setParameter("weight_id_user", userId);
		query.setParameter("from", from);
		query.setParameter("to", to);
		listWeight = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
		return listWeight;
	}

	/**
	 * Récupère le poids selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si il n'existe pas
	 * 
	 * @param weightId
	 * @param userId
	 * @return Weight
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Weight getOneWeight(int weightId, int userId) throws FonctionnalException, TechnicalException {
		Weight weight = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Weight> query = session.createQuery(WEIGHTDAO_GET_ONE);
		query.setParameter("weight_id", weightId);
		query.setParameter("weight_id_user", userId);
		try {
			weight = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le poids n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return weight;
	}

	/**
	 * Ajoute un nouveau poids en base et renvoi son ID, ou exception fonctionnelle si impossible
	 * 
	 * @param weight
	 * @return int
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public int add(Weight weight) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(weight);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le poids");
		}
		return id;
	}

	/**
	 * Met à jour un poids, ou exception fonctionnelle si impossible
	 * 
	 * @param weight
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(Weight weight) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(weight);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le poids");
		}
	}

	/**
	 * Supprime un poids, ou exception fonctionnelle si impossible
	 * 
	 * @param weight
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(Weight weight) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(weight);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le poids");
		}
	}

}
