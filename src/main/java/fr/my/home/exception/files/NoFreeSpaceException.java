package fr.my.home.exception.files;

import fr.my.home.exception.FonctionnalException;

/**
 * Classe modèle d'une exception fonctionnelle de type : File - Espace de stockage insuffisant
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class NoFreeSpaceException extends FonctionnalException {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String message;

	// Constructor

	/**
	 * Default Constructor
	 */
	public NoFreeSpaceException(String message) {
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