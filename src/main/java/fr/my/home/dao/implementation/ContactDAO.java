package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Contact;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Settings;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

/**
 * Classe ContactDAO
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class ContactDAO implements HibernateDAO<Contact> {

	/**
	 * Attributs
	 */

	private static final String CONTACTDAO_GET_ALL = Settings.getStringProperty("contact.get.all");
	private static final String CONTACTDAO_GET_ONE = Settings.getStringProperty("contact.get.one");

	/**
	 * Constructeur
	 */
	public ContactDAO() {}

	/**
	 * Récupère la liste de tous les contacts de l'utilisateur
	 * 
	 * @param userId
	 * @return List<Contact>
	 * @throws TechnicalException
	 */
	public List<Contact> getAllContacts(int userId) throws TechnicalException {
		List<Contact> listContact = new ArrayList<Contact>();
		Session session = HibernateUtil.getInstance().openSession();
		Query<Contact> query = session.createQuery(CONTACTDAO_GET_ALL, Contact.class);
		query.setParameter("id_user", userId);
		listContact = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
		return listContact;
	}

	/**
	 * Récupère le contact selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si il n'existe pas
	 * 
	 * @param contactId
	 * @param userId
	 * @return Contact
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Contact getOneContact(int contactId, int userId) throws FonctionnalException, TechnicalException {
		Contact contact = null;
		Session session = HibernateUtil.getInstance().openSession();
		Query<Contact> query = session.createQuery(CONTACTDAO_GET_ONE, Contact.class);
		query.setParameter("id", contactId);
		query.setParameter("id_user", userId);
		query.setMaxResults(1);
		try {
			contact = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le contact n'existe pas");
		} finally {
			HibernateUtil.getInstance().validateSession(session);
		}
		return contact;
	}

	/**
	 * Ajoute un nouveau contact, ou exception fonctionnelle si impossible
	 * 
	 * @param contact
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(Contact contact) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.persist(contact);
			session.flush();
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pex) {
			throw new FonctionnalException("Impossible d'ajouter le contact");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Met à jour un contact, ou exception fonctionnelle si impossible
	 * 
	 * @param contact
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(Contact contact) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.merge(contact);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le contact");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Supprime un contact, ou exception fonctionnelle si impossible
	 * 
	 * @param contact
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(Contact contact) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.remove(contact);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le contact");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

}
