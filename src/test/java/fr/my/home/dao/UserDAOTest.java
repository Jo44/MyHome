package fr.my.home.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.my.home.bean.User;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe UserDAOTest qui teste le stockage des utilisateurs
 * 
 * @author Jonathan
 * @version 1.0
 */
public class UserDAOTest {

	// Attributs
	private UserDAO userDAO;
	private Timestamp ts;

	// Constructeur
	public UserDAOTest() {
		super();
		userDAO = new UserDAO();
		ts = Timestamp.valueOf(LocalDateTime.now());
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe UserDAO");
	}

	// Test des méthodes

	/**
	 * Récupère l'utilisateur associé au couple username/password (valide)
	 */
	@Test
	public void getUserOk() {
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		try {
			userDAO.add(user);
			user = userDAO.getUser("name", "pass");
			userDAO.delete(user);
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNotNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au couple username/password (non-valide)
	 */
	@Test
	public void getUserKo() {
		User user = new User();
		try {
			user = userDAO.getUser("name", "wrongPass");
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au username (valide)
	 */
	@Test
	public void getUserByUsernameOk() {
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		try {
			userDAO.add(user);
			user = userDAO.getUserByUsername("name");
			userDAO.delete(user);
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNotNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au username (non-valide)
	 */
	@Test
	public void getUserByUsernameKo() {
		User user = new User();
		try {
			user = userDAO.getUserByUsername("wrongName");
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNull(user);
	}

	/**
	 * Récupère l'utilisateur associé à l'email (valide)
	 */
	@Test
	public void getUserByEmailOk() {
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		try {
			userDAO.add(user);
			user = userDAO.getUserByEmail("email@domain.com");
			userDAO.delete(user);
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNotNull(user);
	}

	/**
	 * Récupère l'utilisateur associé à l'email (non-valide)
	 */
	@Test
	public void getUserByEmailKo() {
		User user = new User();
		try {
			user = userDAO.getUserByEmail("wrong.email@domain.com");
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au token de 'Remember Me' (valide)
	 */
	@Test
	public void getUserByRememberMeTokenOk() {
		User user = new User("name", "pass", "email@domain.com", "token", "token", true, null, null, null, ts);
		try {
			userDAO.add(user);
			user = userDAO.getUserByRememberMeToken("token");
			userDAO.delete(user);
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNotNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au token de 'Remember Me' (non-valide)
	 */
	@Test
	public void getUserByRememberMeTokenKo() {
		User user = new User();
		try {
			user = userDAO.getUserByRememberMeToken("wrongToken");
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au token de validation (valide)
	 */
	@Test
	public void getUserByValidationTokenOk() {
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		try {
			userDAO.add(user);
			user = userDAO.getUserByValidationToken("token");
			userDAO.delete(user);
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNotNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au token de validation (non-valide)
	 */
	@Test
	public void getUserByValidationTokenKo() {
		User user = new User();
		try {
			user = userDAO.getUserByValidationToken("wrongToken");
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au token de ré-initialisation (valide)
	 */
	@Test
	public void getUserByReInitTokenOk() {
		User user = new User("name", "pass", "email@domain.com", null, "token", true, "token", null, null, ts);
		try {
			userDAO.add(user);
			user = userDAO.getUserByReInitToken("token");
			userDAO.delete(user);
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNotNull(user);
	}

	/**
	 * Récupère l'utilisateur associé au token de ré-initialisation (non-valide)
	 */
	@Test
	public void getUserByReInitTokenKo() {
		User user = new User();
		try {
			user = userDAO.getUserByReInitToken("wrongToken");
		} catch (FonctionnalException | TechnicalException ex) {
			user = null;
		}
		assertNull(user);
	}

	/**
	 * Ajoute un utilisateur (valide)
	 */
	@Test
	public void addOk() {
		int id = 0;
		User user = null;
		try {
			user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
			id = userDAO.add(user);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		} finally {
			if (id != 0) {
				try {
					userDAO.delete(user);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		}
		assertTrue(id != 0);
	}

	/**
	 * Ajoute un utilisateur (non-valide)
	 */
	@Test
	public void addKo() {
		int id;
		try {
			User user = new User();
			id = userDAO.add(user);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		}
		assertTrue(id == 0);
	}

	/**
	 * Modifie un utilisateur (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		try {
			int id = userDAO.add(user);
			try {
				user.setPass("anotherPass");
				userDAO.update(user);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				if (id != 0) {
					try {
						userDAO.delete(user);
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
	 * Modifie un utilisateur (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		try {
			int id = userDAO.add(user);
			try {
				user.setPass(null);
				userDAO.update(user);
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				if (id != 0) {
					try {
						userDAO.delete(user);
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
	 * Supprime un utilisateur (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		try {
			userDAO.add(user);
			userDAO.delete(user);
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
		System.out.println("Fin des tests de la classe UserDAO");
	}

}
