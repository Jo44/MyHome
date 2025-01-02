package fr.my.home.exception.youtube.videos;

import fr.my.home.exception.FonctionnalException;

/**
 * Classe modèle d'une exception fonctionnelle de type : YouTube Vidéo - Artiste incorrect
 *
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
public class ArtistException extends FonctionnalException {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String message;

	// Constructor

	/**
	 * Default Constructor
	 */
	public ArtistException(String message) {
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
