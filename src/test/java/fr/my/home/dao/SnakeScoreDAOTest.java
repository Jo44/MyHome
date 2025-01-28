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

import fr.my.home.bean.SnakeScore;
import fr.my.home.dao.implementation.SnakeScoreDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe SnakeSportDAOTest qui teste le stockage des scores de Snake
 * 
 * @author Jonathan
 * @version 1.1
 */
public class SnakeScoreDAOTest {

	/**
	 * Attributs
	 */

	private SnakeScoreDAO scoreDAO;

	/**
	 * Constructeur
	 */
	public SnakeScoreDAOTest() {
		super();
		scoreDAO = new SnakeScoreDAO();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe SnakeScoreDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste des 3 meilleurs scores tous utilisateurs confondus (valide)
	 */
	@Test
	public void getWROk() {
		List<SnakeScore> listScore = null;
		try {
			listScore = scoreDAO.getWR();
		} catch (TechnicalException ex) {
			listScore = null;
		}
		assertNotNull(listScore);
	}

	/**
	 * Récupère le meilleur score de l'utilisateur (valide)
	 */
	@Test
	public void getPBOk() {
		SnakeScore score = new SnakeScore(1, 333, Timestamp.valueOf(LocalDateTime.now()));
		try {
			scoreDAO.add(score);
			score = scoreDAO.getPB(score.getIdUser());
			scoreDAO.delete(score);
		} catch (FonctionnalException | TechnicalException ex) {
			score = null;
		}
		assertNotNull(score);
	}

	/**
	 * Récupère le meilleur score de l'utilisateur (non-valide)
	 */
	@Test
	public void getOneSportKo() {
		SnakeScore score = new SnakeScore();
		try {
			score = scoreDAO.getPB(0);
		} catch (TechnicalException ex) {
			score = null;
		}
		assertNull(score);
	}

	/**
	 * Ajoute un score (valide)
	 */
	@Test
	public void addOk() {
		boolean valid = false;
		SnakeScore score = new SnakeScore(1, 555, Timestamp.valueOf(LocalDateTime.now()));
		try {
			scoreDAO.add(score);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				scoreDAO.delete(score);
			} catch (FonctionnalException | TechnicalException ex) {
				ex.printStackTrace();
			}
		}
		assertTrue(valid);
	}

	/**
	 * Ajoute un score (non-valide)
	 */
	@Test
	public void addKo() {
		boolean valid = true;
		SnakeScore score = new SnakeScore(1, 666, null);
		try {
			scoreDAO.add(score);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				scoreDAO.delete(score);
			} catch (FonctionnalException | TechnicalException ex) {}
		}
		assertFalse(valid);
	}

	/**
	 * Modifie un score (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		SnakeScore score = new SnakeScore(1, 777, Timestamp.valueOf(LocalDateTime.now()));
		try {
			scoreDAO.add(score);
			try {
				score.setSaveDate(Timestamp.valueOf(LocalDateTime.now()));
				scoreDAO.update(score);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					scoreDAO.delete(score);
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
	 * Modifie un score (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
		SnakeScore score = new SnakeScore(1, 888, ts);
		try {
			scoreDAO.add(score);
			try {
				score.setSaveDate(null);
				scoreDAO.update(score);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					score.setSaveDate(ts);
					scoreDAO.delete(score);
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
	 * Supprime un score (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		SnakeScore score = new SnakeScore(1, 999, Timestamp.valueOf(LocalDateTime.now()));
		try {
			scoreDAO.add(score);
			scoreDAO.delete(score);
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
		System.out.println("Fin des tests de la classe SnakeScoreDAO");
	}

}
