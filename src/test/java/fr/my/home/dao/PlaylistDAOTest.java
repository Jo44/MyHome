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

import fr.my.home.bean.ActivePlaylist;
import fr.my.home.dao.implementation.PlaylistDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe PlaylistDAOTest qui teste le stockage des playlists
 * 
 * @author Jonathan
 * @version 1.1
 */
public class PlaylistDAOTest {

	/**
	 * Attributs
	 */

	private PlaylistDAO ytPlaylistDAO;

	/**
	 * Constructeur
	 */
	public PlaylistDAOTest() {
		super();
		ytPlaylistDAO = new PlaylistDAO();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe PlaylistDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste de tous les playlists de l'utilisateur (valide)
	 */
	@Test
	public void getAllPlaylistsOk() {
		List<ActivePlaylist> listYtPlaylist = null;
		try {
			listYtPlaylist = ytPlaylistDAO.getAllPlaylists(0);
		} catch (TechnicalException ex) {
			listYtPlaylist = null;
		}
		assertNotNull(listYtPlaylist);
	}

	/**
	 * Récupère la liste de tous les playlists actives de l'utilisateur (valide)
	 */
	@Test
	public void getAllActivePlaylistsOk() {
		List<ActivePlaylist> listYtPlaylist = null;
		try {
			listYtPlaylist = ytPlaylistDAO.getAllActivePlaylists(0);
		} catch (TechnicalException ex) {
			listYtPlaylist = null;
		}
		assertNotNull(listYtPlaylist);
	}

	/**
	 * Récupère la playlist selon son ID et l'ID de l'utilisateur (valide)
	 */
	@Test
	public void getOnePlaylistOk() {
		ActivePlaylist ytPlaylist = new ActivePlaylist(1, "0000", true, Timestamp.valueOf(LocalDateTime.now()));
		try {
			ytPlaylistDAO.add(ytPlaylist);
			ytPlaylist = ytPlaylistDAO.getOnePlaylist(ytPlaylist.getIdUser(), "0000");
			ytPlaylistDAO.delete(ytPlaylist);
		} catch (FonctionnalException | TechnicalException ex) {
			ytPlaylist = null;
		}
		assertNotNull(ytPlaylist);
	}

	/**
	 * Récupère la playlist selon son ID et l'ID de l'utilisateur (non-valide)
	 */
	@Test
	public void getOnePlaylistKo() {
		ActivePlaylist ytPlaylist = new ActivePlaylist();
		try {
			ytPlaylist = ytPlaylistDAO.getOnePlaylist(ytPlaylist.getIdUser(), "0000");
		} catch (TechnicalException tex) {
			ytPlaylist = null;
		}
		assertNull(ytPlaylist);
	}

	/**
	 * Ajoute une playlist (valide)
	 */
	@Test
	public void addOk() {
		boolean valid = false;
		ActivePlaylist ytPlaylist = new ActivePlaylist(1, "0000", true, Timestamp.valueOf(LocalDateTime.now()));
		try {
			ytPlaylistDAO.add(ytPlaylist);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				ytPlaylistDAO.delete(ytPlaylist);
			} catch (FonctionnalException | TechnicalException ex) {
				ex.printStackTrace();
			}
		}
		assertTrue(valid);
	}

	/**
	 * Ajoute une playlist (non-valide)
	 */
	@Test
	public void addKo() {
		boolean valid = true;
		ActivePlaylist ytPlaylist = new ActivePlaylist(1, null, true, Timestamp.valueOf(LocalDateTime.now()));
		try {
			ytPlaylistDAO.add(ytPlaylist);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				ytPlaylistDAO.delete(ytPlaylist);
			} catch (FonctionnalException | TechnicalException ex) {}
		}
		assertFalse(valid);
	}

	/**
	 * Modifie une playlist (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		ActivePlaylist ytPlaylist = new ActivePlaylist(1, "0000", true, Timestamp.valueOf(LocalDateTime.now()));
		try {
			ytPlaylistDAO.add(ytPlaylist);
			try {
				ytPlaylist.setIdYouTube("1111");
				ytPlaylistDAO.update(ytPlaylist);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					ytPlaylistDAO.delete(ytPlaylist);
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
	 * Modifie une playlist (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		ActivePlaylist ytPlaylist = new ActivePlaylist(1, "0000", true, Timestamp.valueOf(LocalDateTime.now()));
		try {
			ytPlaylistDAO.add(ytPlaylist);
			try {
				ytPlaylist.setIdYouTube(null);
				ytPlaylistDAO.update(ytPlaylist);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					ytPlaylist.setIdYouTube("0000");
					ytPlaylistDAO.delete(ytPlaylist);
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
	 * Supprime un contact (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		ActivePlaylist ytPlaylist = new ActivePlaylist(1, "0000", true, Timestamp.valueOf(LocalDateTime.now()));
		try {
			ytPlaylistDAO.add(ytPlaylist);
			ytPlaylistDAO.delete(ytPlaylist);
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
		System.out.println("Fin des tests de la classe PlaylistDAO");
	}

}
