package fr.my.home.bean.api;

import java.io.Serializable;

/**
 * Bean ObjectIPAPI
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class ObjectIPAPI implements Serializable {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 930448801449184468L;
	private String status;
	private String org;
	private String country;
	private String query;
	private float lat;
	private float lon;

	/**
	 * Constructeur
	 *
	 * @param status
	 * @param org
	 * @param country
	 * @param query
	 * @param lat
	 * @param lon
	 */
	public ObjectIPAPI(String status, String org, String country, String query, float lat, float lon) {
		this.status = status;
		this.org = org;
		this.country = country;
		this.query = query;
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Getters
	 */

	public String getStatus() {
		return status;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public String getOrg() {
		return org;
	}

	public String getCountry() {
		return country;
	}

	public String getQuery() {
		return query;
	}

}
