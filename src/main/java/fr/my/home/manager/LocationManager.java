package fr.my.home.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.api.ObjectIPAPI;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Settings;

/**
 * Manager qui prends en charge la gestion de la localisation d'une IP ou nom de domaine
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class LocationManager {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(LocationManager.class);

	/**
	 * Constructeur
	 */
	public LocationManager() {
		super();
	}

	/**
	 * Formatte le string pour enlever le protocol https/http et le sous domaine www si présents
	 * 
	 * @param inputStr
	 * @return String
	 */
	public String formatLocString(String inputStr) {
		String formatStr = null;
		// Supprime le début (inutile) de l'url saisie
		if (inputStr != null && !inputStr.isEmpty()) {
			if (inputStr.indexOf("https://www.") != -1) {
				formatStr = inputStr.replace("https://www.", "");
			} else if (inputStr.indexOf("http://www.") != -1) {
				formatStr = inputStr.replace("http://www.", "");
			} else if (inputStr.indexOf("https://") != -1) {
				formatStr = inputStr.replace("https://", "");
			} else if (inputStr.indexOf("http://") != -1) {
				formatStr = inputStr.replace("http://", "");
			} else if (inputStr.indexOf("www.") != -1) {
				formatStr = inputStr.replace("www.", "");
			} else {
				formatStr = inputStr;
			}
		}
		// Supprime la fin (inutile) de l'url saisie
		if (formatStr != null && !formatStr.isEmpty()) {
			if (formatStr.indexOf("/") != -1) {
				formatStr = formatStr.substring(0, formatStr.indexOf("/"));
			}
			// Trim l'url final
			formatStr = formatStr.trim();
		}
		return formatStr;
	}

	/**
	 * Requête le site ip-api.com pour renvoyer un objet IPAPI si possible
	 * 
	 * @param inputSearch
	 * @return ObjectIPAPI
	 * @throws FonctionnalException
	 */
	public ObjectIPAPI getIPAPIFunction(String inputSearch) throws FonctionnalException {
		ObjectIPAPI objectIPAPI = null;
		// Si l'input n'est pas vide, récupération de l'objet IPAPI
		if (inputSearch != null && !inputSearch.isEmpty()) {
			// Récupère l'ObjectIPAPI à partir d'une requete à IP-API
			objectIPAPI = getObjectIPAPI(inputSearch);
		} else {
			throw new FonctionnalException("L'adresse IP / web est manquante");
		}
		return objectIPAPI;
	}

	/**
	 * Récupère l'objet IP-API à partir de l'input via ip-api.com -> http://ip-api.com/json/INPUT
	 * 
	 * @param input
	 * @return ObjectIPAPI
	 * @throws FonctionnalException
	 */
	private ObjectIPAPI getObjectIPAPI(String input) throws FonctionnalException {
		ObjectIPAPI objectIPAPI = null;
		try {
			// Récupère l'url de l'IP-API
			String baseUrl = Settings.getStringProperty("ipapi.url");
			// Récupère le code HTML de la réponse de l'IP-API
			String responseAPI = GlobalTools.getHTML(baseUrl + input);
			// Parse la réponse JSON en ObjectIPAPI
			objectIPAPI = GlobalTools.getObjectIPAPI(responseAPI);
		} catch (Exception ex) {
			throw new FonctionnalException("Impossible de récupérer l'objet IP-API");
		}
		logger.debug("Récupération de l'objet IP-API réussi");
		return objectIPAPI;
	}

}
