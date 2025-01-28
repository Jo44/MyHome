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

import fr.my.home.bean.Sport;
import fr.my.home.dao.implementation.SportDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe SportDAOTest qui teste le stockage des activités sportives
 * 
 * @author Jonathan
 * @version 1.1
 */
public class SportDAOTest {

	/**
	 * Attributs
	 */

	private SportDAO sportDAO;

	/**
	 * Constructeur
	 */
	public SportDAOTest() {
		super();
		sportDAO = new SportDAO();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe SportDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste de toutes les activités sportives de l'utilisateur pour la période voulue (valide)
	 */
	@Test
	public void getAllSportsFromPeriodOk() {
		List<Sport> listSport = null;
		try {
			listSport = sportDAO.getAllSportsFromPeriod(0, Timestamp.valueOf(LocalDateTime.now().minusMonths(3L)),
					Timestamp.valueOf(LocalDateTime.now()));
		} catch (TechnicalException ex) {
			listSport = null;
		}
		assertNotNull(listSport);
	}

	/**
	 * Récupère l'activité sportive selon son ID et l'ID de l'utilisateur (valide)
	 */
	@Test
	public void getOneSportOk() {
		Sport sport = new Sport(1, "activity", Timestamp.valueOf(LocalDateTime.now()));
		try {
			sportDAO.add(sport);
			sport = sportDAO.getOneSport(sport.getId(), 1);
			sportDAO.delete(sport);
		} catch (FonctionnalException | TechnicalException ex) {
			sport = null;
		}
		assertNotNull(sport);
	}

	/**
	 * Récupère l'activité sportive selon son ID et l'ID de l'utilisateur (non-valide)
	 */
	@Test
	public void getOneSportKo() {
		Sport sport = new Sport();
		try {
			sport = sportDAO.getOneSport(0, 1);
		} catch (FonctionnalException | TechnicalException ex) {
			sport = null;
		}
		assertNull(sport);
	}

	/**
	 * Ajoute une activité sportive (valide)
	 */
	@Test
	public void addOk() {
		boolean valid = false;
		Sport sport = new Sport(1, "activity", Timestamp.valueOf(LocalDateTime.now()));
		try {
			sportDAO.add(sport);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				sportDAO.delete(sport);
			} catch (FonctionnalException | TechnicalException ex) {
				ex.printStackTrace();
			}
		}
		assertTrue(valid);
	}

	/**
	 * Ajoute une activité sportive (non-valide)
	 */
	@Test
	public void addKo() {
		boolean valid = true;
		Sport sport = new Sport(1, null, Timestamp.valueOf(LocalDateTime.now()));
		try {
			sportDAO.add(sport);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				sportDAO.delete(sport);
			} catch (FonctionnalException | TechnicalException ex) {}
		}
		assertFalse(valid);
	}

	/**
	 * Modifie une activité sportive (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		Sport sport = new Sport(1, "activity", Timestamp.valueOf(LocalDateTime.now()));
		try {
			sportDAO.add(sport);
			try {
				sport.setActivity("anotherActivity");
				sportDAO.update(sport);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					sportDAO.delete(sport);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		} catch (FonctionnalException | TechnicalException ex) {
			ex.printStackTrace();
		}
		assertTrue(valid);
	}

	/**
	 * Modifie une activité sportive (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		Sport sport = new Sport(1, "activity", Timestamp.valueOf(LocalDateTime.now()));
		try {
			sportDAO.add(sport);
			try {
				sport.setActivity(null);
				sportDAO.update(sport);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					sport.setActivity("activity");
					sportDAO.delete(sport);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		} catch (FonctionnalException | TechnicalException ex) {
			ex.printStackTrace();
		}
		assertFalse(valid);
	}

	/**
	 * Supprime une activité sportive (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		Sport sport = new Sport(1, "activity", Timestamp.valueOf(LocalDateTime.now()));
		try {
			sportDAO.add(sport);
			sportDAO.delete(sport);
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
		System.out.println("Fin des tests de la classe SportDAO");
	}

}
