package fr.my.home.servlet.files;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.CustomFile;
import fr.my.home.bean.User;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
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

/**
 * Servlet qui prends en charge la gestion des fichiers
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/files")
@MultipartConfig(fileSizeThreshold = 0, maxFileSize = 1024 * 1024 * 100, maxRequestSize = 1024 * 1024 * 250)
public class FilesServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(FilesServlet.class);

	// Attributes

	private FilesManager fileMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public FilesServlet() {
		super();
		// Initialisation du manager
		fileMgr = new FilesManager();
	}

	// Methods

	/**
	 * Redirection vers le téléchargement, la liste et la suppression de fichiers
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Files Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Récupère l'attribut success si il existe
		String success = (String) request.getSession().getAttribute("success");
		request.getSession().removeAttribute("success");
		view.addAttributeToList(new ViewAttribut("success", success));

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère le nombre de lignes max d'un tableau à afficher (pour traitement javascript)
		int maxRows = GlobalTools.getMaxRows(request);
		view.addAttributeToList(new ViewAttribut("maxRows", maxRows));

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
					boolean exist = getFunction(response, view, fileId, userId, lang);
					if (!exist) {
						// Si il n'existe pas, renvoi à la liste des fichiers
						redirectToFiles(request, response, view, userId, null, null, lang);
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
					redirectToFiles(request, response, view, userId, orderBy, dir, lang);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des fichiers
					redirectToFiles(request, response, view, userId, null, null, lang);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des fichiers
			redirectToFiles(request, response, view, userId, null, null, lang);
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
	 * Récupère le fichier selon son ID et essaye de renvoyer le téléchargement à l'utilisateur et charge erreur dans la view si besoin
	 * 
	 * @param response
	 * @param view
	 * @param fileId
	 * @param userId
	 * @param lang
	 * @return boolean
	 */
	private boolean getFunction(HttpServletResponse response, ViewJSP view, int fileId, int userId, String lang) {
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
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.file.not.exist", lang)));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		} catch (IOException ioe) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.file.cant.get", lang)));
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

			// Ajoute le message de succès dans la view
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
	 * @param view
	 * @param userId
	 * @param orderBy
	 * @param dir
	 * @param lang
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToFiles(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, String orderBy, String dir,
			String lang) throws ServletException, IOException {
		List<CustomFile> listFile = null;
		long usedSpace = 0L;
		try {
			// Récupère la liste des fichiers de l'utilisateur
			listFile = fileMgr.getFiles(userId);

			// Récupère le poids total utilisé
			for (CustomFile file : listFile) {
				usedSpace += file.getWeight();
			}

			// Tri la liste en fonction des paramètres
			listFile = fileMgr.orderBy(listFile, orderBy, dir);

		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}
		// Ajoute la liste dans la view
		view.addAttributeToList(new ViewAttribut("listFile", listFile));
		// Ajoute l'espace disque utilisé dans la view
		view.addAttributeToList(new ViewAttribut("usedSpace", usedSpace));
		// Redirection
		redirectToFilesJSP(request, response, view);
	}

	/**
	 * Redirige la requête vers la JSP Files
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToFilesJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

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
