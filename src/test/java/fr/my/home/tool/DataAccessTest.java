package fr.my.home.tool;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.my.home.exception.TechnicalException;

/**
 * Classe DataAccessTest qui teste la connexion à la base de données (Hibernate)
 * 
 * @author Jonathan
 * @version 1.0
 */
public class DataAccessTest {

	// Constructeur
	public DataAccessTest() {
		super();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe DataAccessTest");
	}

	// Test des méthodes

	/**
	 * Retourne une session Hibernate prête à l'emploi (valide)
	 */
	@Test
	public void openSessionOk() {
		Session session;
		try {
			session = DatabaseAccess.getInstance().openSession();
		} catch (TechnicalException tex) {
			session = null;
		}
		assertNotNull(session);
	}

	/**
	 * Valide la transaction (valide)
	 */
	@Test
	public void validateSessionOk() {
		boolean valid = true;
		Session session;
		try {
			session = DatabaseAccess.getInstance().openSession();
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException | TechnicalException ex) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test la connection Hibernate (valide)
	 */
	@Test
	public void testConnectionOk() {
		boolean valid = false;
		valid = DatabaseAccess.getInstance().testConnection();
		assertTrue(valid);
	}

	/**
	 * Fin des tests
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Fin des tests de la classe DataAccessTest");
	}

}
