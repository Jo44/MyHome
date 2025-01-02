package fr.my.home.tool;

import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.properties.EmailMessages;
import fr.my.home.tool.properties.Settings;

/**
 * Classe DatabaseAccess permettant d'établir une session Hibernate, de gérer les transactions et de tester la connexion
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class DatabaseAccess {
	private static final Logger logger = LogManager.getLogger(DatabaseAccess.class);

	// Attributes

	private static DatabaseAccess instance; // Singleton
	private static SessionFactory sessionFactory;
	private int nbTentative = 0;
	private static final String ADMIN_LANG = Settings.getStringProperty("admin.lang");
	private static final String EMAIL_TARGET = Settings.getStringProperty("admin.email");

	// Constructors

	/**
	 * Default Constructor
	 */
	private DatabaseAccess() {
		// Refresh la SessionFactory
		refreshSessionFactory();
	}

	// Methods

	/**
	 * Râfraichir la Session Factory
	 */
	private void refreshSessionFactory() {
		// La SessionFactory est configurée une fois au lancement de l'application, puis si impossible d'ouvrir une transaction
		// (pour cause de time out de la base)
		logger.debug("Initialisation de la Session Factory ..");
		sessionFactory = new Configuration().configure() // configure à partir des paramètres hibernate.cfg.xml
				.buildSessionFactory();
	}

	/**
	 * Retourne une session Hibernate prête à l'emploi (transaction débutée)
	 * 
	 * @return Session
	 * @throws TechnicalException
	 */
	public Session openSession() throws TechnicalException {
		Session session = sessionFactory.openSession();
		logger.debug("Session Hibernate débutée ..");
		try {
			session.beginTransaction();
		} catch (JDBCConnectionException jdbcex) {
			nbTentative++;
			logger.error("Erreur #" + nbTentative + " lors de l'ouverture d'une nouvelle transaction.");
			session = openSessionRetry(session);
		}
		// Renvoi exception si impossible de débuter une transaction
		if (session == null || session.getTransaction() == null || session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
			throw new TechnicalException("Impossible de débuter la transaction !");
		}
		logger.debug("Transaction débutée ..");
		// Reset du nombre de tentative
		nbTentative = 0;
		return session;
	}

	/**
	 * Râfraichis la SessionFactory et essaye de nouveau d'ouvrir une nouvelle session
	 * 
	 * @param session
	 * @return Session
	 * @throws TechnicalException
	 */
	private Session openSessionRetry(Session session) throws TechnicalException {
		if (nbTentative < 3) {
			logger.info("Nouvelle tentative en cours ..");
			// Fermeture de la session
			session.close();
			// Ré-initialisation de la factory
			refreshSessionFactory();
			// Ré-ouverture de la session
			session = openSession();
		} else {
			// Envoi de l'email de notification de l'erreur en fonction de la langue de l'administrateur
			GlobalTools.sendEmail(EMAIL_TARGET, EmailMessages.getProperty("error.database.object", ADMIN_LANG),
					EmailMessages.getProperty("error.database.content", ADMIN_LANG));
			throw new TechnicalException("Impossible de débuter la transaction apres " + nbTentative + " tentatives.. Abandon.");
		}
		return session;
	}

	/**
	 * Valide la transaction (ou capte PersistenceException (Rollback automatique)) et clôture la session
	 * 
	 * @param session
	 * @throws PersistenceException
	 */
	public void validateSession(Session session) throws PersistenceException {
		if (session != null && session.getTransaction() != null) {
			try {
				session.flush();
				session.getTransaction().commit();
				logger.debug("Transaction commit.");
			} catch (PersistenceException pe) {
				logger.debug("Transaction roll-back.");
				throw pe;
			} finally {
				session.close();
				logger.debug("Session Hibernate clôturée.");
			}
		}
	}

	/**
	 * Test la connection Hibernate, et renvoi son status boolean
	 * 
	 * @return boolean
	 */
	public boolean testConnection() {
		logger.info("Test de connexion Hibernate :");
		boolean ready = false;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			if (session != null && session.isConnected()) {
				session.beginTransaction();
				ready = true;
				logger.info("Test de connexion Hibernate effectué avec succès.");
			}
		} catch (HibernateException he) {
			logger.error("Test de connexion Hibernate échoué !");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return ready;
	}

	/**
	 * Récupère l'unique instance de DatabaseAccess (Singleton)
	 * 
	 * @return DatabaseAccess
	 */
	public static synchronized DatabaseAccess getInstance() {
		if (instance == null) {
			instance = new DatabaseAccess();
		}
		return instance;
	}

}
