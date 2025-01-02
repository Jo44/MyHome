package fr.my.home.servlet.users;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import fr.my.home.bean.User;
import fr.my.home.exception.FonctionnalException;

/**
 * Servlet qui prends en charge les requêtes AJAX pour charger en session le nombre de lignes max à afficher par défaut, et renvoi la réponse JSON
 * Valeurs acceptées : 10 / 25 / 50 / -1 (all)
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@WebServlet("/set_max_rows")
public class MaxRowsServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(MaxRowsServlet.class);

	// Constructors

	/**
	 * Default Constructor
	 */
	public MaxRowsServlet() {
		super();
	}

	// Methods

	/**
	 * Redirection
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirection
		doPost(request, response);
	}

	/**
	 * Traitement du nouveau maxRows à stocker en session
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Session Max Rows Servlet [POST] {AJAX} -->");
		int newMaxRows = 0;
		JsonObject jsonResult = new JsonObject();
		try {
			// Récupère l'utilisateur connecté
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				throw new FonctionnalException("Utilisateur incorrect");
			}

			// Récupère le paramètre de la requête
			String newMaxRowsStr = request.getParameter("newMaxRows");
			newMaxRows = Integer.parseInt(newMaxRowsStr);

			// Test si la valeur est acceptée
			if (newMaxRows != 10 && newMaxRows != 25 && newMaxRows != 50 && newMaxRows != -1) {
				throw new FonctionnalException("Valeur maxRows incorrect");
			}

			// Met à jour la variable en session
			request.getSession().setAttribute("maxRows", newMaxRows);

			// Mise à jour avec succès
			logger.info("Mise à jour du nombre max de lignes par tableau réussie -> maxRows: " + String.valueOf(newMaxRows));
			jsonResult.addProperty("state", "success");
			jsonResult.addProperty("result", newMaxRows);
		} catch (FonctionnalException | NullPointerException | NumberFormatException ex) {
			// Mise à jour échouée car erreur de paramètres
			logger.error("Erreur Parameters");
			jsonResult.addProperty("state", "error");
			jsonResult.addProperty("result", "parameters");
		} finally {
			// Renvoi la réponse
			PrintWriter out = response.getWriter();
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(jsonResult);
			out.flush();
		}
	}

}