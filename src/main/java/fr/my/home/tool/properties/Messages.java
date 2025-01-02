package fr.my.home.tool.properties;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe en charge de récupérer en mémoire les messages de l'application via des fichiers 'messages.properties' + localisation
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class Messages {
	private static final Logger logger = LogManager.getLogger(Messages.class);

	// Attributes

	private static Properties enProperties;
	private static Properties frProperties;

	// Methods

	/**
	 * Charge le fichier 'messages.properties' + localisation
	 */
	static {
		try {
			enProperties = new Properties();
			frProperties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			enProperties.load(classLoader.getResourceAsStream("messages.properties"));
			frProperties.load(classLoader.getResourceAsStream("messages_fr.properties"));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}

	// Getters

	public static String getProperty(String key, String lang) {
		String parametre = "";
		if (lang != null && lang.equals("fr")) {
			parametre = frProperties.getProperty(key, null);
		} else {
			parametre = enProperties.getProperty(key, null);
		}
		return parametre;
	}

}
