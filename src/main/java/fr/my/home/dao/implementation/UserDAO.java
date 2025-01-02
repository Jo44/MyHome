package fr.my.home.dao.implementation;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.User;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe UserDAO qui gère le stockage des utilisateurs
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class UserDAO implements HibernateDAO<User> {

	// Attributes

	private static final String USERDAO_GET_BY_LOGINS = Settings.getStringProperty("user.get.by.logins");
	private static final String USERDAO_GET_BY_USERNAME = Settings.getStringProperty("user.get.by.username");
	private static final String USERDAO_GET_BY_EMAIL = Settings.getStringProperty("user.get.by.email");
	private static final String USERDAO_GET_BY_REMEMBER_ME_TOKEN = Settings.getStringProperty("user.get.by.remember.me.token");
	private static final String USERDAO_GET_BY_VALIDATION_TOKEN = Settings.getStringProperty("user.get.by.validation.token");
	private static final String USERDAO_GET_BY_REINIT_TOKEN = Settings.getStringProperty("user.get.by.reinit.token");
	private static final String USERDAO_GET_NAME_BY_ID = Settings.getStringProperty("user.get.name.by.id");

	// Constructor

	/**
	 * Default Constructor
	 */
	public UserDAO() {
		super();
	}

	// Methods

	/**
	 * Récupère l'utilisateur associé au couple username/password, ou exception fonctionnelle si il n'existe pas
	 * 
	 * @param username
	 * @param password
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUser(String username, String password) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_LOGINS);
		query.setParameter("user_name", username);
		query.setParameter("user_pass", password);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("L'utilisateur n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Récupère l'utilisateur associé au username, ou exception fonctionnelle si il n'existe pas (pour vérification de disponibilité lors de
	 * l'inscription d'un utilisateur)
	 * 
	 * @param username
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByUsername(String username) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_USERNAME);
		query.setParameter("user_name", username);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le login n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Récupère l'utilisateur associé à l'email, ou exception fonctionnelle si il n'existe pas (pour vérification de disponibilité lors de
	 * l'inscription d'un utilisateur ou récupération des identifiants de connexion)
	 * 
	 * @param email
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByEmail(String email) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_EMAIL);
		query.setParameter("user_email", email);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("L'email n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Récupère l'utilisateur associé au token de 'Remember Me', ou exception fonctionnelle si il n'existe pas (pour récupération de la session)
	 * 
	 * @param rememberMeToken
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByRememberMeToken(String rememberMeToken) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_REMEMBER_ME_TOKEN);
		query.setParameter("user_remember_me_token", rememberMeToken);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le token 'Remember Me' n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Récupère l'utilisateur associé au token de validation, ou exception fonctionnelle si il n'existe pas (pour vérification par email lors de
	 * l'inscription d'un utilisateur)
	 * 
	 * @param validationToken
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByValidationToken(String validationToken) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_VALIDATION_TOKEN);
		query.setParameter("user_validation_token", validationToken);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le token de validation n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Récupère l'utilisateur associé au token de ré-initialisation, ou exception fonctionnelle si il n'existe pas (pour vérification par email lors
	 * de la ré-initialisation du mot de passe d'un utilisateur)
	 * 
	 * @param reInitToken
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByReInitToken(String reInitToken) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_REINIT_TOKEN);
		query.setParameter("user_reinit_token", reInitToken);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le token de ré-initialisation n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Récupère le nom de l'utilisateur associé à l'ID
	 * 
	 * @param userId
	 * @return String
	 * @throws TechnicalException
	 */
	public String getNameById(int userId) throws TechnicalException {
		String userName = "???";
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_NAME_BY_ID);
		query.setParameter("user_id", userId);
		try {
			User user = query.getSingleResult();
			userName = user.getName();
		} catch (Exception ex) {
			userName = "???";
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return userName;
	}

	/**
	 * Ajoute un nouvel utilisateur et renvoi son ID, ou exception fonctionnelle si impossible
	 * 
	 * @param user
	 * @return int
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public int add(User user) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(user);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter l'utilisateur");
		}
		return id;
	}

	/**
	 * Met à jour un utilisateur, ou exception fonctionnelle impossible
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(User user) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(user);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour l'utilisateur");
		}
	}

	/**
	 * Supprime un utilisateur
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(User user) throws FonctionnalException, TechnicalException {
		// Utilisation interdite, uniquement pour test
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(user);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer l'utilisateur");
		}
	}

}
