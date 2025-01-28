package fr.my.home.manager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Contact;
import fr.my.home.dao.implementation.ContactDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.contacts.CantDeleteException;
import fr.my.home.exception.contacts.FirstnameException;
import fr.my.home.exception.contacts.NotExistException;
import fr.my.home.tool.GlobalTools;

/**
 * Manager qui prends en charge la gestion des contacts
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class ContactsManager {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(ContactsManager.class);
	private ContactDAO contactDAO;

	/**
	 * Constructeur
	 */
	public ContactsManager() {
		super();
		contactDAO = new ContactDAO();
	}

	/**
	 * Récupère la liste des contacts pour l'utilisateur connecté
	 * 
	 * @param userId
	 * @return List<Contact>
	 * @throws TechnicalException
	 */
	public List<Contact> getContacts(int userId) throws TechnicalException {
		logger.debug("Tentative de récupération des contacts en cours ..");
		List<Contact> listContact = null;
		try {
			// Récupère la liste des contacts
			listContact = contactDAO.getAllContacts(userId);
			logger.debug("Récupération des contacts enregistrés");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listContact;
	}

	/**
	 * Récupère le contact à partir de son ID et de l'ID de l'utilisateur, ou erreur fonctionnelle si aucun contact
	 * 
	 * @param contactId
	 * @param userId
	 * @return Contact
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Contact getContact(int contactId, int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de récupération d'un contact en cours ..");
		Contact contact = null;
		try {
			// Récupère le contact
			contact = contactDAO.getOneContact(contactId, userId);
			logger.debug("Récupération d'un contact enregistré");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new NotExistException("Contact Inexistant");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return contact;
	}

	/**
	 * Vérifie les champs du formulaire puis ajoute le contact en base, ou renvoi exception fonctionnelle / technique
	 * 
	 * @param userId
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param phone
	 * @param twitter
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addContact(int userId, String firstname, String lastname, String email, String phone, String twitter)
			throws FonctionnalException, TechnicalException {
		// Vérifie si les champs sont bien tous valides
		checkParamsContact(firstname);

		try {
			// Ajout du nouveau contact
			Contact contact = new Contact(userId, GlobalTools.capitalizeFirstLetters(firstname.trim()),
					GlobalTools.capitalizeFirstLetters(lastname.trim()), email.trim(), phone.trim(), twitter.trim(),
					Timestamp.valueOf(LocalDateTime.now()));
			contactDAO.add(contact);
			logger.debug("Ajout d'un nouveau contact réussi");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Modifie le contact
	 * 
	 * @param contactId
	 * @param userId
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param phone
	 * @param twitter
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void updateContact(int contactId, int userId, String firstname, String lastname, String email, String phone, String twitter)
			throws FonctionnalException, TechnicalException {
		// Vérifie si les champs sont bien tous valides
		checkParamsContact(firstname);

		try {
			// Récupère le contact
			Contact contact = contactDAO.getOneContact(contactId, userId);
			logger.debug("Récupération du contact");

			// Modifie le contact
			contact.setFirstname(GlobalTools.capitalizeFirstLetters(firstname.trim()));
			contact.setLastname(GlobalTools.capitalizeFirstLetters(lastname.trim()));
			contact.setEmail(email.trim());
			contact.setPhone(phone.trim());
			contact.setTwitter(twitter.trim());

			// Met à jour le contact en BDD
			contactDAO.update(contact);
			logger.debug("Contact modifié");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Supprime le contact ou exception fonctionnelle si problème
	 * 
	 * @param contact
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deleteContact(Contact contact) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'un contact en cours ..");
		try {
			// Supprime le contact
			contactDAO.delete(contact);
			logger.debug("Suppression du contact {" + String.valueOf(contact.getId()) + "} de la base");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new CantDeleteException("Impossible de supprimer le contact");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Vérifie les paramètres d'ajout d'un contact et renvoi une exception fonctionnelle si problème
	 * 
	 * @param firstname
	 * @throws FonctionnalException
	 */
	private void checkParamsContact(String firstname) throws FonctionnalException {
		if (firstname == null || firstname.trim().isEmpty()) {
			throw new FirstnameException("Prénom manquant");
		}
	}

	/**
	 * Organise la liste en fonction des paramètres
	 * 
	 * @param listContact
	 * @param orderBy
	 * @param dir
	 * @return List<Contact>
	 */
	public List<Contact> orderBy(List<Contact> listContact, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null
				&& !orderBy.trim().isEmpty() && (orderBy.equals("firstname") | orderBy.equals("lastname") | orderBy.equals("email")
						| orderBy.equals("phone") | orderBy.equals("twitter"))
				&& dir != null && !dir.trim().isEmpty() && (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("firstname")) {
				// Si ordre par firstname
				listContact.sort(Comparator.comparing(Contact::getFirstname, String.CASE_INSENSITIVE_ORDER)
						.thenComparing(Contact::getLastname, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Contact::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listContact);
				}
			} else if (orderBy.equals("lastname")) {
				// Si ordre par lastname
				listContact.sort(Comparator.comparing(Contact::getLastname, String.CASE_INSENSITIVE_ORDER)
						.thenComparing(Contact::getFirstname, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Contact::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listContact);
				}
				// Met les entrées vides à la fin de la liste
				List<Contact> emptyListContact = new ArrayList<Contact>();
				for (Iterator<Contact> it = listContact.iterator(); it.hasNext();) {
					Contact contact = it.next();
					if (contact.getLastname() == null | contact.getLastname().trim().isEmpty()) {
						emptyListContact.add(contact);
						it.remove();
					}
				}
				if (emptyListContact.size() > 0) {
					listContact.addAll(emptyListContact);
				}
			} else if (orderBy.equals("email")) {
				// Si ordre par email
				listContact.sort(Comparator.comparing(Contact::getEmail, String.CASE_INSENSITIVE_ORDER)
						.thenComparing(Contact::getFirstname, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Contact::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listContact);
				}
				// Met les entrées vides à la fin de la liste
				List<Contact> emptyListContact = new ArrayList<Contact>();
				for (Iterator<Contact> it = listContact.iterator(); it.hasNext();) {
					Contact contact = it.next();
					if (contact.getEmail() == null | contact.getEmail().trim().isEmpty()) {
						emptyListContact.add(contact);
						it.remove();
					}
				}
				if (emptyListContact.size() > 0) {
					listContact.addAll(emptyListContact);
				}
			} else if (orderBy.equals("phone")) {
				// Si ordre par phone
				listContact.sort(Comparator.comparing(Contact::getPhone, String.CASE_INSENSITIVE_ORDER)
						.thenComparing(Contact::getFirstname, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Contact::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listContact);
				}
				// Met les entrées vides à la fin de la liste
				List<Contact> emptyListContact = new ArrayList<Contact>();
				for (Iterator<Contact> it = listContact.iterator(); it.hasNext();) {
					Contact contact = it.next();
					if (contact.getPhone() == null | contact.getPhone().trim().isEmpty()) {
						emptyListContact.add(contact);
						it.remove();
					}
				}
				if (emptyListContact.size() > 0) {
					listContact.addAll(emptyListContact);
				}
			} else {
				// Si ordre par twitter
				listContact.sort(Comparator.comparing(Contact::getTwitter, String.CASE_INSENSITIVE_ORDER)
						.thenComparing(Contact::getFirstname, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Contact::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listContact);
				}
				// Met les entrées vides à la fin de la liste
				List<Contact> emptyListContact = new ArrayList<Contact>();
				for (Iterator<Contact> it = listContact.iterator(); it.hasNext();) {
					Contact contact = it.next();
					if (contact.getTwitter() == null | contact.getTwitter().trim().isEmpty()) {
						emptyListContact.add(contact);
						it.remove();
					}
				}
				if (emptyListContact.size() > 0) {
					listContact.addAll(emptyListContact);
				}
			}
		} else {
			// Ordre par défaut
			listContact.sort(Comparator.comparing(Contact::getFirstname, String.CASE_INSENSITIVE_ORDER)
					.thenComparing(Contact::getLastname, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Contact::getId));
		}
		return listContact;
	}

}
