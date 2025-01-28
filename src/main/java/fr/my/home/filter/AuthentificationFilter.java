package fr.my.home.filter;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;
import fr.my.home.manager.UsersManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtre qui permet de vérifier si un utilisateur est bien authentifié pour accèder à la ressource demandée
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class AuthentificationFilter implements Filter {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(AuthentificationFilter.class);

	/**
	 * Initialisation
	 */
	@Override
	public void init(FilterConfig fc) throws ServletException {}

	/**
	 * Filter
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsreq = (HttpServletRequest) request;
		HttpServletResponse hsres = (HttpServletResponse) response;
		HttpSession session = hsreq.getSession(true);
		// Récupère l'url d'accès des servlets ne nécessitant pas de connexion utilisateur
		// Servlet de login
		String loginURI = hsreq.getContextPath() + "/check";
		// Servlet de status (brut)
		String statusURI = hsreq.getContextPath() + "/status";
		// Servlet d'inscription d'un compte
		String registerURI = hsreq.getContextPath() + "/register";
		// Servlet de validation d'un compte
		String validationURI = hsreq.getContextPath() + "/validation";
		// Servlet de récupération d'un compte
		String recoveryURI = hsreq.getContextPath() + "/recovery";
		// Servlet de modification d'un mot de passe
		String reinitURI = hsreq.getContextPath() + "/reinit";
		// Récupère l'url demandée actuellement
		String requestURI = hsreq.getRequestURI();
		User user = null;
		boolean loggedIn = false;
		boolean loginRequest = requestURI.equals(loginURI);
		boolean statusRequest = requestURI.equals(statusURI);
		boolean registerRequest = requestURI.equals(registerURI);
		boolean validationRequest = requestURI.equals(validationURI);
		boolean recoveryRequest = requestURI.equals(recoveryURI);
		boolean reinitRequest = requestURI.equals(reinitURI);
		// Détermine si l'utilisateur est présent en session
		if (session != null) {
			user = (User) session.getAttribute("user");
			if (user != null) {
				// Utilisateur déjà en session
				loggedIn = true;
			} else {
				// Tente de récupérer l'utilisateur grâce au cookie
				UsersManager usersMgr = new UsersManager();
				user = usersMgr.getUserByCookie(hsreq, hsres);
				if (user != null) {
					// Si un cookie de connexion valide est présent
					session.setAttribute("user", user);
					loggedIn = true;
				}
			}
		}
		// Laisse passer la requête si utilisateur déjà connecté, si url autorisée ou toutes ressources css/js/png/ico/fonts
		if (loggedIn || loginRequest || statusRequest || registerRequest || validationRequest || recoveryRequest || reinitRequest
				|| requestURI.endsWith(".css") || requestURI.endsWith(".js") || requestURI.endsWith(".png") || requestURI.endsWith(".ico")
				|| requestURI.endsWith(".svg") || requestURI.endsWith(".eot") || requestURI.endsWith(".ttf") || requestURI.endsWith(".woff")
				|| requestURI.endsWith(".woff2")) {
			// Laisse passer la requête
			chain.doFilter(request, response);
		} else {
			logger.debug("Utilisateur non connecté");
			// Redirige vers la page de login
			hsres.sendRedirect(loginURI);
		}
	}

	/**
	 * Destroy
	 */
	@Override
	public void destroy() {}

}
