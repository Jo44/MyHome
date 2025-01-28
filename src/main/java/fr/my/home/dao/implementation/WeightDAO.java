package fr.my.home.dao.implementation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Weight;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Settings;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

/**
 * Classe WeightDAO
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class WeightDAO implements HibernateDAO<Weight> {

	/**
	 * Attributs
	 */

	private static final String WEIGHTDAO_GET_ALL_FROM_PERIOD = Settings.getStringProperty("weight.get.all.from.period");
	private static final String WEIGHTDAO_GET_ONE = Settings.getStringProperty("weight.get.one");

	/**
	 * Constructeur
	 */
	public WeightDAO() {}

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
		Session session = HibernateUtil.getInstance().openSession();
		Query<Weight> query = session.createQuery(WEIGHTDAO_GET_ALL_FROM_PERIOD, Weight.class);
		query.setParameter("id_user", userId);
		query.setParameter("from", from);
		query.setParameter("to", to);
		listWeight = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
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
		Session session = HibernateUtil.getInstance().openSession();
		Query<Weight> query = session.createQuery(WEIGHTDAO_GET_ONE, Weight.class);
		query.setParameter("id", weightId);
		query.setParameter("id_user", userId);
		query.setMaxResults(1);
		try {
			weight = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le poids n'existe pas");
		} finally {
			HibernateUtil.getInstance().validateSession(session);
		}
		return weight;
	}

	/**
	 * Ajoute un nouveau poids, ou exception fonctionnelle si impossible
	 * 
	 * @param weight
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(Weight weight) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.persist(weight);
			session.flush();
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le poids");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
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
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.merge(weight);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le poids");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
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
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.remove(weight);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le poids");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

}
