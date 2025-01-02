package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.CustomFile;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe FileDAO qui gère le stockage des fichiers
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class FileDAO implements HibernateDAO<CustomFile> {

	// Attributes

	private static final String FILEDAO_GET_ALL = Settings.getStringProperty("files.get.all");
	private static final String FILEDAO_GET_ONE = Settings.getStringProperty("files.get.one");
	private static final String FILEDAO_GET_ONE_BY_NAME = Settings.getStringProperty("files.get.one.by.name");

	// Constructor

	/**
	 * Default Constructor
	 */
	public FileDAO() {
		super();
	}

	// Methods

	/**
	 * Récupère la liste de tous les fichiers de l'utilisateur
	 * 
	 * @param userId
	 * @return List<CustomFile>
	 * @throws TechnicalException
	 */
	public List<CustomFile> getAllFiles(int userId) throws TechnicalException {
		List<CustomFile> listFile = new ArrayList<CustomFile>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ALL);
		query.setParameter("file_id_user", userId);
		listFile = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
		return listFile;
	}

	/**
	 * Récupère le fichier selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si il n'existe pas
	 * 
	 * @param fileId
	 * @param userId
	 * @return CustomFile
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public CustomFile getOneFile(int fileId, int userId) throws FonctionnalException, TechnicalException {
		CustomFile file = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ONE);
		query.setParameter("file_id", fileId);
		query.setParameter("file_id_user", userId);
		try {
			file = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le fichier n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return file;
	}

	/**
	 * Vérifie si un fichier avec ce nom existe déjà pour l'utilisateur
	 * 
	 * @param filename
	 * @param userId
	 * @return boolean
	 * @throws TechnicalException
	 */
	public boolean checkOneFileByName(String filename, int userId) throws TechnicalException {
		boolean exist;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ONE_BY_NAME);
		query.setParameter("file_name", filename);
		query.setParameter("file_id_user", userId);
		try {
			// Inutile de stocker le fichier récupéré
			query.getSingleResult();
			exist = true;
		} catch (NoResultException nre) {
			exist = false;
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return exist;
	}

	/**
	 * Ajoute un nouveau fichier en base et renvoi son ID, ou exception fonctionnelle si impossible
	 * 
	 * @param file
	 * @return int
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public int add(CustomFile file) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(file);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le fichier");
		}
		return id;
	}

	/**
	 * Met à jour un fichier, ou exception fonctionnelle si impossible
	 * 
	 * @param file
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(CustomFile file) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(file);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le fichier");
		}
	}

	/**
	 * Supprime un fichier, ou exception fonctionnelle si impossible
	 * 
	 * @param file
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(CustomFile file) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(file);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le fichier");
		}
	}

}
