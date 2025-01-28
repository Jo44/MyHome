package fr.my.home.tool.properties;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Classe SettingsTest qui charge en mémoire les paramètres de l'application via un fichier 'settings.properties'
 * 
 * @author Jonathan
 * @version 1.1
 */
public class SettingsTest {

	/**
	 * Constructeur
	 */
	public SettingsTest() {
		super();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe SettingsTest");
	}

	// Test des méthodes

	/**
	 * Récupère un paramètre String (valide)
	 */
	@Test
	public void getStringPropertyOk() {
		String parametre = null;
		parametre = Settings.getStringProperty("global.test.string");
		assertTrue(parametre.equals("Test"));
	}

	/**
	 * Récupère un paramètre String (non-valide)
	 */
	@Test
	public void getStringPropertyKo() {
		String parametre = null;
		parametre = Settings.getStringProperty("global.wrong.test.string");
		assertNull(parametre);
	}

	/**
	 * Récupère un paramètre int (valide)
	 */
	@Test
	public void getIntPropertyOk() {
		int parametre = Settings.getIntProperty("global.test.int");
		assertTrue(parametre == 42);
	}

	/**
	 * Récupère un paramètre int (non-valide)
	 */
	@Test
	public void getIntPropertyKo() {
		int parametre = 0;
		try {
			parametre = Settings.getIntProperty("global.wrong.test.int");
		} catch (NumberFormatException nfe) {
			parametre = 1;
		}
		assertTrue(parametre == 1);
	}

	/**
	 * Récupère un paramètre long (valide)
	 */
	@Test
	public void getLongPropertyOk() {
		long parametre = Settings.getLongProperty("global.test.long");
		assertTrue(parametre == 42424242424242L);
	}

	/**
	 * Récupère un paramètre long (non-valide)
	 */
	@Test
	public void getLongPropertyKo() {
		long parametre = 0L;
		try {
			parametre = Settings.getLongProperty("global.wrong.test.long");
		} catch (NumberFormatException nfe) {
			parametre = 1L;
		}
		assertTrue(parametre == 1L);
	}

	/**
	 * Fin des tests
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Fin des tests de la classe SettingsTest");
	}

}
