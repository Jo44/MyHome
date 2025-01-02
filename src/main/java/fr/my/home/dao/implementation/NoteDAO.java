package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Note;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe NoteDAO qui gère le stockage des notes
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class NoteDAO implements HibernateDAO<Note> {

	// Attributes

	private static final String NOTEDAO_GET_ALL = Settings.getStringProperty("note.get.all");
	private static final String NOTEDAO_GET_ONE = Settings.getStringProperty("note.get.one");

	// Constructor

	/**
	 * Default Constructor
	 */
	public NoteDAO() {
		super();
	}

	// Methods

	/**
	 * Récupère la liste de toutes les notes de l'utilisateur
	 * 
	 * @param userId
	 * @return List<Note>
	 * @throws TechnicalException
	 */
	public List<Note> getAllNotes(int userId) throws TechnicalException {
		List<Note> listNote = new ArrayList<Note>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Note> query = session.createQuery(NOTEDAO_GET_ALL);
		query.setParameter("note_id_user", userId);
		listNote = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
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
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Note> query = session.createQuery(NOTEDAO_GET_ONE);
		query.setParameter("note_id", noteId);
		query.setParameter("note_id_user", userId);
		try {
			note = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("La note n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return note;
	}

	/**
	 * Ajoute une nouvelle note en base et renvoi son ID, ou exception fonctionnelle si impossible
	 * 
	 * @param note
	 * @return int
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public int add(Note note) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(note);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter la note");
		}
		return id;
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
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(note);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour la note");
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
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(note);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer la note");
		}
	}

}
