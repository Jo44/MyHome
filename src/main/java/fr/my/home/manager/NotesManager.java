package fr.my.home.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import fr.my.home.bean.Note;
import fr.my.home.dao.implementation.NoteDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.notes.CantDeleteException;
import fr.my.home.exception.notes.DateHourException;
import fr.my.home.exception.notes.NotExistException;
import fr.my.home.exception.notes.TitleException;

/**
 * Manager qui prends en charge la gestion des notes
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class NotesManager {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(NotesManager.class);
	private NoteDAO noteDAO;

	/**
	 * Constructeur
	 */
	public NotesManager() {
		super();
		noteDAO = new NoteDAO();
	}

	/**
	 * Récupère la liste des notes pour l'utilisateur connecté
	 * 
	 * @param userId
	 * @return List<Note>
	 * @throws TechnicalException
	 */
	public List<Note> getNotes(int userId) throws TechnicalException {
		logger.debug("Tentative de récupération des notes en cours ..");
		List<Note> listNote = null;
		try {
			// Récupère la liste des notes
			listNote = noteDAO.getAllNotes(userId);
			// Enlève le code HTML du contenu des messages pour affichage plus propre
			for (Note note : listNote) {
				note.setMessage(Jsoup.parse(note.getMessage()).text());
			}
			logger.debug("Récupération des notes enregistrées");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listNote;
	}

	/**
	 * Récupère la note à partir de son ID et de l'ID de l'utilisateur, ou erreur fonctionnelle si aucune note
	 * 
	 * @param noteId
	 * @param userId
	 * @return Note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Note getNote(int noteId, int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de récupération d'une note en cours ..");
		Note note = null;
		try {
			// Récupère la note
			note = noteDAO.getOneNote(noteId, userId);
			logger.debug("Récupération d'une note enregistrée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new NotExistException("Note Inexistante");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return note;
	}

	/**
	 * Vérifie les champs du formulaire puis ajoute la note en base, ou renvoi exception fonctionnelle / technique
	 * 
	 * @param userId
	 * @param dateTime
	 * @param title
	 * @param message
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addNote(int userId, Timestamp dateTime, String title, String message) throws FonctionnalException, TechnicalException {
		// Vérifie si les champs sont bien tous valides
		checkParamsNote(dateTime, title);
		try {
			// Ajout de la nouvelle note
			Note note = new Note(userId, title.trim(), message.trim(), dateTime);
			noteDAO.add(note);
			logger.debug("Ajout d'une nouvelle note réussi");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Supprime la note ou exception fonctionnelle si problème
	 * 
	 * @param note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deleteNote(Note note) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'une note en cours ..");
		try {
			// Supprime la note
			noteDAO.delete(note);
			logger.debug("Suppression de la note {" + String.valueOf(note.getId()) + "} de la base");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new CantDeleteException("Impossible de supprimer la note");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Vérifie les paramètres d'ajout d'une note et renvoi une exception fonctionnelle si problème
	 * 
	 * @param dateTime
	 * @param title
	 * @throws FonctionnalException
	 */
	private void checkParamsNote(Timestamp dateTime, String title) throws FonctionnalException {
		if (dateTime == null) {
			throw new DateHourException("Date / Heure manquante");
		} else if (title == null || title.trim().length() < 1 || title.trim().length() > 100) {
			throw new TitleException("Titre manquant");
		}
	}

	/**
	 * Organise la liste en fonction des paramètres
	 * 
	 * @param listNote
	 * @param orderBy
	 * @param dir
	 * @return List<Note>
	 */
	public List<Note> orderBy(List<Note> listNote, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null && !orderBy.trim().isEmpty() && (orderBy.equals("date") | orderBy.equals("title") | orderBy.equals("message"))
				&& dir != null && !dir.trim().isEmpty() && (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("date")) {
				// Si ordre par date
				listNote.sort(Comparator.comparing(Note::getSaveDate).thenComparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER)
						.thenComparingInt(Note::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listNote);
				}
			} else if (orderBy.equals("title")) {
				// Si ordre par titre
				listNote.sort(Comparator.comparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER)
						.thenComparing(Note::getMessage, String.CASE_INSENSITIVE_ORDER).thenComparing(Note::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listNote);
				}
			} else {
				// Si ordre par message
				listNote.sort(Comparator.comparing(Note::getMessage, String.CASE_INSENSITIVE_ORDER)
						.thenComparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER).thenComparing(Note::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listNote);
				}
				// Met les entrées vides à la fin de la liste
				List<Note> emptyListNote = new ArrayList<Note>();
				for (Iterator<Note> it = listNote.iterator(); it.hasNext();) {
					Note note = it.next();
					if (note.getMessage() == null | note.getMessage().trim().isEmpty() | note.getMessage().trim().equals("<br>")) {
						emptyListNote.add(note);
						it.remove();
					}
				}
				if (emptyListNote.size() > 0) {
					listNote.addAll(emptyListNote);
				}
			}
		} else {
			// Ordre par défaut
			listNote.sort(Comparator.comparing(Note::getSaveDate).thenComparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER)
					.thenComparingInt(Note::getId));
			Collections.reverse(listNote);
		}
		return listNote;
	}

}
