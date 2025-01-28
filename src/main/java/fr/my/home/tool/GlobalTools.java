package fr.my.home.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import jakarta.servlet.http.HttpServletRequest;

/**
 * Classe GlobalTools regroupant différents outils
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class GlobalTools {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(GlobalTools.class);
	private static final String HASH_SALT = Settings.getStringProperty("global.hash.salt");
	private static final String SMTP_HOSTNAME = Settings.getStringProperty("smtp.hostname");
	private static final int SMTP_PORT = Settings.getIntProperty("smtp.port");
	private static final boolean SMTP_SSL = Settings.getBooleanProperty("smtp.ssl");
	private static final String SMTP_USER = Settings.getStringProperty("smtp.user");
	private static final String SMTP_PASS = Settings.getStringProperty("smtp.pass");

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
	 * Format Weight to String (octets/ko/Mo/Go)
	 * 
	 * @param weight
	 * @return String
	 */
	public static String formatWeightToString(long weight) {
		String formattedWeight;
		DecimalFormat formatter = new DecimalFormat("#.00");
		if (weight <= 0) {
			formattedWeight = "0 octet";
		} else if (weight == 1) {
			formattedWeight = "1 octet";
		} else if (weight < 1024) {
			formattedWeight = String.valueOf(weight) + " octets";
		} else if (weight < (1024 * 1024)) {
			formattedWeight = String.valueOf(formatter.format((double) weight / 1024)) + " ko";
		} else if (weight < (1024 * 1024 * 1024)) {
			formattedWeight = String.valueOf(formatter.format((double) weight / (1024 * 1024))) + " Mo";
		} else {
			formattedWeight = String.valueOf(formatter.format((double) weight / (1024 * 1024 * 1024))) + " Go";
		}
		return formattedWeight;
	}

	/**
	 * Vérifie que le timestamp est valide et supérieur à l'année 2000
	 * 
	 * @param value
	 * @param defaultDuration
	 * @return Timestamp
	 */
	public static Timestamp getFrom(String value, long defaultDuration) {
		LocalDateTime now = LocalDateTime.now();
		Timestamp from = Timestamp.valueOf(now);
		try {
			from.setTime(Long.parseLong(value));
			// Vérifie que la date est > 2000
			Calendar cal = Calendar.getInstance();
			cal.setTime(from);
			int year = cal.get(Calendar.YEAR);
			if (year < 2000) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nfe) {
			from = Timestamp.valueOf(now.minusMonths(defaultDuration));
		}
		return from;
	}

	/**
	 * Vérifie que le timestamp est valide et inférieur à l'année 2200
	 * 
	 * @param value
	 * @return Timestamp
	 */
	public static Timestamp getTo(String value) {
		LocalDateTime now = LocalDateTime.now();
		Timestamp to = Timestamp.valueOf(now);
		try {
			to.setTime(Long.parseLong(value));
			// Vérifie que la date est < 2200
			Calendar cal = Calendar.getInstance();
			cal.setTime(to);
			int year = cal.get(Calendar.YEAR);
			if (year > 2200) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nfe) {
			to = Timestamp.valueOf(now);
		}
		return to;
	}

	/**
	 * Encode une chaine de caractères en latin
	 * 
	 * @param value
	 * @return String
	 */
	public static String encodeLatin(String value) {
		String result = "";
		final Pattern patern = Pattern.compile("[^\\p{IsLatin}\\p{Digit}\\p{Space}\\p{Punct}]+");
		if (value != null && !value.trim().isEmpty()) {
			// Normalisation Unicode pour gérer les caractères combinés
			String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
			// Suppression des caractères non latins, chiffres, espaces et ponctuation
			Matcher matcher = patern.matcher(normalized);
			result = matcher.replaceAll("").trim();
		}
		return result;
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
			formattedYTTitle = title.trim().replace('\"', '\'');
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
	 * Permet de hasher un string en SHA-256
	 * 
	 * @param value
	 * @return String
	 * @throws FonctionnalException
	 */
	public static String hash256(String value) throws FonctionnalException {
		String hash = null;
		try {
			value = HASH_SALT + value;
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(value.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			hash = hexString.toString();
		} catch (Exception ex) {
			String error = "Erreur de cryptage SHA-256";
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
	 * @throws URISyntaxException
	 */
	public static String getHTML(String urlToRead) throws IOException, URISyntaxException {
		StringBuilder result = new StringBuilder();

		// Récupère l'url et ouvre la connexion
		URL url = new URI(urlToRead).toURL();
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
	 * @throws URISyntaxException
	 */
	protected static String postHTML(HashMap<String, String> hmap) throws IOException, URISyntaxException {
		StringBuilder result = new StringBuilder();

		// Récupère l'url et ouvre la connexion
		URL url = new URI(hmap.get("request-url")).toURL();
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

			// Parse la réponse JSON en ObjectReCaptcha
			ObjectReCaptcha objectReCaptcha = getObjectReCaptcha(reponseAPI);
			if (objectReCaptcha != null) {
				if (objectReCaptcha.isSuccess()) {
					valid = true;
				}
				logger.debug("ReCaptcha : " + objectReCaptcha.toString());
			}
		} catch (IOException | JsonSyntaxException | URISyntaxException ex) {
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

	/**
	 * Génère une chaine de caractères aléatoires
	 * 
	 * @param length
	 * @return String
	 */
	public static String generateRandomString(int length) {
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			sb.append(characters.charAt(index));
		}
		return sb.toString();
	}

}
