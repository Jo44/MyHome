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

import fr.my.home.bean.YouTubeActivPlaylist;
import fr.my.home.dao.implementation.YouTubePlaylistDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe YouTubePlaylistDAOTest qui teste le stockage des playlists
 * 
 * @author Jonathan
 * @version 1.0
 */
public class YouTubePlaylistDAOTest {

	// Attributs
	private YouTubePlaylistDAO ytPlaylistDAO;
	private Timestamp ts;

	// Constructeur
	public YouTubePlaylistDAOTest() {
		super();
		ytPlaylistDAO = new YouTubePlaylistDAO();
		ts = Timestamp.valueOf(LocalDateTime.now());
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe YouTubePlaylistDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste de tous les playlists de l'utilisateur (valide)
	 */
	@Test
	public void getAllPlaylistsOk() {
		List<YouTubeActivPlaylist> listYtPlaylist = null;
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
		List<YouTubeActivPlaylist> listYtPlaylist = null;
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
		YouTubeActivPlaylist ytPlaylist = new YouTubeActivPlaylist(1, "0000", true, ts);
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
		YouTubeActivPlaylist ytPlaylist = new YouTubeActivPlaylist();
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
		int id = 0;
		YouTubeActivPlaylist ytPlaylist = null;
		try {
			ytPlaylist = new YouTubeActivPlaylist(1, "0000", true, ts);
			id = ytPlaylistDAO.add(ytPlaylist);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		} finally {
			if (id != 0) {
				try {
					ytPlaylistDAO.delete(ytPlaylist);
				} catch (FonctionnalException | TechnicalException ex) {
					ex.printStackTrace();
				}
			}
		}
		assertTrue(id != 0);
	}

	/**
	 * Ajoute une playlist (non-valide)
	 */
	@Test
	public void addKo() {
		int id;
		try {
			YouTubeActivPlaylist ytPlaylist = new YouTubeActivPlaylist(1, null, true, ts);
			id = ytPlaylistDAO.add(ytPlaylist);
		} catch (FonctionnalException | TechnicalException ex) {
			id = 0;
		}
		assertTrue(id == 0);
	}

	/**
	 * Modifie une playlist (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		YouTubeActivPlaylist ytPlaylist = new YouTubeActivPlaylist(1, "0000", true, ts);
		try {
			int id = ytPlaylistDAO.add(ytPlaylist);
			try {
				ytPlaylist.setIdYouTube("1111");
				ytPlaylistDAO.update(ytPlaylist);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				if (id != 0) {
					try {
						ytPlaylistDAO.delete(ytPlaylist);
					} catch (FonctionnalException | TechnicalException ex) {
						ex.printStackTrace();
					}
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
		YouTubeActivPlaylist ytPlaylist = new YouTubeActivPlaylist(1, "0000", true, ts);
		try {
			int id = ytPlaylistDAO.add(ytPlaylist);
			try {
				ytPlaylist.setIdYouTube(null);
				ytPlaylistDAO.update(ytPlaylist);
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				if (id != 0) {
					try {
						ytPlaylistDAO.delete(ytPlaylist);
					} catch (FonctionnalException | TechnicalException ex) {
						ex.printStackTrace();
					}
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
		YouTubeActivPlaylist ytPlaylist = new YouTubeActivPlaylist(1, "0000", true, ts);
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
		System.out.println("Fin des tests de la classe YouTubePlaylistDAO");
	}

}
