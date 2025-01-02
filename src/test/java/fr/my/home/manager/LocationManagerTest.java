package fr.my.home.manager;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.my.home.bean.api.ObjectIPAPI;
import fr.my.home.exception.FonctionnalException;

/**
 * Classe LocationManagerTest qui teste la localisation d'une IP ou nom de domaine
 * 
 * @author Jonathan
 * @version 1.0
 */
public class LocationManagerTest {

	// Attributs
	LocationManager locMgr;

	// Constructeur
	public LocationManagerTest() {
		super();
		locMgr = new LocationManager();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe LocationManager");
	}

	// Test des méthodes

	/**
	 * Formatte le string pour enlever le protocol https/http et le sous domaine www si présents (valide)
	 */
	@Test
	public void formatLocStringOk() {
		String inputStr = "https://lecnam.net/accueil";
		String response = locMgr.formatLocString(inputStr);
		assertTrue(response.equals("lecnam.net"));
	}

	/**
	 * Requête le site ip-api.com pour renvoyer un objet IPAPI si possible (valide)
	 */
	@Test
	public void getIPAPIFunctionOk() {
		String inputStr = "lecnam.net";
		ObjectIPAPI objectIPAPI;
		try {
			objectIPAPI = locMgr.getIPAPIFunction(inputStr);
		} catch (FonctionnalException fe) {
			objectIPAPI = null;
		}
		assertTrue(objectIPAPI.getStatus().equals("success"));
	}

	/**
	 * Requête le site ip-api.com pour renvoyer un objet IPAPI si possible (valide mais failed)
	 */
	@Test
	public void getIPAPIFunctionOkFailed() {
		String inputStr = "https://lecnam.net/accueil";
		ObjectIPAPI objectIPAPI;
		try {
			objectIPAPI = locMgr.getIPAPIFunction(inputStr);
		} catch (FonctionnalException fex) {
			objectIPAPI = null;
		}
		assertTrue(objectIPAPI.getStatus().equals("fail"));
	}

	/**
	 * Requête le site ip-api.com pour renvoyer un objet IPAPI si possible (non-valide)
	 */
	@Test
	public void getIPAPIFunctionKo() {
		String inputStr = "";
		ObjectIPAPI objectIPAPI;
		try {
			objectIPAPI = locMgr.getIPAPIFunction(inputStr);
		} catch (FonctionnalException fex) {
			objectIPAPI = null;
		}
		assertNull(objectIPAPI);
	}

	/**
	 * Fin des tests
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Fin des tests de la classe LocationManager");
	}

}
