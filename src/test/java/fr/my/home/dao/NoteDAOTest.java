package fr.my.home.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.my.home.bean.Note;
import fr.my.home.dao.implementation.NoteDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe NoteDAOTest qui teste le stockage des notes
 * 
 * @author Jonathan
 * @version 1.1
 */
public class NoteDAOTest {

	/**
	 * Attributs
	 */

	private NoteDAO noteDAO;

	/**
	 * Constructeur
	 */
	public NoteDAOTest() {
		super();
		noteDAO = new NoteDAO();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe NoteDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste de toutes les notes de l'utilisateur (valide)
	 */
	@Test
	public void getAllNotesOk() {
		List<Note> listNote = null;
		try {
			listNote = noteDAO.getAllNotes(0);
		} catch (TechnicalException ex) {
			listNote = null;
		}
		assertNotNull(listNote);
	}

	/**
	 * Récupère la note selon son ID et l'ID de l'utilisateur (valide)
	 */
	@Test
	public void getOneNoteOk() {
		Note note = new Note(1, "title", "message", Timestamp.valueOf(LocalDateTime.now()));
		try {
			noteDAO.add(note);
			note = noteDAO.getOneNote(note.getId(), 1);
			noteDAO.delete(note);
		} catch (FonctionnalException | TechnicalException ex) {
			note = null;
		}
		assertNotNull(note);
	}

	/**
	 * Récupère la note selon son ID et l'ID de l'utilisateur (non-valide)
	 */
	@Test
	public void getOneContactKo() {
		Note note = new Note();
		try {
			note = noteDAO.getOneNote(0, 1);
		} catch (FonctionnalException | TechnicalException ex) {
			note = null;
		}
		assertNull(note);
	}

	/**
	 * Ajoute une note (valide)
	 */
	@Test
	public void addOk() {
		boolean valid = false;
		Note note = new Note(1, "title", "message", Timestamp.valueOf(LocalDateTime.now()));
		try {
			noteDAO.add(note);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				noteDAO.delete(note);
			} catch (FonctionnalException | TechnicalException ex) {
				ex.printStackTrace();
			}
		}
		assertTrue(valid);
	}

	/**
	 * Ajoute une note (non-valide)
	 */
	@Test
	public void addKo() {
		boolean valid = true;
		Note note = new Note(1, null, "message", Timestamp.valueOf(LocalDateTime.now()));
		try {
			noteDAO.add(note);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				noteDAO.delete(note);
			} catch (FonctionnalException | TechnicalException ex) {}
		}
		assertFalse(valid);
	}

	/**
	 * Modifie une note (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		Note note = new Note(1, "title", "message", Timestamp.valueOf(LocalDateTime.now()));
		try {
			noteDAO.add(note);
			try {
				note.setMessage("anotherMessage");
				noteDAO.update(note);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					noteDAO.delete(note);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		} catch (FonctionnalException | TechnicalException ex) {
			ex.printStackTrace();
		}
		assertTrue(valid);
	}

	/**
	 * Modifie une note (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		Note note = new Note(1, "title", "message", Timestamp.valueOf(LocalDateTime.now()));
		try {
			noteDAO.add(note);
			try {
				note.setTitle(null);
				noteDAO.update(note);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					note.setTitle("title");
					noteDAO.delete(note);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		} catch (FonctionnalException | TechnicalException ex) {
			ex.printStackTrace();
		}
		assertFalse(valid);
	}

	/**
	 * Supprime une note (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		Note note = new Note(1, "title", "message", Timestamp.valueOf(LocalDateTime.now()));
		try {
			noteDAO.add(note);
			noteDAO.delete(note);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Fin des tests
	 * 
	 * @throws Exception
	 */
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Fin des tests de la classe NoteDAO");
	}

}
