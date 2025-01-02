package fr.my.home.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import fr.my.home.bean.api.ObjectIPAPI;
import fr.my.home.bean.api.ObjectReCaptcha;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.tool.properties.Settings;

/**
 * Classe regroupant différents outils (formatDateToString / formattedYTTitle / validLanguage / getMaxRows / capitalizeFirstLetters / hash / getHtml /
 * postHtml / getObjectIPAPI / getObjectReCaptcha / checkReCaptcha / send Email)
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class GlobalTools {
	private static final Logger logger = LogManager.getLogger(GlobalTools.class);

	// Attributes

	private static final String SMTP_HOSTNAME = Settings.getStringProperty("smtp.hostname");
	private static final int SMTP_PORT = Settings.getIntProperty("smtp.port");
	private static final boolean SMTP_SSL = Settings.getBooleanProperty("smtp.ssl");
	private static final String SMTP_USER = Settings.getStringProperty("smtp.user");
	private static final String SMTP_PASS = Settings.getStringProperty("smtp.pass");

	// Methods

	/**
	 * Format LocalDateTime to String "yyyy-MM-ddTHH:mm"
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatDateToString(LocalDateTime date) {
		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");
		return date.format(formatDate) + "T" + date.format(formatTime);
	}

	/**
	 * Format String "yyyy-MM-ddTHH:mm" to Timestamp
	 * 
	 * @param date
	 * @return Timestamp
	 */
	public static Timestamp formatStringToTimestamp(String date) {
		Timestamp timestamp = null;
		try {
			date = date.replace("T", " ");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
			timestamp = Timestamp.valueOf(dateTime);
			if (timestamp == null) {
				throw new FonctionnalException("Datetime non valide");
			}
		} catch (Exception ex) {
			timestamp = Timestamp.valueOf(LocalDateTime.now());
		}
		return timestamp;
	}

	/**
	 * Return the default actual timestamp
	 * 
	 * @return Timestamp
	 */
	public static Timestamp defaultTimestamp() {
		return Timestamp.valueOf(LocalDateTime.now());
	}

	/**
	 * Format YouTube Title
	 * 
	 * @param title
	 * @return String
	 */
	public static String formattedYTTitle(String title) {
		String formattedYTTitle = null;
		if (title != null && !title.trim().isEmpty()) {
			formattedYTTitle = title.trim().replace('\'', '\"');
		}
		return formattedYTTitle;
	}

	/**
	 * Vérifie si la langue est 'fr ou 'en' (sinon anglais par défaut)
	 * 
	 * @param lang
	 * @return String
	 */
	public static String validLanguage(String lang) {
		String validLang = "";
		if (lang != null && lang.equals("fr")) {
			validLang = "fr";
		} else {
			validLang = "en";
		}
		return validLang;
	}

	/**
	 * Récupère le nombre de lignes max d'un tableau à afficher en session à partir de la requête - Valeurs possibles : 10 (par défaut) / 25 / 50 / -1
	 * 
	 * @param request
	 * @return int
	 */
	public static int getMaxRows(HttpServletRequest request) {
		int maxRows;
		try {
			maxRows = (int) request.getSession().getAttribute("maxRows");
			if (maxRows != 10 && maxRows != 25 && maxRows != 50 && maxRows != -1) {
				throw new FonctionnalException("Valeur non autorisée");
			}
		} catch (NullPointerException | ClassCastException | FonctionnalException ex) {
			maxRows = 10;
		}
		return maxRows;
	}

	/**
	 * Permet de mettre la première lettre de chaque mot en majuscule
	 * 
	 * @param rawStr
	 * @return String
	 */
	public static String capitalizeFirstLetters(String rawStr) {
		String words[] = rawStr.replaceAll("\\s+", " ").trim().split(" ");
		String newSentence = "";
		for (String word : words) {
			for (int i = 0; i < word.length(); i++)
				newSentence = newSentence + ((i == 0) ? word.substring(i, i + 1).toUpperCase()
						: (i != word.length() - 1) ? word.substring(i, i + 1).toLowerCase()
								: word.substring(i, i + 1).toLowerCase().toLowerCase() + " ");
		}
		return newSentence.trim();
	}

	/**
	 * Permet de hasher un string en MD5
	 * 
	 * @param rawStr
	 * @return String
	 * @throws FonctionnalException
	 */
	public static String hash(String rawStr) throws FonctionnalException {
		String hash;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(rawStr.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			hash = number.toString(16);
		} catch (NoSuchAlgorithmException nsaex) {
			String error = "Erreur de cryptage";
			logger.error(error);
			throw new FonctionnalException(error);
		}
		return hash;
	}

	/**
	 * Récupère la réponse HTML en fonction d'une requête GET à partir de son URL
	 * 
	 * @param urlToRead
	 * @return String
	 * @throws IOException
	 */
	public static String getHTML(String urlToRead) throws IOException {
		StringBuilder result = new StringBuilder();

		// Récupère l'url et ouvre la connexion
		URL url = new URL(urlToRead);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// Ajoute le header request
		con.setRequestMethod("GET");

		// Récupère le contenu de la réponse (JSON stocké en string)
		BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

		return result.toString();
	}

	/**
	 * Récupère la réponse HTML en fonction d'une requête POST à partir de son URL et des paramètres du POST transmis via une HashMap<String, String>
	 * 
	 * @param hmap
	 *            request-url => url POST / user-agent => headers / accept-language => headers / content => body content
	 * @return string
	 * @throws IOException
	 */
	protected static String postHTML(HashMap<String, String> hmap) throws IOException {
		StringBuilder result = new StringBuilder();

		// Récupère l'url et ouvre la connexion
		URL url = new URL(hmap.get("request-url"));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// Ajoute les headers request
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", hmap.get("user-agent"));
		con.setRequestProperty("Accept-Language", hmap.get("accept-language"));

		// Envoi le contenu en POST
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(hmap.get("content"));
		dos.flush();
		dos.close();

		// Récupère les paramètres de la réponse
		int responseCode = con.getResponseCode();
		logger.debug("Requête 'POST' vers l'URL : " + url);
		logger.debug("Paramètres 'POST' : " + hmap.get("content"));
		logger.debug("Response Code : " + String.valueOf(responseCode));

		// Récupère le contenu de la réponse (JSON stocké en string)
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			result.append(line);
		}
		br.close();

		return result.toString();
	}

	/**
	 * Parse un string de Json en ObjectIPAPI
	 * 
	 * @param jsonToParse
	 * @return ObjectIPAPI
	 * @throws JsonSyntaxException
	 */
	public static ObjectIPAPI getObjectIPAPI(String jsonToParse) throws JsonSyntaxException {
		Gson gson = new Gson();
		ObjectIPAPI ipApiObject = gson.fromJson(jsonToParse, ObjectIPAPI.class);
		return ipApiObject;
	}

	/**
	 * Parse un string de Json en ObjectReCaptcha
	 * 
	 * @param jsonToParse
	 * @return ObjectReCaptcha
	 * @throws JsonSyntaxException
	 */
	protected static ObjectReCaptcha getObjectReCaptcha(String jsonToParse) throws JsonSyntaxException {
		Gson gson = new Gson();
		ObjectReCaptcha reCaptchaObject = gson.fromJson(jsonToParse, ObjectReCaptcha.class);
		return reCaptchaObject;
	}

	/**
	 * Vérifie si le reCaptcha récupéré du formulaire a été correctement validé à partir d'une requête POST et de la lecture de sa réponse auprès de
	 * Google
	 * 
	 * @param reCaptcha
	 * @return boolean
	 */
	public static boolean checkReCaptcha(String reCaptcha) {
		boolean valid = false;

		// URL de vérification : voir 'settings.properties'
		// Clef privé : voir 'settings.properties'
		// Paramètre reCaptcha : 'g-recaptcha-response'

		// HashMap regroupant les headers et le body du POST de vérification reCaptcha
		HashMap<String, String> hmap = new HashMap<String, String>();

		// Ajoute les élements à la HashMap
		hmap.put("request-url", Settings.getStringProperty("recaptcha.url"));
		hmap.put("user-agent", "Mozilla/5.0");
		hmap.put("accept-language", "en-US,en;q=0.5");
		// Construis le contenu du POST
		String postParams = "secret=" + Settings.getStringProperty("recaptcha.private") + "&response=" + reCaptcha;
		hmap.put("content", postParams);

		try {
			// Récupère le code HTML de la réponse de reCaptcha
			String reponseAPI = GlobalTools.postHTML(hmap);
			System.out.println("DEBUG: " + reponseAPI);

			// Parse la réponse JSON en ObjectReCaptcha
			ObjectReCaptcha objectReCaptcha = getObjectReCaptcha(reponseAPI);
			if (objectReCaptcha != null) {
				if (objectReCaptcha.isSuccess()) {
					valid = true;
				}
				logger.debug("ReCaptcha : " + objectReCaptcha.toString());
			}
		} catch (IOException | JsonSyntaxException e) {
			logger.error("Impossible de vérifier le reCaptcha");
		}

		if (valid) {
			logger.info("Vérification reCaptcha V2 effectuée avec succès");
		} else {
			logger.error("Vérification reCaptcha V2 échouée !");
		}

		return valid;
	}

	/**
	 * Envoi un mail en utilisant Apache Commons Email
	 * 
	 * @param target
	 * @param subject
	 * @param message
	 */
	public static void sendEmail(String target, String subject, String message) {
		try {
			Email email = new SimpleEmail();
			email.setHostName(SMTP_HOSTNAME);
			email.setSmtpPort(SMTP_PORT);
			email.setSSLOnConnect(SMTP_SSL);
			email.setAuthenticator(new DefaultAuthenticator(SMTP_USER, SMTP_PASS));
			email.setSSLOnConnect(true);
			email.setFrom(SMTP_USER);
			email.setSubject(subject);
			email.setMsg(message);
			email.addTo(target);
			email.send();
			logger.info("Email correctement envoyé à " + target);
		} catch (EmailException ee) {
			ee.printStackTrace();
			logger.error("Erreur lors de l'envoi de l'email à " + target);
		}
	}

}
