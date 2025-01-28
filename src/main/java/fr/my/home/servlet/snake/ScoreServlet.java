package fr.my.home.servlet.snake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.my.home.bean.SnakeScore;
import fr.my.home.bean.User;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.SnakeManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui prends en charge les requêtes AJAX pour enregistrer / récupérer les scores de Snake
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@WebServlet("/snake_score")
public class ScoreServlet extends HttpServlet {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ScoreServlet.class);
	private SnakeManager scoreMgr;

	/**
	 * Constructeur
	 */
	public ScoreServlet() {
		super();
		// Initialisation du manager
		scoreMgr = new SnakeManager();
	}

	/**
	 * Retourne les scores PB et WR en JSON
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Score Servlet [GET] {AJAX} -->");
		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();
		try {
			// Récupère le meilleur score de l'utilisateur
			SnakeScore scorePB = scoreMgr.getScorePB(userId);
			// Récupère la liste des 3 meilleurs scores
			List<SnakeScore> scoresWR = scoreMgr.getScoresWR();

			// Création du JSON des scores
			String json = scoreMgr.generateJSON(scorePB, scoresWR);

			// Répondre avec du JSON
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);

			logger.info("Récupération des scores réussie");
		} catch (TechnicalException ex) {
			logger.error("Erreur lors de la récupération des scores");
			logger.error("-> " + ex.getMessage());
		}
	}

	/**
	 * Enregistre le score pour l'utilisateur
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Score Servlet [POST] {AJAX} -->");
		JsonObject jsonResult = new JsonObject();
		try {
			// Lire le corps de la requête JSON
			BufferedReader reader = request.getReader();
			StringBuilder jsonString = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}

			// Parse le JSON reçu
			JsonObject jsonRequest = JsonParser.parseString(jsonString.toString()).getAsJsonObject();

			// Récupère l'utilisateur connecté
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				throw new FonctionnalException("Utilisateur incorrect");
			}

			// Récupère le score dans le JSON
			int newScore = jsonRequest.get("score").getAsInt();

			// Test si la valeur est acceptée
			if (newScore < 0 || newScore > 9999) {
				throw new FonctionnalException("Valeur score incorrect");
			}

			// Enregistrement du score
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			SnakeScore snakeScore = new SnakeScore(user.getId(), newScore, now);
			scoreMgr.addScore(snakeScore);

			// Enregistrement réussi
			logger.info("Enregistrement du score réussi");
			logger.info("-> User : " + user.getName());
			logger.info("-> Score : " + String.valueOf(newScore));
			jsonResult.addProperty("state", "success");
			jsonResult.addProperty("result", newScore);
		} catch (FonctionnalException | TechnicalException | NullPointerException | NumberFormatException ex) {
			// Enregistrement échoué
			logger.error("Erreur lors de l'enregistrement du score");
			logger.error("-> " + ex.getMessage());
			jsonResult.addProperty("state", "error");
			jsonResult.addProperty("result", ex.getMessage());
		} finally {
			// Renvoi la réponse
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(jsonResult);
			out.flush();
		}
	}

}
