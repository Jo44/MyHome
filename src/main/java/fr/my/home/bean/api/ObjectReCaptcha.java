package fr.my.home.bean.api;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Bean ObjectReCaptcha regroupant toutes les informations d'une requête à https://www.google.com/recaptcha/api/siteverify permettant la vérification
 * du système reCatpcha V2
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class ObjectReCaptcha implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private boolean success;
	private Timestamp challengeTs;
	private String hostname;

	// Constructors

	/**
	 * Default Constructor
	 */
	public ObjectReCaptcha() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param success
	 * @param challengeTs
	 * @param hostname
	 */
	public ObjectReCaptcha(boolean success, Timestamp challengeTs, String hostname) {
		this();
		this.success = success;
		this.challengeTs = challengeTs;
		this.hostname = hostname;
	}

	// Methods

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ Success: ");
		sb.append(String.valueOf(success));
		sb.append(" , Challenge_ts: ");
		sb.append(String.valueOf(challengeTs));
		sb.append(" , Hostname: ");
		if (hostname != null) {
			sb.append(hostname);
		} else {
			sb.append("null");
		}
		sb.append(" }");
		return sb.toString();
	}

	// Getters/Setters

	public boolean isSuccess() {
		return success;
	}

}
