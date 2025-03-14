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

import fr.my.home.bean.Weight;
import fr.my.home.dao.implementation.WeightDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe SportDAOTest qui teste le stockage des poids
 * 
 * @author Jonathan
 * @version 1.1
 */
public class WeightDAOTest {

	/**
	 * Attributs
	 */

	private WeightDAO weightDAO;

	/**
	 * Constructeur
	 */
	public WeightDAOTest() {
		super();
		weightDAO = new WeightDAO();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe WeightDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste de tous les poids de l'utilisateur pour la période voulue (valide)
	 */
	@Test
	public void getAllWeightsFromPeriodOk() {
		List<Weight> listWeight = null;
		try {
			listWeight = weightDAO.getAllWeightsFromPeriod(0, Timestamp.valueOf(LocalDateTime.now().minusMonths(3L)),
					Timestamp.valueOf(LocalDateTime.now()));
		} catch (TechnicalException ex) {
			listWeight = null;
		}
		assertNotNull(listWeight);
	}

	/**
	 * Récupère le poids selon son ID et l'ID de l'utilisateur (valide)
	 */
	@Test
	public void getOneWeightOk() {
		Weight weight = new Weight(1, 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			weightDAO.add(weight);
			weight = weightDAO.getOneWeight(weight.getId(), 1);
			weightDAO.delete(weight);
		} catch (FonctionnalException | TechnicalException ex) {
			weight = null;
		}
		assertNotNull(weight);
	}

	/**
	 * Récupère le poids selon son ID et l'ID de l'utilisateur (non-valide)
	 */
	@Test
	public void getOneWeightKo() {
		Weight weight = new Weight();
		try {
			weight = weightDAO.getOneWeight(0, 1);
		} catch (FonctionnalException | TechnicalException ex) {
			weight = null;
		}
		assertNull(weight);
	}

	/**
	 * Ajoute un poids (valide)
	 */
	@Test
	public void addOk() {
		boolean valid = false;
		Weight weight = new Weight(1, 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			weightDAO.add(weight);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				weightDAO.delete(weight);
			} catch (FonctionnalException | TechnicalException ex) {
				ex.printStackTrace();
			}
		}
		assertTrue(valid);
	}

	/**
	 * Ajoute un poids (non-valide)
	 */
	@Test
	public void addKo() {
		boolean valid = true;
		Weight weight = new Weight(1, 0, null);
		try {
			weightDAO.add(weight);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				weightDAO.delete(weight);
			} catch (FonctionnalException | TechnicalException ex) {}
		}
		assertFalse(valid);
	}

	/**
	 * Modifie un poids (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		Weight weight = new Weight(1, 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			weightDAO.add(weight);
			try {
				weight.setSaveDate(Timestamp.valueOf(LocalDateTime.now().minusMonths(3L)));
				weightDAO.update(weight);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					weightDAO.delete(weight);
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
	 * Modifie un poids (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
		Weight weight = new Weight(1, 0, ts);
		try {
			weightDAO.add(weight);
			try {
				weight.setSaveDate(null);
				weightDAO.update(weight);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					weight.setSaveDate(ts);
					weightDAO.delete(weight);
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
	 * Supprime un poids (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		Weight weight = new Weight(1, 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			weightDAO.add(weight);
			weightDAO.delete(weight);
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
		System.out.println("Fin des tests de la classe WeightDAO");
	}

}
