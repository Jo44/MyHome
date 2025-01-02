package fr.my.home.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.CustomFile;
import fr.my.home.dao.implementation.FileDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.files.CantDeleteException;
import fr.my.home.exception.files.ExistFileException;
import fr.my.home.exception.files.FileSizeException;
import fr.my.home.exception.files.NoFreeSpaceException;
import fr.my.home.exception.files.NoSentFileException;
import fr.my.home.exception.files.NotExistException;
import fr.my.home.exception.files.RequestSizeException;
import fr.my.home.tool.properties.Settings;

/**
 * Manager qui prends en charge la gestion des fichiers
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class FilesManager {
	private static final Logger logger = LogManager.getLogger(FilesManager.class);

	// Attributes

	private static final String MYHOME_PATH = System.getenv("MYHOME");
	private static final int FILES_BUFFER_SIZE = Settings.getIntProperty("files.buffer.size"); // 10 ko
	private static final long FILES_MAX_FILE_SIZE = Settings.getLongProperty("files.max.file.size"); // 100 Mo
	private static final long FILES_MAX_REQUEST_SIZE = Settings.getLongProperty("files.max.request.size"); // 250 Mo
	private static final String FILES_FOLDER = Settings.getStringProperty("files.folder");
	private FileDAO fileDAO;

	// Constructors

	/**
	 * Default Constructor
	 */
	public FilesManager() {
		super();
		fileDAO = new FileDAO();
	}

	// Methods

	/**
	 * Récupère la liste des fichiers pour l'utilisateur connecté
	 * 
	 * @param userId
	 * @return List<CustomFile>
	 * @throws TechnicalException
	 */
	public List<CustomFile> getFiles(int userId) throws TechnicalException {
		logger.debug("Tentative de récupération des fichiers en cours ..");
		List<CustomFile> listFile = null;
		try {
			// Récupère la liste des fichiers
			listFile = fileDAO.getAllFiles(userId);
			logger.debug("Récupération des fichiers enregistrés");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listFile;
	}

	/**
	 * Récupère le fichier à partir de son ID et de l'ID de l'utilisateur, ou erreur fonctionnelle si aucun fichier
	 * 
	 * @param fileId
	 * @param userId
	 * @return CustomFile
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public CustomFile getFile(int fileId, int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de récupération du fichier en cours ..");
		CustomFile file = null;
		try {
			// Récupère le fichier
			file = fileDAO.getOneFile(fileId, userId);
			logger.debug("Récupération du fichier enregistré");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new NotExistException("Fichier Inexistant");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return file;
	}

	/**
	 * Renvoi le téléchargement du fichier à l'utilisateur, ou erreur IO si erreur de lecture du fichier
	 * 
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	public void downloadFile(HttpServletResponse response, CustomFile file) throws IOException {
		logger.debug("Tentative de téléchargement d'un fichier en cours ..");
		try {
			// Récupère le nom du fichier
			String nameFile = file.getName();

			// Reforme le path de stockage du fichier à récupérer
			String path = MYHOME_PATH + "/" + FILES_FOLDER + "/" + file.getIdUser();

			// Renvoi le téléchargement au navigateur
			readFile(response, path, nameFile);
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
			throw ioe;
		}
	}

	/**
	 * Ajoute l'ensemble des fichiers transmis au formulaire et renvoi le nombre de fichier ajouté, ou erreur fonctionnelle/technique/IO si pas
	 * possible
	 * 
	 * @param fileParts
	 * @param userId
	 * @return int
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 * @throws IOException
	 */
	public int addFiles(List<Part> fileParts, int userId) throws FonctionnalException, TechnicalException, IOException {
		int nbFile = 0;
		// Vérifie si des fichiers sont sélectionnés ou non
		// Dans tous les cas récupère un part, donc on vérifie si il a un nom
		if (Paths.get(fileParts.get(0).getSubmittedFileName()).getFileName().toString().isEmpty()) {
			String error = "Aucun fichier envoyé";
			logger.debug(error);
			throw new NoSentFileException(error);
		} else {
			// Assemble le chemin principal de stockage des fichiers
			// {chemin}/{files folder}
			File fileFolder = new File(MYHOME_PATH + "//" + FILES_FOLDER);
			// Si le dossier n'existe pas, le créé
			if (!fileFolder.exists() || !fileFolder.isDirectory()) {
				logger.debug("Le dossier {" + FILES_FOLDER + "} n'existe pas. Création en cours ..");
				if (fileFolder.mkdir()) {
					logger.debug("Dossier {" + FILES_FOLDER + "} créé avec succès");
				} else {
					logger.error("Erreur lors de la création du dossier {" + FILES_FOLDER + "}");
					throw new IOException("Impossible de créer le dossier");
				}
			}
			// Assemble le chemin du dossier de stockage de l'utilisateur
			// {chemin}/{files folder}/{userId}
			File fileUserFolder = new File(MYHOME_PATH + "//" + FILES_FOLDER + "//" + userId);
			// Si le dossier n'existe pas, le créé
			if (!fileUserFolder.exists() || !fileUserFolder.isDirectory()) {
				logger.debug("Le dossier utilisateur {" + userId + "} n'existe pas. Création en cours ..");
				if (fileUserFolder.mkdir()) {
					logger.debug("Dossier utilisateur {" + userId + "} créé avec succès.");
				} else {
					logger.error("Erreur lors de la création du dossier utilisateur {" + userId + "}.");
					throw new IOException("Impossible de créer le dossier");
				}
			}
			// Vérification si les fichiers sont valides pour l'upload
			boolean validRequest = true;
			long requestSize = 0L;
			String failedNamePart = "";
			for (Part filePart : fileParts) {
				requestSize += filePart.getSize();
				if (filePart.getSize() > FILES_MAX_FILE_SIZE) {
					failedNamePart = filePart.getSubmittedFileName();
					validRequest = false;
				}
			}
			// Vérifie si l'utilisateur possède encore de l'espace de stockage dédié (5 Go / utilisateur)
			// Récupère l'espace restant avant tentative stockage
			long freeSpace = getFreeSpace(userId);
			// Récupère l'espace restant après tentative stockage
			long afterSentFreeSpace = freeSpace - requestSize;

			// Si les fichiers ne sont pas trop lourd et qu'il reste suffisament d'espace de stockage
			if (validRequest && requestSize <= FILES_MAX_REQUEST_SIZE && afterSentFreeSpace >= 0L) {
				// Enregistre chaque fichier en BDD et sur le disque
				for (Part filePart : fileParts) {
					// Récupère le nom avec max 100 caracteres (en gardant l'extension finale)
					String fileName = (Paths.get(filePart.getSubmittedFileName()).getFileName().toString());
					// Traitement pour nom max 100 carctères
					if (fileName.length() > 99) {
						// Récupère l'extension (si elle existe)
						if (fileName.lastIndexOf(".") > 0) {
							String ext = fileName.substring(fileName.lastIndexOf("."));
							if (ext.length() > 5) {
								ext = ext.substring(0, 6);
							}
							fileName = fileName.substring(0, 94);
							fileName += ext;
						} else {
							fileName = fileName.substring(0, 100);
						}
					}
					logger.debug("Nom du fichier : " + fileName);
					// Récupère le poids (en octets)
					long fileSize = filePart.getSize();
					logger.debug("Taille du fichier : " + fileSize);
					try {
						// Vérifie si un fichier portant ce nom existe déjà
						if (fileDAO.checkOneFileByName(fileName, userId)) {
							throw new ExistFileException("Un fichier portant ce nom existe déjà");
						}
						// Préparation pour ajout BDD
						CustomFile file = new CustomFile(userId, fileName, fileSize, Timestamp.valueOf(LocalDateTime.now()));
						// Ecriture en BDD ...
						logger.debug("Fichier -> " + file.toString());
						logger.debug("Chemin -> " + fileUserFolder.getAbsolutePath());
						fileDAO.add(file);
						// ... puis en stockage
						writeFile(filePart, fileUserFolder.toString(), fileName);
						logger.debug("Fichier ajouté avec succès");
						nbFile++;
					} catch (FonctionnalException fex) {
						logger.debug(fex.getMessage());
						throw new ExistFileException("Impossible d'ajouter le fichier");
					} catch (TechnicalException tex) {
						String error = "Erreur technique lors de l'ajout du fichier (" + fileName + ")";
						logger.error(error);
						throw new TechnicalException(error);
					} catch (IOException ioe) {
						String error = "Erreur IO lors de l'ajout du fichier (" + fileName + ")";
						logger.error(error);
						throw new IOException(error);
					}
				}
			} else {
				// Si les fichiers sont trop lourds ou espace de stockage disponible insuffisant
				if (!validRequest) {
					String error = "Le fichier '" + failedNamePart + "' est trop volumineux";
					logger.debug(error);
					throw new FileSizeException(error, failedNamePart);
				} else if (requestSize > FILES_MAX_REQUEST_SIZE) {
					String error = "Le total des fichiers est trop volumineux";
					logger.debug(error);
					throw new RequestSizeException(error);
				} else {
					String error = "Espace de stockage disponible insuffisant";
					logger.debug(error);
					throw new NoFreeSpaceException(error);
				}
			}
		}
		return nbFile;
	}

	/**
	 * Retourne l'espace de stockage restant pour l'utilisateur (Total : 5 Go)
	 * 
	 * @param userId
	 * @return long
	 * @throws TechnicalException
	 */
	private long getFreeSpace(int userId) throws TechnicalException {
		long freeSpace = 0L;
		long totalSpace = 5368709120L; // 5 Go
		long usedSpace = 0L;

		// Calcul le poids total déjà enregistré
		List<CustomFile> listFile = fileDAO.getAllFiles(userId);
		for (CustomFile file : listFile) {
			usedSpace += file.getWeight();
		}
		if (usedSpace >= totalSpace) {
			// Si poids max déjà atteint
			freeSpace = 0L;
		} else {
			// Si espace libre
			freeSpace = totalSpace - usedSpace;
		}
		return freeSpace;
	}

	/**
	 * Supprime un fichier de l'utilisateur de la base de données et du stockage, ou erreur fonctionnelle si aucun fichier
	 * 
	 * @param file
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deleteFile(CustomFile file) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'un fichier en cours ..");
		try {
			// Réforme le path intégral à partir du nom du fichier
			String path = MYHOME_PATH + "/" + FILES_FOLDER + "/" + String.valueOf(file.getIdUser()) + "/" + file.getName();
			logger.debug("Path du fichier : " + path);

			// Supprime le fichier de la base de donnée
			fileDAO.delete(file);
			logger.debug("Suppression du fichier {" + String.valueOf(file.getId()) + "} de la base");

			// Supprime le fichier du stockage
			File localFile = new File(path);
			if (localFile.delete()) {
				logger.debug("Suppression du fichier {" + String.valueOf(file.getId()) + "} du stockage");
			} else {
				String error = "Impossible de supprimer le fichier du stockage";
				logger.error(error);
				throw new FonctionnalException(error);
			}
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new CantDeleteException("Impossible de supprimer le fichier");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Organise la liste en fonction des paramètres
	 * 
	 * @param listFile
	 * @param orderBy
	 * @param dir
	 * @return List<CustomFile>
	 */
	public List<CustomFile> orderBy(List<CustomFile> listFile, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null && !orderBy.trim().isEmpty() && (orderBy.equals("date") | orderBy.equals("weight") | orderBy.equals("name"))
				&& dir != null && !dir.trim().isEmpty() && (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("date")) {
				// Si ordre par date
				listFile.sort(Comparator.comparing(CustomFile::getUploadDate).thenComparingInt(CustomFile::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listFile);
				}
			} else if (orderBy.equals("weight")) {
				// Si ordre par poids
				listFile.sort(Comparator.comparing(CustomFile::getWeight).thenComparing(CustomFile::getUploadDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listFile);
				}
			} else {
				// Si ordre par nom
				listFile.sort(Comparator.comparing(CustomFile::getName, String.CASE_INSENSITIVE_ORDER).thenComparing(CustomFile::getUploadDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listFile);
				}
			}
		} else {
			// Ordre par défaut
			listFile.sort(Comparator.comparing(CustomFile::getUploadDate).thenComparingInt(CustomFile::getId));
			Collections.reverse(listFile);
		}
		return listFile;
	}

	/**
	 * Ecrit le fichier sur le disque local à partir du part, du chemin et du nom
	 * 
	 * @param part
	 * @param path
	 * @param nameFile
	 * @throws IOException
	 */
	private void writeFile(Part part, String path, String nameFile) throws IOException {
		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		// Initialisation des buffers
		input = new BufferedInputStream(part.getInputStream(), FILES_BUFFER_SIZE);
		output = new BufferedOutputStream(new FileOutputStream(new File(path + "//" + nameFile)), FILES_BUFFER_SIZE);

		// Lit le fichier d'entrée et écrit son contenu dans le fichier de sortie
		byte[] buffer = new byte[FILES_BUFFER_SIZE];
		int length;
		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		output.close();
		input.close();
	}

	/**
	 * Envoi le fichier du disque local au navigateur
	 * 
	 * @param response
	 * @param path
	 * @param nameFile
	 * @throws IOException
	 */
	private void readFile(HttpServletResponse response, String path, String nameFile) throws IOException {
		// Reforme l'objet File
		File file = new File(path + "//" + nameFile);
		// Défini le type de fichier renvoyé
		response.setContentType("application/octet-stream");
		// Montre le téléchargement
		response.setHeader("Content-disposition", "attachment; filename=" + nameFile);

		// Envoi le fichier au navigateur
		OutputStream output = response.getOutputStream();
		FileInputStream input = new FileInputStream(file);
		byte[] buffer = new byte[FILES_BUFFER_SIZE];
		int length;
		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		input.close();
		output.flush();
	}

}
