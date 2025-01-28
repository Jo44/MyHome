package fr.my.home.bean.api;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Bean ObjectReCaptcha
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class ObjectReCaptcha implements Serializable {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private boolean success;
	@SuppressWarnings("unused")
	private Timestamp challengeTs;
	@SuppressWarnings("unused")
	private String hostname;

	/**
	 * Constructeur
	 * 
	 * @param success
	 * @param challengeTs
	 * @param hostname
	 */
	public ObjectReCaptcha(boolean success, Timestamp challengeTs, String hostname) {
		this.success = success;
		this.challengeTs = challengeTs;
		this.hostname = hostname;
	}

	/**
	 * Getters
	 */

	public boolean isSuccess() {
		return success;
	}

}
