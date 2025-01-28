package fr.my.home.filter;

import java.io.IOException;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Filtre qui permet de définir le langage via un paramètre de requête ou via la localisation de la requête
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class LocalizationFilter implements Filter {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(LocalizationFilter.class);

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
		HttpSession session = hsreq.getSession(false);
		// Initialise la session si inexistante
		if (session == null) {
			session = hsreq.getSession(true);
		}
		// Vérifie l'état de la session
		if (session != null) {
			// Si la session est ouverte, vérifie si un paramètre 'lang' est présent dans la requête
			if (hsreq.getParameter("lang") != null) {
				// Si présent, vérifie si fr/en puis défini l'attribut de session 'lang'
				String paramLang = hsreq.getParameter("lang");
				if (!paramLang.equals("fr")) {
					paramLang = "en";
				}
				// Défini l'attribut de session 'lang'
				session.setAttribute("lang", paramLang);
				logger.debug("Langage défini en session via paramètre 'lang' de la requête.");
			} else {
				// Si non présent, vérifie si un attribut de session est déjà définie
				String sessionAttr = (String) session.getAttribute("lang");
				if (sessionAttr == null || sessionAttr.trim().isEmpty()) {
					// Si non présent, essaye de récupèrer le langage depuis la locale de la requête
					String lang = getRequestLanguage(request);
					// Défini l'attribut de session 'lang'
					session.setAttribute("lang", lang);
					logger.debug("Langage défini en session via la locale de la requête.");
				}
			}
		}
		// Laisse passer la requête
		chain.doFilter(request, response);
	}

	/**
	 * Récupère la langue à partir de la locale de la requête
	 * 
	 * @param request
	 * @return lang
	 */
	private String getRequestLanguage(ServletRequest request) {
		// Essaye de déterminer la langue en fonction de la requête, met en anglais par défaut
		String lang = "en";
		// Essaye de récupèrer le langage depuis la locale de la requête
		Locale reqLocale = request.getLocale();
		// Vérifie si la locale de la requête est présente, si son langage est également présent et si la langue est "fr"
		if (reqLocale != null && reqLocale.getLanguage() != null && reqLocale.getLanguage().equals("fr")) {
			lang = "fr";
		}
		return lang;
	}

	/**
	 * Destroy
	 */
	@Override
	public void destroy() {}

}
