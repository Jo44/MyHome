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
 * @version 1.0
 */
public class SnakeScoreDAOTest {

	// Attributs
	private SnakeScoreDAO scoreDAO;
	private Timestamp now;

	// Constructeur
	public SnakeScoreDAOTest() {
		super();
		scoreDAO = new SnakeScoreDAO();
		now = Timestamp.valueOf(LocalDateTime.now());
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
		SnakeScore score = new SnakeScore(1, 333, now);
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
		int id = 0;
		SnakeScore score = null;
		try {
			score = new SnakeScore(1, 555, now);
			id = scoreDAO.add(score);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		} finally {
			if (id != 0) {
				try {
					scoreDAO.delete(score);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		}
		assertTrue(id != 0);
	}

	/**
	 * Ajoute un score (non-valide)
	 */
	@Test
	public void addKo() {
		int id;
		try {
			SnakeScore score = new SnakeScore(1, 666, null);
			id = scoreDAO.add(score);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		}
		assertTrue(id == 0);
	}

	/**
	 * Modifie un score (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		SnakeScore score = new SnakeScore(1, 777, now);
		try {
			int id = scoreDAO.add(score);
			try {
				score.setSaveDate(Timestamp.valueOf(LocalDateTime.now()));
				scoreDAO.update(score);
				valid = true;
			} finally {
				if (id != 0) {
					try {
						scoreDAO.delete(score);
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
	 * Modifie un score (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		SnakeScore score = new SnakeScore(1, 888, now);
		try {
			int id = scoreDAO.add(score);
			try {
				score.setSaveDate(null);
				scoreDAO.update(score);
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				if (id != 0) {
					try {
						scoreDAO.delete(score);
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
	 * Supprime un score (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		SnakeScore score = new SnakeScore(1, 999, now);
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
