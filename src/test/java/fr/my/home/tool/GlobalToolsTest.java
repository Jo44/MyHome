package fr.my.home.tool;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonSyntaxException;

import fr.my.home.bean.api.ObjectIPAPI;
import fr.my.home.bean.api.ObjectReCaptcha;
import fr.my.home.exception.FonctionnalException;

/**
 * Classe GlobalToolsTest qui regroupe différents outils
 * 
 * @author Jonathan
 * @version 1.0
 */
public class GlobalToolsTest {

	// Constructeur
	public GlobalToolsTest() {
		super();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe GlobalToolsTest");
	}

	// Test des méthodes

	/**
	 * Vérifie si la langue est 'fr' (valide)
	 */
	@Test
	public void validLanguageFrOk() {
		String validLang = null;
		String lang = "fr";
		validLang = GlobalTools.validLanguage(lang);
		assertTrue(validLang.equals("fr"));
	}

	/**
	 * Vérifie si la langue est 'en' (valide)
	 */
	@Test
	public void validLanguageEnOk() {
		String validLang = null;
		String lang = null;
		validLang = GlobalTools.validLanguage(lang);
		assertTrue(validLang.equals("en"));
	}

	/**
	 * Permet de mettre la première lettre de chaque mot en majuscule (valide)
	 */
	@Test
	public void capitalizeFirstLettersOk() {
		String str = "ceci est un test";
		str = GlobalTools.capitalizeFirstLetters(str);
		assertTrue(str.equals("Ceci Est Un Test"));
	}

	/**
	 * Permet de hasher un string en MD5 (valide)
	 */
	@Test
	public void hashOk() {
		String hash = "test";
		try {
			hash = GlobalTools.hash(hash);
		} catch (FonctionnalException fex) {
			hash = "erreur";
		}
		assertTrue(hash.equals("98f6bcd4621d373cade4e832627b4f6"));
	}

	/**
	 * Récupère la réponse HTML en fonction d'une requête GET à partir de son URL (valide)
	 */
	@Test
	public void getHTMLOk() {
		String html = null;
		String url = "https://www.google.fr";
		try {
			html = GlobalTools.getHTML(url);
		} catch (IOException ioe) {
			html = null;
		}
		assertNotNull(html);
	}

	/**
	 * Récupère la réponse HTML en fonction d'une requête GET à partir de son URL (non-valide)
	 */
	@Test
	public void getHTMLKo() {
		String html = "";
		String url = "badUrl";
		try {
			html = GlobalTools.getHTML(url);
		} catch (IOException ioe) {
			html = null;
		}
		assertNull(html);
	}

	/**
	 * Récupère la réponse HTML en fonction d'une requête POST (valide)
	 */
	@Test
	public void postHTMLOk() {
		String html = null;
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("request-url", "https://www.google.com/recaptcha/api/siteverify");
		hmap.put("user-agent", "Mozilla/5.0");
		hmap.put("accept-language", "en-US,en;q=0.5");
		hmap.put("content", "test");
		try {
			html = GlobalTools.postHTML(hmap);
		} catch (IOException ioe) {
			html = null;
		}
		assertNotNull(html);
	}

	/**
	 * Récupère la réponse HTML en fonction d'une requête POST (non-valide)
	 */
	@Test
	public void postHTMLKo() {
		String html = "";
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("request-url", "https://www.google.com");
		hmap.put("user-agent", "Mozilla/5.0");
		hmap.put("accept-language", "en-US,en;q=0.5");
		hmap.put("content", "test");
		try {
			html = GlobalTools.postHTML(hmap);
		} catch (IOException ioe) {
			html = null;
		}
		assertNull(html);
	}

	/**
	 * Parse un string de Json en ObjectIPAPI (valide)
	 */
	@Test
	public void getObjectIPAPIOk() {
		ObjectIPAPI objectIPAPI = null;
		String jsonStr = "{\"status\":\"success\",\"country\":\"United States\",\"countryCode\":\"US\",\"region\":\"CA\",\"regionName\":\"California\",\"city\":\"Mountain View\",\"zip\":\"94043\",\"lat\":37.4043,\"lon\":-122.0748,\"timezone\":\"America/Los_Angeles\",\"isp\":\"Google LLC\",\"org\":\"Google LLC\",\"as\":\"AS15169 Google LLC\",\"query\":\"216.58.212.206\"}";
		try {
			objectIPAPI = GlobalTools.getObjectIPAPI(jsonStr);
		} catch (JsonSyntaxException jse) {
			objectIPAPI = null;
		}
		assertNotNull(objectIPAPI);
	}

	/**
	 * Parse un string de Json en ObjectIPAPI (non-valide)
	 */
	@Test
	public void getObjectIPAPIKo() {
		ObjectIPAPI objectIPAPI = new ObjectIPAPI();
		String jsonStr = "{\"fail\"}";
		try {
			objectIPAPI = GlobalTools.getObjectIPAPI(jsonStr);
		} catch (JsonSyntaxException jse) {
			objectIPAPI = null;
		}
		assertNull(objectIPAPI);
	}

	/**
	 * Parse un string de Json en ObjectReCaptcha (valide)
	 */
	@Test
	public void getObjectReCaptchaOk() {
		ObjectReCaptcha objectReCaptcha = null;
		String jsonStr = "{  \"success\": true,  \"challenge_ts\": \"2021-06-18T14:28:22Z\",  \"hostname\": \"localhost\"}";
		try {
			objectReCaptcha = GlobalTools.getObjectReCaptcha(jsonStr);
		} catch (JsonSyntaxException jse) {
			objectReCaptcha = null;
		}
		assertNotNull(objectReCaptcha);
	}

	/**
	 * Parse un string de Json en ObjectReCaptcha (non-valide)
	 */
	@Test
	public void getObjectReCaptchaKo() {
		ObjectReCaptcha objectReCaptcha = new ObjectReCaptcha();
		String jsonStr = "{\"fail\"}";
		try {
			objectReCaptcha = GlobalTools.getObjectReCaptcha(jsonStr);
		} catch (JsonSyntaxException jse) {
			objectReCaptcha = null;
		}
		assertNull(objectReCaptcha);
	}

	/**
	 * Vérifie le reCaptcha récupéré du formulaire (non-valide)
	 */
	@Test
	public void checkReCaptchaKo() {
		boolean valid = true;
		String reCaptcha = "wrongReCaptcha";
		valid = GlobalTools.checkReCaptcha(reCaptcha);
		assertFalse(valid);
	}

	/**
	 * Fin des tests
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Fin des tests de la classe GlobalToolsTest");
	}

}
