package fr.my.home.exception.files;

import fr.my.home.exception.FonctionnalException;

/**
 * Classe modèle d'une exception fonctionnelle de type : File - Fichier trop volumineux
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class FileSizeException extends FonctionnalException {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String message;
	private String filename;

	// Constructor

	/**
	 * Default Constructor
	 */
	public FileSizeException(String message, String filename) {
		super(message);
		this.message = message;
		this.filename = filename;
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
