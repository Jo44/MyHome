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

import fr.my.home.bean.CustomFile;
import fr.my.home.dao.implementation.FileDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe FileDAOTest qui teste le stockage des fichiers
 * 
 * @author Jonathan
 * @version 1.1
 */
public class FileDAOTest {

	/**
	 * Attributs
	 */

	private FileDAO fileDAO;

	/**
	 * Constructeur
	 */
	public FileDAOTest() {
		super();
		fileDAO = new FileDAO();
	}

	/**
	 * Début des tests
	 * 
	 * @throws Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Début des tests de la classe FileDAO");
	}

	// Test des méthodes

	/**
	 * Récupère la liste de tous les fichiers de l'utilisateur (valide)
	 */
	@Test
	public void getAllFilesOk() {
		List<CustomFile> listFile = null;
		try {
			listFile = fileDAO.getAllFiles(0);
		} catch (TechnicalException ex) {
			listFile = null;
		}
		assertNotNull(listFile);
	}

	/**
	 * Récupère le fichier selon son ID et l'ID de l'utilisateur (valide)
	 */
	@Test
	public void getOneFileOk() {
		CustomFile file = new CustomFile(1, "filename", 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			fileDAO.add(file);
			file = fileDAO.getOneFile(file.getId(), 1);
			fileDAO.delete(file);
		} catch (FonctionnalException | TechnicalException ex) {
			file = null;
		}
		assertNotNull(file);
	}

	/**
	 * Récupère le fichier selon son ID et l'ID de l'utilisateur (non-valide)
	 */
	@Test
	public void getOneFileKo() {
		CustomFile file = new CustomFile();
		try {
			file = fileDAO.getOneFile(0, 1);
		} catch (FonctionnalException | TechnicalException ex) {
			file = null;
		}
		assertNull(file);
	}

	/**
	 * Vérifie si un fichier avec ce nom existe déjà pour l'utilisateur (existe)
	 */
	@Test
	public void checkOneFileByNameExist() {
		boolean exist = false;
		CustomFile file = new CustomFile(1, "uniqueFilename", 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			fileDAO.add(file);
			exist = fileDAO.checkOneFileByName("uniqueFilename", 1);
			fileDAO.delete(file);
		} catch (FonctionnalException | TechnicalException ex) {
			exist = false;
		}
		assertTrue(exist);
	}

	/**
	 * Vérifie si un fichier avec ce nom existe déjà pour l'utilisateur (existe pas)
	 */
	@Test
	public void checkOneFileByNameNotExist() {
		boolean exist = true;
		try {
			exist = fileDAO.checkOneFileByName("newFilename", 1);
		} catch (TechnicalException ex) {
			exist = true;
		}
		assertFalse(exist);
	}

	/**
	 * Ajoute un fichier (valide)
	 */
	@Test
	public void addOk() {
		boolean valid = false;
		CustomFile file = new CustomFile(1, "filename", 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			fileDAO.add(file);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				fileDAO.delete(file);
			} catch (FonctionnalException | TechnicalException ex) {
				ex.printStackTrace();
			}
		}
		assertTrue(valid);
	}

	/**
	 * Ajoute un fichier (non-valide)
	 */
	@Test
	public void addKo() {
		boolean valid = true;
		CustomFile file = new CustomFile(1, null, 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			fileDAO.add(file);
			valid = true;
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		} finally {
			try {
				fileDAO.delete(file);
			} catch (FonctionnalException | TechnicalException ex) {}
		}
		assertFalse(valid);
	}

	/**
	 * Modifie un fichier (valide)
	 */
	@Test
	public void updateOk() {
		boolean valid = false;
		CustomFile file = new CustomFile(1, "filename", 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			fileDAO.add(file);
			try {
				file.setName("anotherFilename");
				fileDAO.update(file);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					fileDAO.delete(file);
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
	 * Modifie un fichier (non-valide)
	 */
	@Test
	public void updateKo() {
		boolean valid = true;
		CustomFile file = new CustomFile(1, "filename", 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			fileDAO.add(file);
			try {
				file.setName(null);
				fileDAO.update(file);
				valid = true;
			} catch (FonctionnalException | TechnicalException ex) {
				valid = false;
			} finally {
				try {
					file.setName("filename");
					fileDAO.delete(file);
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
	 * Supprime un fichier (valide)
	 */
	@Test
	public void deleteOk() {
		boolean valid = false;
		CustomFile file = new CustomFile(1, "filename", 0, Timestamp.valueOf(LocalDateTime.now()));
		try {
			fileDAO.add(file);
			fileDAO.delete(file);
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
		System.out.println("Fin des tests de la classe FileDAO");
	}

}
