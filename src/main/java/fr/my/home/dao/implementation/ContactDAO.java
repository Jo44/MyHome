package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Contact;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe ContactDAO qui gère le stockage des contacts
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class ContactDAO implements HibernateDAO<Contact> {

	// Attributes

	private static final String CONTACTDAO_GET_ALL = Settings.getStringProperty("contact.get.all");
	private static final String CONTACTDAO_GET_ONE = Settings.getStringProperty("contact.get.one");

	// Constructor

	/**
	 * Default Constructor
	 */
	public ContactDAO() {
		super();
	}

	// Methods

	/**
	 * Récupère la liste de tous les contacts de l'utilisateur
	 * 
	 * @param userId
	 * @return List<Contact>
	 * @throws TechnicalException
	 */
	public List<Contact> getAllContacts(int userId) throws TechnicalException {
		List<Contact> listContact = new ArrayList<Contact>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Contact> query = session.createQuery(CONTACTDAO_GET_ALL);
		query.setParameter("contact_id_user", userId);
		listContact = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
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
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Contact> query = session.createQuery(CONTACTDAO_GET_ONE);
		query.setParameter("contact_id", contactId);
		query.setParameter("contact_id_user", userId);
		try {
			contact = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le contact n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return contact;
	}

	/**
	 * Ajoute un nouveau contact en base et renvoi son ID, ou exception fonctionnelle si impossible
	 * 
	 * @param contact
	 * @return int
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public int add(Contact contact) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(contact);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le contact");
		}
		return id;
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
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(contact);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le contact");
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
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(contact);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le contact");
		}
	}

}
