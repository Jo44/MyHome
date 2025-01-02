package fr.my.home.exception.users;

import fr.my.home.exception.FonctionnalException;

/**
 * Classe modèle d'une exception fonctionnelle de type : User - Identifiants incorrects
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class IDsException extends FonctionnalException {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String message;

	// Constructor

	/**
	 * Default Constructor
	 */
	public IDsException(String message) {
		super(message);
		this.message = message;
	}

	// Getters/Setters

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

}