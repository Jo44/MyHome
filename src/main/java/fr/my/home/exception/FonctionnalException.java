package fr.my.home.exception;

/**
 * Classe modèle d'une exception de type fonctionnel
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class FonctionnalException extends Exception {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String message;

	// Constructor

	/**
	 * Default Constructor
	 */
	public FonctionnalException(String message) {
		this.message = message;
	}

	// Getters/Setters

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
