package fr.my.home.manager;

import fr.my.home.bean.User;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.global.ReCaptchaException;
import fr.my.home.exception.help.MessageException;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.EmailMessages;
import fr.my.home.tool.properties.Settings;

/**
 * Manager qui prends en charge la gestion de l'envoi du message d'aide à l'administrateur
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class HelpManager {

	/**
	 * Constructeur
	 */
	public HelpManager() {
		super();
	}

	/**
	 * Vérifie le message, le reCaptcha et l'utilisateur puis envoi le message à l'administrateur en fonction de la localisation
	 * 
	 * @param user
	 * @param message
	 * @param reCaptcha
	 * @throws FonctionnalException
	 */
	public void sendHelpMessage(User user, String message, String reCaptcha) throws FonctionnalException {
		// Vérifie si le message est valide
		boolean validMessage = validContactMessage(message);

		// Si le message est valide
		if (validMessage) {
			// Vérification du reCaptcha
			boolean validReCaptcha = GlobalTools.checkReCaptcha(reCaptcha);

			// Si le reCaptcha est valide
			if (validReCaptcha) {
				// Prépare l'objet et le contenu de l'email (avec user, message et lang)
				// Récupère la langue de l'administrateur pour localisation du mail
				String lang = GlobalTools.validLanguage(Settings.getStringProperty("admin.lang"));
				String mailObject = generateHelpEmailObject(lang);
				String content = generateHelpEmailContent(user, message.trim(), lang);

				// Envoi de l'email de contact
				GlobalTools.sendEmail(Settings.getStringProperty("admin.email"), mailObject, content);
			} else {
				// Le reCaptcha n'est pas valide
				throw new ReCaptchaException("Le reCaptcha n'est pas valide");
			}
		} else {
			// Le message n'est pas valide
			throw new MessageException("Le message n'est pas valide");
		}
	}

	/**
	 * Vérifie que le message est valide pour l'envoi
	 * 
	 * @param message
	 * @return boolean
	 */
	private boolean validContactMessage(String message) {
		boolean valid = false;
		if (message != null && !message.trim().isEmpty() && message.trim().length() < 5000) {
			valid = true;
		}
		return valid;
	}

	/**
	 * Génère l'objet de l'email de demande d'aide en fonction de la localisation
	 * 
	 * @param lang
	 * @return String
	 */
	private String generateHelpEmailObject(String lang) {
		return EmailMessages.getProperty("help.object", lang);
	}

	/**
	 * Génère le contenu de l'email de demande d'aide à partir de l'utilisateur et de son message en fonction de la localisation
	 * 
	 * @param user
	 * @param message
	 * @param lang
	 * @return String
	 */
	protected String generateHelpEmailContent(User user, String message, String lang) {
		StringBuilder sb = new StringBuilder();
		sb.append(EmailMessages.getProperty("help.msg1", lang));
		sb.append(user.getId());
		sb.append(EmailMessages.getProperty("help.msg2", lang));
		sb.append(" ");
		sb.append(user.getName());
		sb.append(" ");
		sb.append(EmailMessages.getProperty("help.msg3", lang));
		sb.append(" ");
		sb.append(user.getEmail());
		sb.append(" ");
		sb.append(EmailMessages.getProperty("help.msg4", lang));
		sb.append(message.trim());
		sb.append(EmailMessages.getProperty("help.msg5", lang));
		return sb.toString();
	}

}
