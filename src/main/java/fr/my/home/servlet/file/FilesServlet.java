package fr.my.home.servlet.file;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.CustomFile;
import fr.my.home.bean.User;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.files.CantDeleteException;
import fr.my.home.exception.files.ExistFileException;
import fr.my.home.exception.files.FileSizeException;
import fr.my.home.exception.files.NoFreeSpaceException;
import fr.my.home.exception.files.NoSentFileException;
import fr.my.home.exception.files.NotExistException;
import fr.my.home.exception.files.RequestSizeException;
import fr.my.home.manager.FilesManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Servlet qui prends en charge la gestion des fichiers
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/files")
@MultipartConfig(fileSizeThreshold = 0, maxFileSize = 1024 * 1024 * 100, maxRequestSize = 1024 * 1024 * 250)
public class FilesServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(FilesServlet.class);
	private FilesManager fileMgr;

	/**
	 * Constructeur
	 */
	public FilesServlet() {
		super();
		// Initialisation du manager
		fileMgr = new FilesManager();
	}

	/**
	 * Redirection vers le téléchargement, la liste et la suppression de fichiers
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Files Servlet [GET] -->");

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		request.setAttribute("error", error);

		// Récupère l'attribut success si il existe
		String success = (String) request.getSession().getAttribute("success");
		request.getSession().removeAttribute("success");
		request.setAttribute("success", success);

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère le nombre de lignes max d'un tableau à afficher (pour traitement javascript)
		int maxRows = GlobalTools.getMaxRows(request);
		request.setAttribute("maxRows", maxRows);

		// Récupère les paramètres de la requête
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		int fileId;
		try {
			fileId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			fileId = 0;
		}
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action télécharger
				case "get":
					// Renvoi le fichier demandé au navigateur de l'utilisateur
					boolean exist = getFunction(request, response, fileId, userId, lang);
					if (!exist) {
						// Si il n'existe pas, renvoi à la liste des fichiers
						redirectToFiles(request, response, userId, null, null, lang);
					}
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer le fichier
					deleteFunction(request, fileId, userId, lang);

					// Puis renvoi à la liste des fichiers
					redirectToThisServletAfterPostOrDelete(request, response);
					break;
				// Si action lister
				case "list":
					// Renvoi à la liste des fichiers
					redirectToFiles(request, response, userId, orderBy, dir, lang);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des fichiers
					redirectToFiles(request, response, userId, null, null, lang);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des fichiers
			redirectToFiles(request, response, userId, null, null, lang);
		}
	}

	/**
	 * Traitement du formulaire d'ajout de fichiers
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Files Servlet [POST] -->");
		logger.info("Tentative d'upload de fichier(s) ..");
		int nbAddedFile;

		// Récupère l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		try {
			// Récupère la liste des parts du formulaire (fichiers envoyés ou non)
			// <input type="file" name="file" multiple="true">
			List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());

			// Essaye d'ajouter les fichiers en base et sur le stockage
			nbAddedFile = fileMgr.addFiles(fileParts, user.getId());

			// Ajoute le message de succès dans la session (en fonction du nombre de fichier ajouté)
			if (nbAddedFile > 1) {
				request.getSession().setAttribute("success", Messages.getProperty("success.file.adds", lang));
			} else {
				request.getSession().setAttribute("success", Messages.getProperty("success.file.add", lang));
			}
		} catch (FonctionnalException fex) {
			if (fex instanceof NoSentFileException) {
				// Aucun fichier envoyé
				request.getSession().setAttribute("error", Messages.getProperty("error.file.no.sent", lang));
			} else if (fex instanceof ExistFileException) {
				// Fichier existant avec nom identique
				request.getSession().setAttribute("error", Messages.getProperty("error.file.already.exist", lang));
			} else if (fex instanceof FileSizeException) {
				// Fichier trop volumineux
				FileSizeException fsex = (FileSizeException) fex;
				String msg = Messages.getProperty("error.file.size.msg1", lang) + " " + fsex.getFilename() + " "
						+ Messages.getProperty("error.file.size.msg2", lang);
				request.getSession().setAttribute("error", msg);
			} else if (fex instanceof RequestSizeException) {
				// Requête trop volumineuse
				request.getSession().setAttribute("error", Messages.getProperty("error.file.request.size", lang));
			} else if (fex instanceof NoFreeSpaceException) {
				// Espace de stockage insuffisant
				request.getSession().setAttribute("error", Messages.getProperty("error.file.no.free.space", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		} catch (IOException ioe) {
			request.getSession().setAttribute("error", Messages.getProperty("error.file.io", lang));
		} catch (IllegalStateException ise) {
			String error = "La requête est trop volumineuse !";
			logger.error(error);
			request.getSession().setAttribute("error", Messages.getProperty("error.file.request.size", lang));
		} finally {
			// Redirection vers la servlet en GET
			redirectToThisServletAfterPostOrDelete(request, response);
		}
	}

	/**
	 * Récupère le fichier selon son ID et essaye de renvoyer le téléchargement à l'utilisateur et charge erreur dans la requête si besoin
	 * 
	 * @param request
	 * @param response
	 * @param fileId
	 * @param userId
	 * @param lang
	 * @return boolean
	 */
	private boolean getFunction(HttpServletRequest request, HttpServletResponse response, int fileId, int userId, String lang) {
		boolean valid = false;
		CustomFile file = null;
		try {
			// Récupère le fichier selon son ID et l'ID de l'utilisateur
			file = fileMgr.getFile(fileId, userId);

			// Renvoi le téléchargement à l'utilisateur
			fileMgr.downloadFile(response, file);

			// Le téléchargement est possible
			valid = true;
		} catch (FonctionnalException fex) {
			request.setAttribute("error", Messages.getProperty("error.file.not.exist", lang));
		} catch (TechnicalException tex) {
			request.setAttribute("error", Messages.getProperty("error.database", lang));
		} catch (IOException ioe) {
			request.setAttribute("error", Messages.getProperty("error.file.cant.get", lang));
		}
		return valid;
	}

	/**
	 * Récupère le fichier selon son ID et essaye de le supprimer de la base de donnée et du stockage, charge le message succes/erreur
	 * 
	 * @param request
	 * @param fileId
	 * @param userId
	 * @param lang
	 */
	private void deleteFunction(HttpServletRequest request, int fileId, int userId, String lang) {
		CustomFile file = null;
		try {
			// Récupère le fichier selon son ID et l'ID de l'utilisateur
			file = fileMgr.getFile(fileId, userId);

			// Supprime le fichier
			fileMgr.deleteFile(file);

			// Ajoute le message de succès dans la requête
			request.getSession().setAttribute("success", Messages.getProperty("success.file.delete", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof NotExistException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.file.not.exist", lang));
			} else if (fex instanceof CantDeleteException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.file.cant.delete", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Récupère la liste des fichiers de l'utilisateur, le poids total utilisé, tri la liste en fonction des paramètres, puis renvoi vers la JSP
	 * fichiers avec message d'erreur si besoin
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToFiles(HttpServletRequest request, HttpServletResponse response, int userId, String orderBy, String dir, String lang)
			throws ServletException, IOException {
		List<CustomFile> listFile = null;
		long usedSpace = 0L;
		long progressValue = 0L;
		try {
			// Récupère la liste des fichiers de l'utilisateur
			listFile = fileMgr.getFiles(userId);
			// Récupère le poids total utilisé
			for (CustomFile file : listFile) {
				usedSpace += file.getWeight();
			}
			// Détermine le pourcentage d'utilisation total
			if (usedSpace <= 0) {
				progressValue = 0;
			} else if (usedSpace >= 5368709120L) {
				progressValue = 100;
			} else {
				progressValue = (usedSpace * 100) / 5368709120L;
			}
			// Tri la liste en fonction des paramètres
			listFile = fileMgr.orderBy(listFile, orderBy, dir);

		} catch (TechnicalException tex) {
			request.setAttribute("error", Messages.getProperty("error.database", lang));
		} finally {
			// Format l'affichage du poids total
			String formattedUsedSpace = GlobalTools.formatWeightToString(usedSpace);
			// Ajoute les attributs à la requête
			request.setAttribute("progressValue", progressValue);
			request.setAttribute("formattedUsedSpace", formattedUsedSpace);
			request.setAttribute("listFile", listFile);
			request.setAttribute("formatterDate", new SimpleDateFormat("dd/MM/yyyy - HH:mm"));
			// Redirection
			redirectToFilesJSP(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP Files
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToFilesJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Files JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/files/files.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet en Get après un Post
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServletAfterPostOrDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString());
	}

}
