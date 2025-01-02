package fr.my.home.exception.youtube.playlists;

import fr.my.home.exception.FonctionnalException;

/**
 * Classe modèle d'une exception fonctionnelle de type : YouTube Playlist - Impossible de supprimer une playlist active
 * 
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
public class CantDeleteActiveException extends FonctionnalException {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String message;

	// Constructor

	/**
	 * Default Constructor
	 */
	public CantDeleteActiveException(String message) {
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