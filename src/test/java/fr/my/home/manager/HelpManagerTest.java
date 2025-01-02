package fr.my.home.manager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.my.home.bean.User;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.global.ReCaptchaException;

/**
 * Classe HelpManagerTest qui teste l'envoi du message d'aide à l'administrateur
 * 
 * @author Jonathan
 * @version 1.0
 */
public class HelpManagerTest {

	// Attributs
	private HelpManager helpMgr;
	private Timestamp ts;

	// Constructeur
	public HelpManagerTest() {
		super();
		helpMgr = new HelpManager();
		ts = Timestamp.valueOf(LocalDateTime.now());
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe HelpManager");
	}

	// Test des méthodes

	/**
	 * Génère le contenu de l'email de demande d'aide (valide)
	 */
	@Test
	public void generateHelpEmailContentOk() {
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		String message = "test";
		String lang = "fr";

		String emailContent = helpMgr.generateHelpEmailContent(user, message, lang);
		assertTrue(emailContent != null && !emailContent.trim().isEmpty());
	}

	/**
	 * Génère le contenu de l'email de demande d'aide (non-valide)
	 */
	@Test
	public void generateHelpEmailContentKo() {
		boolean valid = true;
		User user = null;
		String message = null;
		String lang = null;

		try {
			@SuppressWarnings("unused")
			String emailContent = helpMgr.generateHelpEmailContent(user, message, lang);
		} catch (NullPointerException npe) {
			valid = false;
		}
		assertFalse(valid);
	}

	/**
	 * Vérifie le message, le reCaptcha et l'utilisateur puis envoi le message à l'administrateur (non-valide)
	 */
	@Test
	public void sendHelpMessageKo() {
		boolean valid = true;
		User user = new User("name", "pass", "email@domain.com", null, "token", true, null, null, null, ts);
		String message = "test";
		String reCaptcha = "reCaptcha";

		try {
			helpMgr.sendHelpMessage(user, message, reCaptcha);
		} catch (FonctionnalException fe) {
			if (fe instanceof ReCaptchaException) {
				valid = false;
			}
		}
		assertFalse(valid);
	}

	/**
	 * Fin des tests
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Fin des tests de la classe HelpManager");
	}

}
