package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.CustomFile;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Settings;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

/**
 * Classe FileDAO
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class FileDAO implements HibernateDAO<CustomFile> {

	/**
	 * Attributs
	 */

	private static final String FILEDAO_GET_ALL = Settings.getStringProperty("file.get.all");
	private static final String FILEDAO_GET_ONE = Settings.getStringProperty("file.get.one");
	private static final String FILEDAO_GET_ONE_BY_NAME = Settings.getStringProperty("file.get.one.by.name");

	/**
	 * Constructeur
	 */
	public FileDAO() {}

	/**
	 * Récupère la liste de tous les fichiers de l'utilisateur
	 * 
	 * @param userId
	 * @return List<CustomFile>
	 * @throws TechnicalException
	 */
	public List<CustomFile> getAllFiles(int userId) throws TechnicalException {
		List<CustomFile> listFile = new ArrayList<CustomFile>();
		Session session = HibernateUtil.getInstance().openSession();
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ALL, CustomFile.class);
		query.setParameter("id_user", userId);
		listFile = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
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
		Session session = HibernateUtil.getInstance().openSession();
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ONE, CustomFile.class);
		query.setParameter("id", fileId);
		query.setParameter("id_user", userId);
		query.setMaxResults(1);
		try {
			file = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le fichier n'existe pas");
		} finally {
			HibernateUtil.getInstance().validateSession(session);
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
		Session session = HibernateUtil.getInstance().openSession();
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ONE_BY_NAME, CustomFile.class);
		query.setParameter("id_user", userId);
		query.setParameter("name", filename);
		query.setMaxResults(1);
		try {
			// Inutile de stocker le fichier récupéré
			query.getSingleResult();
			exist = true;
		} catch (NoResultException nre) {
			exist = false;
		} finally {
			HibernateUtil.getInstance().validateSession(session);
		}
		return exist;
	}

	/**
	 * Ajoute un nouveau fichier, ou exception fonctionnelle si impossible
	 * 
	 * @param file
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(CustomFile file) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.persist(file);
			session.flush();
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le fichier");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
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
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.merge(file);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le fichier");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
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
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.remove(file);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le fichier");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

}
