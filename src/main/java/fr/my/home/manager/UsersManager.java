package fr.my.home.manager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;
import fr.my.home.dao.implementation.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.global.ReCaptchaException;
import fr.my.home.exception.users.EmailException;
import fr.my.home.exception.users.IDsException;
import fr.my.home.exception.users.InvalidUserException;
import fr.my.home.exception.users.LoginSizeException;
import fr.my.home.exception.users.NewPassException;
import fr.my.home.exception.users.NoMatchPassException;
import fr.my.home.exception.users.OldPassException;
import fr.my.home.exception.users.PassSizeException;
import fr.my.home.exception.users.UnusedEmailException;
import fr.my.home.exception.users.UsedEmailException;
import fr.my.home.exception.users.UsedLoginException;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.EmailMessages;
import fr.my.home.tool.properties.Settings;

/**
 * Manager qui prends en charge la gestion des utilisateurs
 * 
 * @author Jonathan
 * @version 1.1
 * @since 07/08/2021
 */
public class UsersManager {
	private static final Logger logger = LogManager.getLogger(UsersManager.class);

	// Attributes

	private static final String COOKIE_NAME = Settings.getStringProperty("cookie.name");
	private UserDAO userDAO;

	// Constructors

	/**
	 * Default Constructor
	 */
	public UsersManager() {
		super();
		userDAO = new UserDAO();
	}

	// Methods

	// Connexion

	/**
	 * Récupère l'utilisateur via Username et Password ou renvoi exception fonctionnelle / technique (et gestion de la fonction 'Remember Me')
	 * 
	 * @param request
	 * @prama response
	 * @param username
	 * @param password
	 * @param rememberMe
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User checkSignIn(HttpServletRequest request, HttpServletResponse response, String username, String password, String rememberMe)
			throws FonctionnalException, TechnicalException {
		User user = null;
		String hashpass = null;
		try {
			// Cryptage du mot de passe pour comparaison en base
			hashpass = GlobalTools.hash(password.trim());

			// Vérification du login et mdp en base, renvoi user valide ou exceptions
			user = userDAO.getUser(username.trim(), hashpass);

			// Mise à jour last log date
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			user.setLastLogDate(now);
			userDAO.update(user);

			// Vérification 'Remember Me'
			if (rememberMe != null && !rememberMe.isEmpty()) {
				// Test la présence d'un cookie
				User rememberUser = cookieRestore(request, response);
				// Si null ou utilisateur différent
				if (rememberUser == null || !rememberUser.getName().equals(username)) {

					// Supprime l'ancien cookie si besoin
					if (rememberUser != null) {
						deleteCookie(request, response);
					}

					// Création du cookie
					String cookie = createCookie(response);

					// Met à jour le Token 'Remember Me' de l'utilisateur récupéré
					user.setRememberMeToken(cookie);

					// Mise à jour de l'utilisateur dans la base
					userDAO.update(user);

					logger.debug("Création du cookie réussie");
				}
			} else {
				// Supprime le cookie si 'Remember Me' non coché
				deleteCookie(request, response);
				user.setRememberMeToken(null);
				userDAO.update(user);
			}
		} catch (FonctionnalException fex) {
			throw new IDsException("Identifiants incorrects");
		} catch (TechnicalException tex) {
			throw tex;
		}
		return user;
	}

	/**
	 * Test la présence d'un cookie et retourne l'utilisateur reconnu
	 * 
	 * @param request
	 * @param response
	 * @return User
	 */
	public User cookieRestore(HttpServletRequest request, HttpServletResponse response) {
		User user = null;
		// Test la présence d'un cookie de connection
		String cookie = cookieCheck(request);

		// Si cookie présent ..
		if (cookie != null && !cookie.isEmpty()) {
			logger.debug("Tentative de récupération par cookie ..");
			try {
				// .. récupère l'utilisateur
				user = userDAO.getUserByRememberMeToken(cookie);

				logger.debug("Récupération réussie  -> " + user.getName());
			} catch (FonctionnalException fex) {
				// Supprime le cookie si aucun utilisateur ne correspond
				deleteCookie(request, response);

				logger.debug(fex.getMessage());
			} catch (TechnicalException tex) {
				logger.error("Erreur de Base de Données");
			}
		}
		return user;
	}

	/**
	 * Vérifie la présence d'un cookie enregistré pour le site et renvoi sa valeur si oui, ou null sinon
	 * 
	 * @param request
	 * @return String
	 */
	private String cookieCheck(HttpServletRequest request) {
		String cookieValue = null;
		// Récupère la liste des cookies
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			// Parmis ces cookies ..
			for (Cookie cookie : cookies) {
				// .. récupère le cookie correspondant au nom de cookie défini
				if (cookie.getName().equals(COOKIE_NAME)) {
					cookieValue = cookie.getValue();
				}
			}
		}
		return cookieValue;
	}

	/**
	 * Génération d'un cookie et renvoi le token 'Remember Me' pour association en base avec l'utilisateur
	 * 
	 * @param response
	 * @return String
	 */
	private String createCookie(HttpServletResponse response) {
		// Génération d'un UUID
		UUID uuid = UUID.randomUUID();
		String rememberMe = uuid.toString();
		// Création d'un cookie
		Cookie cookie = new Cookie(COOKIE_NAME, rememberMe);
		// Valide pour 1 an
		cookie.setMaxAge(365 * 24 * 60 * 60);
		// Ajout du cookie
		response.addCookie(cookie);
		// Retourne le Token 'Remember Me' pour enregistrement en base
		return rememberMe;
	}

	/**
	 * Récupère l'utilisateur pour connexion automatique via cookie
	 * 
	 * @param request
	 * @param response
	 * @return User
	 */
	public User getUserByCookie(HttpServletRequest request, HttpServletResponse response) {
		User user = null;
		// Tente de récupérer l'utilisateur via son cookie
		user = cookieRestore(request, response);
		// Si l'utilisateur est correctement récupéré
		if (user != null) {
			try {
				// Mise à jour last log date
				Timestamp now = Timestamp.valueOf(LocalDateTime.now());
				user.setLastLogDate(now);
				userDAO.update(user);
			} catch (FonctionnalException | TechnicalException ex) {
				logger.error("Impossible de mettre à jour l'utilisateur pour connexion par cookie");
			}
		}
		return user;
	}

	// Déconnexion

	/**
	 * Supprime le cookie et met à jour l'utilisateur en base si possible
	 * 
	 * @param request
	 * @param response
	 * @param user
	 */
	public void signOut(HttpServletRequest request, HttpServletResponse response, User user) {
		// Supprime le cookie si il existe
		deleteCookie(request, response);

		// Met à jour le Token 'Remember Me' de l'utilisateur récupéré
		user.setRememberMeToken(null);

		// Mise à jour de l'utilisateur dans la base
		try {
			userDAO.update(user);
		} catch (FonctionnalException | TechnicalException ex) {
			logger.error("Impossible de mettre à jour l'utilisateur pour suppression du cookie");
		}
	}

	/**
	 * Suppression du cookie
	 * 
	 * @param request
	 * @param response
	 */
	private void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
		// Récupère tous les cookies
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			// Si on trouve le cookie de MyHome
			if (cookie.getName().equals(COOKIE_NAME)) {
				// Fait expirer le cookie
				cookie.setMaxAge(0);
				cookie.setValue(null);
				response.addCookie(cookie);
				logger.debug("Cookie supprimé avec succès");
			}
		}
	}

	// Inscription

	/**
	 * Vérifie si l'inscription est possible, puis enregistre en base et envoi le mail de confirmation à l'utilisateur en fonction de la localisation
	 * 
	 * @param username
	 * @param password
	 * @param confirmPassword
	 * @param email
	 * @param reCaptcha
	 * @param lang
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User checkSignUp(String username, String password, String confirmPassword, String email, String reCaptcha, String lang)
			throws FonctionnalException, TechnicalException {
		User user = null;

		// Vérification des paramètres du formulaire d'inscription (+ reCatpcha) et renvoi le hashPass si tout est ok
		String hashPass = checkSignUpParameters(username, password, confirmPassword, email, reCaptcha);

		// Vérification du reCaptcha
		boolean validReCaptcha = GlobalTools.checkReCaptcha(reCaptcha);

		// Vérification si le login est disponible
		boolean availableLogin = checkAvailableLogin(username);

		// Vérification si l'email est disponible
		boolean availableEmail = checkAvailableEmail(email);

		// Prépare le token de validation pour activation du compte via email
		String validToken = UUID.randomUUID().toString();

		// Si tout est ok, ajoute l'utilisateur
		if (validReCaptcha && availableLogin && availableEmail) {
			// Créé l'utilisateur (par défaut non activé, et token de validation chargé)
			user = new User(username.trim(), hashPass, email.trim(), null, validToken, false, null, null, null,
					Timestamp.valueOf(LocalDateTime.now()));

			// Ajoute l'utilisateur en base
			userDAO.add(user);

			// Prépare l'objet et le contenu de l'email (avec username, validToken et lang)
			lang = GlobalTools.validLanguage(lang);
			String mailObject = generateVerificationEmailObject(lang);
			String content = generateVerificationEmailContent(username.trim(), validToken, lang);

			// Envoi de l'email de confirmation
			GlobalTools.sendEmail(email, mailObject, content);
		} else {
			if (!validReCaptcha) {
				throw new ReCaptchaException("Le reCaptcha n'est pas valide");
			}
			if (!availableLogin) {
				throw new UsedLoginException("Le login est déjà utilisé");
			}
			if (!availableEmail) {
				throw new UsedEmailException("L'email est déjà utilisé");
			}
		}
		return user;
	}

	/**
	 * Vérifie si tous les champs du formulaire d'inscription sont valides (sinon exceptions) et renvoi le password hashé
	 * 
	 * @param username
	 * @param password
	 * @param confirmPassword
	 * @param email
	 * @param reCaptcha
	 * @return String
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	private String checkSignUpParameters(String username, String password, String confirmPassword, String email, String reCaptcha)
			throws FonctionnalException, TechnicalException {
		String hashPass = null;
		// Vérifie si le login est présent
		if (username == null || username.trim().length() < 3 || username.trim().length() > 30) {
			throw new LoginSizeException("Le login doit contenir entre 3 et 30 caractères");
		}
		// Vérifie si les passwords sont présents
		if (password == null || password.trim().length() < 6 || password.trim().length() > 30 || confirmPassword == null
				|| confirmPassword.trim().length() < 6 || confirmPassword.trim().length() > 30) {
			throw new PassSizeException("Les mots de passe doivent contenir entre 6 et 30 caractères");
		}
		// Vérifie si la confirmation du nouveau mot de passe est identique
		if (!password.trim().equals(confirmPassword.trim())) {
			throw new NoMatchPassException("La confirmation du mot de passe n'est pas identique");
		}
		// Vérifie si le mot de passe est valide (correctement hashable)
		try {
			hashPass = GlobalTools.hash(password.trim());
		} catch (FonctionnalException fex) {
			throw new NewPassException("Le mot de passe n'est pas valide");
		}
		// Vérifie si l'email est valide
		if (email == null || email.trim().length() < 6 || email.trim().length() > 50 || email.trim().lastIndexOf("@") == -1
				|| email.trim().lastIndexOf(".") == -1) {
			throw new EmailException("L'email n'est pas valide");
		}
		// Vérifie si le reCpatcha est présent
		if (reCaptcha == null || reCaptcha.trim().length() < 1) {
			throw new ReCaptchaException("Le reCaptcha n'est pas valide");
		}
		return hashPass;
	}

	/**
	 * Vérifie si le username est disponible pour inscription en base
	 * 
	 * @param username
	 * @return boolean
	 */
	private boolean checkAvailableLogin(String username) {
		boolean valid = false;
		try {
			userDAO.getUserByUsername(username);
			valid = false;
			logger.error("Le login n'est pas disponible");
		} catch (FonctionnalException fex) {
			valid = true;
			logger.info("Le login est disponible");
		} catch (TechnicalException tex) {
			valid = false;
			logger.error("Impossible de déterminer si le login est disponible");
		}
		return valid;
	}

	/**
	 * Vérifie si l'email est disponible pour inscription en base
	 * 
	 * @param email
	 * @return boolean
	 */
	private boolean checkAvailableEmail(String email) {
		boolean valid = false;
		try {
			userDAO.getUserByEmail(email);
			valid = false;
			logger.error("L'email n'est pas disponible");
		} catch (FonctionnalException fex) {
			valid = true;
			logger.info("L'email est disponible");
		} catch (TechnicalException tex) {
			valid = false;
			logger.error("Impossible de déterminer si l'email est disponible");
		}
		return valid;
	}

	/**
	 * Génère l'objet de l'email de confirmation d'inscription d'un utilisateur en fonction de la localisation
	 * 
	 * @param lang
	 * @return String
	 */
	private String generateVerificationEmailObject(String lang) {
		return EmailMessages.getProperty("sign.up.object", lang);
	}

	/**
	 * Génère le contenu l'email de confirmation d'inscription d'un utilisateur à partir de son nom et de son token de validation en fonction de la
	 * localisation
	 * 
	 * @param username
	 * @param validToken
	 * @param lang
	 * @return String
	 */
	private String generateVerificationEmailContent(String username, String validToken, String lang) {
		StringBuilder sb = new StringBuilder();
		sb.append(EmailMessages.getProperty("sign.up.msg1", lang));
		sb.append(username);
		sb.append(" ");
		sb.append(EmailMessages.getProperty("sign.up.msg2", lang));
		sb.append(validToken);
		sb.append(EmailMessages.getProperty("sign.up.msg3", lang));
		sb.append(" ");
		sb.append(Settings.getStringProperty("admin.email"));
		sb.append(EmailMessages.getProperty("sign.up.msg4", lang));
		return sb.toString();
	}

	/**
	 * Récupère l'utilisateur à partir du token de validation, vérifie que son token est encore valide, puis met à jour l'utilisateur
	 * 
	 * @param validationToken
	 * @return boolean
	 */
	public boolean validateUser(String validationToken) {
		boolean valid = false;
		try {
			// Récupère l'utilisateur via son token de validation
			User user = userDAO.getUserByValidationToken(validationToken);

			// Si l'utilisateur n'est pas actif
			if (user != null && !user.isActive()) {
				// Vérifie si le token est expiré (+ de 72h)
				// now > limitDate
				// limitDate = validationTokenDate + 72 heures
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime validationTokenDate = user.getInscriptionDate().toLocalDateTime();
				LocalDateTime limitDate = validationTokenDate.plusHours(72);

				if (now.isAfter(limitDate)) {
					throw new FonctionnalException("Le token a expiré");
				}

				// Met à jour le paramètre 'active'
				user.setActive(true);

				// Met à jour l'utilisateur
				userDAO.update(user);
				valid = true;
			} else {
				valid = true;
			}
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		}
		return valid;
	}

	// Ré-initialisation

	/**
	 * Récupère l'utilisateur demandant une ré-initialisation des identifiants (si reCaptcha valide), génére un token de ré-initialisation et un
	 * timestamp de création du token, les enregistre en base puis lui envoi un mail de récupération en fonction de la localisation
	 * 
	 * @param email
	 * @param reCaptcha
	 * @param lang
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void checkRecovery(String email, String reCaptcha, String lang) throws FonctionnalException, TechnicalException {
		// Vérification de l'email
		if (email == null || email.trim().length() < 6 || email.trim().length() > 50 || email.trim().lastIndexOf("@") == -1
				|| email.trim().lastIndexOf(".") == -1) {
			throw new EmailException("L'email n'est pas valide");
		}
		// Vérification du reCaptcha
		if (reCaptcha == null || reCaptcha.trim().length() < 1 || !GlobalTools.checkReCaptcha(reCaptcha)) {
			throw new ReCaptchaException("Le reCaptcha n'est pas valide");
		}

		try {
			// Récupère l'utilisateur
			User user = userDAO.getUserByEmail(email);

			// Génère un token de ré-initialisation du mot de passe
			String reInitToken = UUID.randomUUID().toString();

			// Génère un timestamp pour la création du token de ré-initialisation
			Timestamp reInitDate = Timestamp.valueOf(LocalDateTime.now());

			// Met à jour l'utilisateur avec des données de ré-initialisation
			user.setReInitToken(reInitToken);
			user.setReInitDate(reInitDate);

			// Met à jour l'utilisateur en base
			userDAO.update(user);

			// Prépare l'objet et le contenu de l'email (avec username, reInitToken et lang)
			lang = GlobalTools.validLanguage(lang);
			String mailObject = generateReinitEmailObject(lang);
			String content = generateReinitEmailContent(user.getName(), user.getReInitToken(), lang);

			// Envoi de l'email de confirmation
			GlobalTools.sendEmail(email, mailObject, content);

		} catch (FonctionnalException fex) {
			throw new UnusedEmailException("L'email n'est pas enregistré");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Génère l'objet de l'email de récupération des identifiants d'un utilisateur en fonction de la localisation
	 * 
	 * @param lang
	 * @return String
	 */
	private String generateReinitEmailObject(String lang) {
		return EmailMessages.getProperty("recovery.object", lang);
	}

	/**
	 * Génère le contenu l'email de récupération des identifiants d'un utilisateur à partir de son nom en fonction de la localisation
	 * 
	 * @param username
	 * @param reInitToken
	 * @param lang
	 * @return String
	 */
	private String generateReinitEmailContent(String username, String reInitToken, String lang) {
		StringBuilder sb = new StringBuilder();
		sb.append(EmailMessages.getProperty("recovery.msg1", lang));
		sb.append(username);
		sb.append(EmailMessages.getProperty("recovery.msg2", lang));
		sb.append(reInitToken);
		sb.append(EmailMessages.getProperty("recovery.msg3", lang));
		sb.append(" ");
		sb.append(Settings.getStringProperty("admin.email"));
		sb.append(EmailMessages.getProperty("recovery.msg4", lang));
		return sb.toString();
	}

	/**
	 * Vérifie si les paramètres de la ré-initialisation sont valides et renvoi le nouveau mot de passe hashé, ou exception fonctionnelle si erreur
	 * 
	 * @param newPassword
	 * @param newPasswordConfirm
	 * @param reInitToken
	 * @return String
	 * @throws FonctionnalException
	 */
	public String checkParamsReInit(String newPassword, String newPasswordConfirm, String reInitToken) throws FonctionnalException {
		String hashPass = null;
		// Si mots de passe non valides
		if (newPassword == null || newPassword.trim().length() < 6 || newPassword.trim().length() > 30 || newPasswordConfirm == null
				|| newPasswordConfirm.trim().length() < 6 || newPasswordConfirm.trim().length() > 30) {
			throw new PassSizeException("Les mots de passe doivent contenir entre 6 et 30 caractères");
		}

		if (!newPassword.trim().equals(newPasswordConfirm.trim())) {
			throw new NoMatchPassException("La confirmation du mot de passe n'est pas identique");
		}

		// Vérifie si le mot de passe est valide (correctement hashable)
		try {
			hashPass = GlobalTools.hash(newPassword.trim());
		} catch (FonctionnalException fex) {
			throw new NewPassException("Le mot de passe n'est pas valide");
		}
		return hashPass;
	}

	/**
	 * Récupère l'utilisateur à partir du token de ré-initialisation, vérifie que son token est encore valide, puis met à jour l'utilisateur ou renvoi
	 * exception fonctionnelle si pas possible
	 * 
	 * @param hashPass
	 * @param reInitToken
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void checkReInit(String hashPass, String reInitToken) throws FonctionnalException, TechnicalException {
		try {
			// Récupère l'utilisateur associé au token de ré-initialisation
			User user = userDAO.getUserByReInitToken(reInitToken);

			// Vérifie si le token est expiré (+ de 24h)
			// now > limitDate
			// limitDate = reInitTokenDate + 24 heures
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime reInitTokenDate = user.getReInitDate().toLocalDateTime();
			LocalDateTime limitDate = reInitTokenDate.plusHours(24);

			if (now.isAfter(limitDate)) {
				throw new FonctionnalException("Le token a expiré");
			}

			// Met à jour l'utilisateur en base
			user.setPass(hashPass);
			user.setReInitDate(null);
			user.setReInitToken(null);
			userDAO.update(user);

		} catch (FonctionnalException | TechnicalException ex) {
			throw ex;
		}
	}

	/**
	 * Vérifie si la modification du mot de passe est possible en fonction de l'utilisateur en cours et des champs du formulaire et renvoi newHash
	 * pour modification en base, ou sinon une exception fonctionnelle qui spécifie la cause de l'erreur
	 * 
	 * @param user
	 * @param oldPassword
	 * @param newPassword
	 * @param newPasswordConfirm
	 * @return String
	 * @throws FonctionnalException
	 */
	public String checkUpdatePassword(User user, String oldPassword, String newPassword, String newPasswordConfirm) throws FonctionnalException {
		String newHash = null;
		// Vérifie si l'utilisateur est valide
		if (user != null && user.getPass() != null) {
			// Vérifie si les champs sont bien remplis
			if (oldPassword == null || oldPassword.trim().length() < 6 || oldPassword.trim().length() > 30 || newPassword == null
					|| newPassword.trim().length() < 6 || newPassword.trim().length() > 30 || newPasswordConfirm == null
					|| newPasswordConfirm.trim().length() < 6 || newPasswordConfirm.trim().length() > 30) {
				throw new PassSizeException("Les mots de passe doivent contenir entre 6 et 30 caractères");
			}
			// Vérifie si la confirmation du nouveau mot de passe est identique
			if (!newPassword.trim().equals(newPasswordConfirm.trim())) {
				throw new NoMatchPassException("La confirmation du nouveau mot de passe n'est pas identique");
			}
			// Vérifie si le nouveau mot de passe est valide (correctement hashable)
			try {
				newHash = GlobalTools.hash(newPassword.trim());
			} catch (FonctionnalException fex) {
				throw new NewPassException("Le nouveau mot de passe n'est pas valide");
			}
			// Vérification si ancien mot de passe est valide
			if (user.getPass().equals(GlobalTools.hash(oldPassword.trim()))) {
				logger.debug("Modification du compte possible");
			} else {
				throw new OldPassException("L'ancien mot de passe n'est pas valide");
			}
		} else {
			// L'utilisateur n'est pas valide
			throw new InvalidUserException("L'utilisateur n'est pas valide");
		}
		return newHash;
	}

	/**
	 * Met à jour le mot de passe de l'utilisateur ou renvoi une exception fonctionnelle / technique
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void updatePassword(User user) throws FonctionnalException, TechnicalException {
		try {
			userDAO.update(user);
			logger.debug("Mise à jour du mot de passe de l'utilisateur effectuée avec succès");
		} catch (FonctionnalException fex) {
			throw fex;
		} catch (TechnicalException tex) {
			throw tex;
		}
	}

}
