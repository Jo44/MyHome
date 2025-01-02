package fr.my.home.servlet.contacts;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Contact;
import fr.my.home.bean.User;
import fr.my.home.bean.jsp.ViewAttribut;
import fr.my.home.bean.jsp.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.contacts.CantDeleteException;
import fr.my.home.exception.contacts.NotExistException;
import fr.my.home.manager.ContactsManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.properties.Messages;

/**
 * Servlet qui prends en charge la gestion des contacts
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/contacts")
public class ContactsServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ContactsServlet.class);

	// Attributes

	private ContactsManager contactMgr;

	// Constructors

	/**
	 * Default Constructor
	 */
	public ContactsServlet() {
		super();
		// Initialisation du manager
		contactMgr = new ContactsManager();
	}

	// Methods

	/**
	 * Redirection vers l'ajout, la liste, le détail et la suppression des contacts
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Contact Servlet [GET] -->");

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
		int contactId;
		try {
			contactId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			contactId = 0;
		}
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					// Redirection vers la page d'ajout d'un contact
					redirectToContactNewJSP(request, response, view);
					break;
				// Si action details
				case "details":
					// Récupère le contact
					boolean exist = getFunction(view, contactId, userId, lang);
					if (exist) {
						// Si le contact existe pour l'utilisateur, redirection vers la page de détails
						redirectToContactDetailsJSP(request, response, view, contactId);
					} else {
						// Si le contact n'existe pas, renvoi à la liste des contacts
						redirectToContactList(request, response, view, userId, null, null, lang);
					}
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer le contact
					deleteFunction(request, contactId, userId, lang);

					// Renvoi à la liste des contacts
					redirectToThisServletAfterPostOrDelete(request, response);
					break;
				// Si action lister
				case "list":
					// Renvoi à la liste des contacts
					redirectToContactList(request, response, view, userId, orderBy, dir, lang);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des contacts
					redirectToContactList(request, response, view, userId, null, null, lang);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des contacts
			redirectToContactList(request, response, view, userId, null, null, lang);
		}
	}

	/**
	 * Traitement du formulaire d'ajout / de modification d'un contact
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Contact Servlet [POST] -->");

		// Récupère l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la langue de l'utilisateur (pour messages success/error)
		String lang = GlobalTools.validLanguage((String) request.getSession().getAttribute("lang"));

		// Récupère les informations saisies dans le formulaire
		String action = request.getParameter("action");
		String firstname = new String(request.getParameter("firstname").trim().getBytes("ISO-8859-1"), "UTF-8");
		String lastname = new String(request.getParameter("lastname").trim().getBytes("ISO-8859-1"), "UTF-8");
		String email = new String(request.getParameter("email").trim().getBytes("ISO-8859-1"), "UTF-8");
		String phone = new String(request.getParameter("intlPhoneNumber").trim().getBytes("ISO-8859-1"), "UTF-8");
		String twitter = new String(request.getParameter("twitter").trim().getBytes("ISO-8859-1"), "UTF-8");
		String idContact = request.getParameter("idContact");
		int contactId;
		try {
			contactId = Integer.parseInt(idContact);
		} catch (NumberFormatException e) {
			contactId = 0;
		}

		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					logger.info("Tentative d'ajout d'un contact en cours ..");

					// Essaye d'ajouter le contact
					addFunction(request, user.getId(), firstname, lastname, email, phone, twitter, lang);

					// Redirige vers la page de liste des contacts
					redirectToThisServletAfterPostOrDelete(request, response);
					break;
				// Si action modifier
				case "update":
					logger.info("Tentative de modification d'un contact en cours ..");

					// Essaye de modifier le contact
					updateFunction(request, contactId, user.getId(), firstname, lastname, email, phone, twitter, lang);

					// Redirige vers la page de liste des contacts
					redirectToThisServletAfterPostOrDelete(request, response);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Récupère le contact selon son ID et la charge ainsi que l'erreur dans la view si besoin et charge la view dans la requête
	 * 
	 * @param view
	 * @param contactId
	 * @param userId
	 * @param lang
	 * @return boolean
	 */
	private boolean getFunction(ViewJSP view, int contactId, int userId, String lang) {
		boolean valid = false;
		Contact contact = null;
		try {
			// Récupère le contact selon son ID et l'ID de l'utilisateur
			contact = contactMgr.getContact(contactId, userId);

			// Ajoute le contact dans la view
			view.addAttributeToList(new ViewAttribut("contact", contact));

			// Le contact existe
			valid = true;
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.contact.not.exist", lang)));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}
		return valid;
	}

	/**
	 * Ajoute le contact à l'utilisateur, charge le message succes/erreur
	 * 
	 * @param request
	 * @param userId
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param phone
	 * @param twitter
	 * @param lang
	 */
	private void addFunction(HttpServletRequest request, int userId, String firstname, String lastname, String email, String phone, String twitter,
			String lang) {
		try {
			// Ajoute le contact
			contactMgr.addContact(userId, firstname, lastname, email, phone, twitter);

			// Ajoute le message d'ajout avec succès
			request.getSession().setAttribute("success", Messages.getProperty("success.contact.add", lang));
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.contact.firstname", lang));
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Met à jour le contact de l'utilisateur, charge le message succes/erreur
	 * 
	 * @param request
	 * @param contactId
	 * @param userId
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param phone
	 * @param twitter
	 * @param lang
	 */
	private void updateFunction(HttpServletRequest request, int contactId, int userId, String firstname, String lastname, String email, String phone,
			String twitter, String lang) {
		try {
			// Met à jour le contact
			contactMgr.updateContact(contactId, userId, firstname, lastname, email, phone, twitter);

			// Ajoute le message d'ajout avec succès
			request.getSession().setAttribute("success", Messages.getProperty("success.contact.update", lang));
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.contact.firstname", lang));
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", Messages.getProperty("error.database", lang));
		}
	}

	/**
	 * Récupère le contact selon son ID et essaye de le supprimer de la base de donnée, charge le message succes/erreur
	 * 
	 * @param request
	 * @param contactId
	 * @param userId
	 * @param lang
	 */
	private void deleteFunction(HttpServletRequest request, int contactId, int userId, String lang) {
		Contact contact = null;
		try {
			// Récupère le contact selon son ID et l'ID de l'utilisateur
			contact = contactMgr.getContact(contactId, userId);

			// Supprime le contact
			contactMgr.deleteContact(contact);

			// Ajoute le message de succès dans la view
			request.getSession().setAttribute("success", Messages.getProperty("success.contact.delete", lang));
		} catch (FonctionnalException fex) {
			if (fex instanceof NotExistException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.contact.not.exist", lang));
			} else if (fex instanceof CantDeleteException) {
				request.getSession().setAttribute("error", Messages.getProperty("error.contact.cant.delete", lang));
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
	private void redirectToContactNewJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Ajout d'un contact JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/contacts/new.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Details
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param contactId
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToContactDetailsJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int contactId)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Détails du contact {" + contactId + "} JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/contacts/details.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Récupère la liste des contacts de l'utilisateur et renvoi vers la JSP Contact avec message d'erreur si besoin
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
	private void redirectToContactList(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, String orderBy, String dir,
			String lang) throws ServletException, IOException {
		List<Contact> listContact = null;
		try {
			// Récupère la liste des contacts de l'utilisateur
			listContact = contactMgr.getContacts(userId);

			// Tri la liste en fonction des paramètres
			listContact = contactMgr.orderBy(listContact, orderBy, dir);

		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", Messages.getProperty("error.database", lang)));
		}
		// Ajoute la liste dans la view
		view.addAttributeToList(new ViewAttribut("listContact", listContact));
		// Redirection
		redirectToContactListJSP(request, response, view);
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
	private void redirectToContactListJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Liste des contacts JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/contacts/list.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet en Get après un Post ou un delete
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
