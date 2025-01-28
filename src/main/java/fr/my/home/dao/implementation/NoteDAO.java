package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Note;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Settings;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

/**
 * Classe NoteDAO
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class NoteDAO implements HibernateDAO<Note> {

	/**
	 * Attributs
	 */

	private static final String NOTEDAO_GET_ALL = Settings.getStringProperty("note.get.all");
	private static final String NOTEDAO_GET_ONE = Settings.getStringProperty("note.get.one");

	/**
	 * Constructeur
	 */
	public NoteDAO() {}

	/**
	 * Récupère la liste de toutes les notes de l'utilisateur
	 * 
	 * @param userId
	 * @return List<Note>
	 * @throws TechnicalException
	 */
	public List<Note> getAllNotes(int userId) throws TechnicalException {
		List<Note> listNote = new ArrayList<Note>();
		Session session = HibernateUtil.getInstance().openSession();
		Query<Note> query = session.createQuery(NOTEDAO_GET_ALL, Note.class);
		query.setParameter("id_user", userId);
		listNote = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
		return listNote;
	}

	/**
	 * Récupère la note selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si elle n'existe pas
	 * 
	 * @param noteId
	 * @param userId
	 * @return Note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Note getOneNote(int noteId, int userId) throws FonctionnalException, TechnicalException {
		Note note = null;
		Session session = HibernateUtil.getInstance().openSession();
		Query<Note> query = session.createQuery(NOTEDAO_GET_ONE, Note.class);
		query.setParameter("id", noteId);
		query.setParameter("id_user", userId);
		query.setMaxResults(1);
		try {
			note = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("La note n'existe pas");
		} finally {
			HibernateUtil.getInstance().validateSession(session);
		}
		return note;
	}

	/**
	 * Ajoute une nouvelle note, ou exception fonctionnelle si impossible
	 * 
	 * @param note
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(Note note) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.persist(note);
			session.flush();
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter la note");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Met à jour une note, ou exception fonctionnelle si impossible
	 * 
	 * @param note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(Note note) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.merge(note);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour la note");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Supprime une note, ou exception fonctionnelle si impossible
	 * 
	 * @param note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(Note note) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.remove(note);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer la note");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

}
