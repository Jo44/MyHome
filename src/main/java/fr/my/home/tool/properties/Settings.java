package fr.my.home.tool.properties;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe en charge de récupérer en mémoire les paramètres de l'application via un fichier 'settings.properties'
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class Settings {
	private static final Logger logger = LogManager.getLogger(Settings.class);

	// Attributes

	private static Properties properties;

	// Methods

	/**
	 * Charge le fichier 'settings.properties'
	 */
	static {
		try {
			properties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			properties.load(classLoader.getResourceAsStream("settings.properties"));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}

	// Getters

	public static String getStringProperty(String key) {
		String parametre = properties.getProperty(key, null);
		return parametre;
	}

	public static int getIntProperty(String key) {
		String parametreStr = properties.getProperty(key, null);
		int parametre = Integer.parseInt(parametreStr);
		return parametre;
	}

	public static long getLongProperty(String key) {
		String parametreStr = properties.getProperty(key, null);
		long parametre = Long.parseLong(parametreStr);
		return parametre;
	}

	public static boolean getBooleanProperty(String key) {
		String parametreStr = properties.getProperty(key, null);
		boolean parametre = Boolean.parseBoolean(parametreStr);
		return parametre;
	}

}
