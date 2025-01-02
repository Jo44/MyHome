package fr.my.home.servlet.notes;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Note;
import fr.my.home.bean.User;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.notes.CantDeleteException;
import fr.my.home.exception.notes.DateHourException;
import fr.my.home.exception.notes.NotExistException;
import fr.my.home.exception.notes.TitleException;
import fr.my.home.exception.notes.UsedTitleException;
import fr.my.home.manager.NotesManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion des notes
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/notes")
public class NotesServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(NotesServlet.class);

	// Attributes

	private NotesManager noteMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public NotesServlet() {
		super();
		// Initialisation du manager
		noteMgr = new NotesManager();
	}

	// Methods

	/**
	 * Redirection vers l'ajout, la liste, le détail et la suppression de notes
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Notes Servlet [GET] -->");

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
		int noteId;
		try {
			noteId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			noteId = 0;
		}
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					// Récupère l'heure actuelle pour redirection vers la page d'ajout d'une note
					addFunction(view);

					// Puis renvoi à l'ajout d'une note
					redirectToNoteNewJSP(request, response, view);
					break;
				// Si action details
				case "details":
					// Récupère la note
					boolean exist = getFunction(view, noteId, userId, lang);
					if (exist) {
						// Si la note existe pour l'utilisateur, redirection vers la page de détails
						redirectToNoteDetailsJSP(request, response, view, noteId);
					} else {
						// Si la note n'existe pas, renvoi à la liste des notes
						redirectToNotesList(request, response, view, userId, null, null, lang);
					}
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer la note
					deleteFunction(request, noteId, userId, lang);

					// Puis renvoi à la liste des notes
					redirectToThisServletAfterPostOrDelete(request, response);
					break;
				// Si action lister
				case "list":
					// Renvoi à la liste des notes
					redirectToNotesList(request, response, view, userId, orderBy, dir, lang);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des notes
					redirectToNotesList(request, response, view, userId, null, null, lang);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des notes
			redirectToNotesList(request, response, view, userId, null, null, lang);
		}
	}

	/**
	 * Traitement du formulaire d'ajout d'une note
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Notes Servlet [POST] -->");
		logger.info("Tentative d'ajout d'une note en cours ..");

		// Récupère de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		Timestamp dateTime = GlobalTools.formatStringToTimestamp(request.getParameter("dateTime"));
		String title = new String(request.getParameter("title").trim().getBytes("ISO-8859-1"), "UTF-8");
		String message = new String(request.getParameter("message").trim().getBytes("ISO-8859-1"), "UTF-8");

		try {
			// Ajoute la nouvelle note
			noteMgr.addNote(user.getId(), dateTime, title, message);

			// Ajoute le message de succès en session
			request.getSession().setAttribute("success", Messages.getProperty("success.note.add", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof DateHourException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.note.date.hour", lang));
			} else if (fex instanceof TitleException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.note.title", lang));
			} else if (fex instanceof UsedTitleException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.note.title.used", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		} finally {
			// Redirection vers la servlet en GET
			redirectToThisServletAfterPostOrDelete(request, response);
		}
	}

	/**
	 * Récupère la note selon son ID et la charge ainsi que l'erreur dans la view si besoin et charge la view dans la requête
	 * 
	 * @param view
	 * @param noteId
	 * @param userId
	 * @param lang
	 * @return boolean
	 */
	private boolean getFunction(ViewJSP view, int noteId, int userId, String lang) {
		boolean valid = false;
		Note note = null;
		try {
			// Récupère la note selon son ID et l'ID de l'utilisateur
			note = noteMgr.getNote(noteId, userId);

			// Ajoute la note dans la view
			view.addAttributeToList(new ViewAttribut("note", note));

			// La note existe
			valid = true;
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.note.not.exist", lang)));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}
		return valid;
	}

	/**
	 * Récupère la date et heure actuelle et les charge dans la view
	 * 
	 * @param view
	 */
	private void addFunction(ViewJSP view) {
		// Récupère la date/heure actuelle en string
		LocalDateTime now = LocalDateTime.now();
		String today = GlobalTools.formatDateToString(now);

		// Charge la date / heure dans la view
		view.addAttributeToList(new ViewAttribut("today", today));
	}

	/**
	 * Récupère la note selon son ID et essaye de la supprimer de la base de donnée, charge le message succes/erreur
	 * 
	 * @param request
	 * @param noteId
	 * @param userId
	 * @param lang
	 */
	private void deleteFunction(HttpServletRequest request, int noteId, int userId, String lang) {
		Note note = null;
		try {
			// Récupère la note selon son ID et l'ID de l'utilisateur
			note = noteMgr.getNote(noteId, userId);

			// Supprime la note
			noteMgr.deleteNote(note);

			// Ajoute le message de succès dans la view
			request.getSession().setAttribute("success", Messages.getProperty("success.note.delete", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof NotExistException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.note.not.exist", lang));
			} else if (fex instanceof CantDeleteException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.note.cant.delete", lang));
			}
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Redirige la requête vers la JSP New
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNoteNewJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Ajout d'une note JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notes/new.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Details
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param noteId
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNoteDetailsJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int noteId)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Détails de la note {" + noteId + "} JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notes/details.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Récupère la liste des notes de l'utilisateur et renvoi vers la JSP notes avec message d'erreur si besoin
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
	private void redirectToNotesList(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, String orderBy, String dir,
			String lang) throws ServletException, IOException {
		List<Note> listNote = null;
		try {
			// Récupère la liste des notes de l'utilisateur
			listNote = noteMgr.getNotes(userId);

			// Tri la liste en fonction des paramètres
			listNote = noteMgr.orderBy(listNote, orderBy, dir);

		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}
		// Ajoute la liste dans la view
		view.addAttributeToList(new ViewAttribut("listNote", listNote));
		// Redirection
		redirectToNoteListJSP(request, response, view);
	}

	/**
	 * Redirige la requête vers la JSP List
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNoteListJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Liste des notes JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notes/list.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet en Get après un Post ou un Delete
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
