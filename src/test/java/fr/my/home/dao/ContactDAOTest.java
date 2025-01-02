package fr.my.home.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.my.home.bean.Contact;
import fr.my.home.dao.implementation.ContactDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe ContactDAOTest qui teste le stockage des contacts
 * 
 * @author Jonathan
 * @version 1.0
 */
public class ContactDAOTest {

	// Attributs
	private ContactDAO contactDAO;
	private Timestamp ts;

	// Constructeur
	public ContactDAOTest() {
		super();
		contactDAO = new ContactDAO();
		ts = Timestamp.valueOf(LocalDateTime.now());
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe ContactDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste de tous les contacts de l'utilisateur (valide)
	 */
	@Test
	public void getAllContactsOk() {
		List<Contact> listContact = null;
		try {
			listContact = contactDAO.getAllContacts(0);
		} catch (TechnicalException ex) {
			listContact = null;
		}
		assertNotNull(listContact);
	}

	/**
	 * Récupère le contact selon son ID et l'ID de l'utilisateur (valide)
	 */
	@Test
	public void getOneContactOk() {
		Contact contact = new Contact(1, "firstname", "lastname", "email@domain.com", "0600112233", "monTwitter", ts);
		try {
			contactDAO.add(contact);
			contact = contactDAO.getOneContact(contact.getId(), 1);
			contactDAO.delete(contact);
		} catch (FonctionnalException | TechnicalException ex) {
			contact = null;
		}
		assertNotNull(contact);
	}

	/**
	 * Récupère le contact selon son ID et l'ID de l'utilisateur (non-valide)
	 */
	@Test
	public void getOneContactKo() {
		Contact contact = new Contact();
		try {
			contact = contactDAO.getOneContact(0, 1);
		} catch (FonctionnalException | TechnicalException ex) {
			contact = null;
		}
		assertNull(contact);
	}

	/**
	 * Ajoute un contact (valide)
	 */
	@Test
	public void addOk() {
		int id = 0;
		Contact contact = null;
		try {
			contact = new Contact(1, "firstname", "lastname", "email@domain.com", "0600112233", "monTwitter", ts);
			id = contactDAO.add(contact);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		} finally {
			if (id != 0) {
				try {
					contactDAO.delete(contact);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		}
		assertTrue(id != 0);
	}

	/**
	 * Ajoute un contact (non-valide)
	 */
	@Test
	public void addKo() {
		int id;
		try {
			Contact contact = new Contact(1, null, null, null, null, null, null);
			id = contactDAO.add(contact);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		}
		assertTrue(id == 0);
	}

	/**
	 * Modifie un contact (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		Contact contact = new Contact(1, "firstname", "lastname", "email@domain.com", "0600112233", "monTwitter", ts);
		try {
			int id = contactDAO.add(contact);
			try {
				contact.setFirstname("anotherFirstname");
				contactDAO.update(contact);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				if (id != 0) {
					try {
						contactDAO.delete(contact);
					} catch (FonctionnalException | TechnicalException ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (FonctionnalException | TechnicalException ex) {
			ex.printStackTrace();
		}
		assertTrue(valid);
	}

	/**
	 * Modifie un contact (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		Contact contact = new Contact(1, "Jo", null, null, null, null, ts);
		try {
			int id = contactDAO.add(contact);
			try {
				contact.setFirstname(null);
				contactDAO.update(contact);
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				if (id != 0) {
					try {
						contactDAO.delete(contact);
					} catch (FonctionnalException | TechnicalException ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (FonctionnalException | TechnicalException ex) {
			ex.printStackTrace();
		}
		assertFalse(valid);
	}

	/**
	 * Supprime un contact (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		Contact contact = new Contact(1, "firstname", "lastname", "email@domain.com", "0600112233", "monTwitter", ts);
		try {
			contactDAO.add(contact);
			contactDAO.delete(contact);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Fin des tests
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Fin des tests de la classe ContactDAO");
	}

}
