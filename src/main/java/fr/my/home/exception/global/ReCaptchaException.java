package fr.my.home.exception.global;

import fr.my.home.exception.FonctionnalException;

/**
 * Classe modèle d'une exception fonctionnelle de type : Global - ReCaptcha incorrect
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class ReCaptchaException extends FonctionnalException {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private String message;

	/**
	 * Constructeur
	 */
	public ReCaptchaException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * Getters/Setters
	 */

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

}
