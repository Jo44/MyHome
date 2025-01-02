package fr.my.home.bean.api;

import java.io.Serializable;

/**
 * Bean ObjectIPAPI regroupant toutes les informations d'une requête à ip-api.com permettant la géolocalisation d'une IP ou nom de domaine
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class ObjectIPAPI implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String status;
	private String org;
	private String country;
	private String query;
	private float lat;
	private float lon;

	// Constructors

	/**
	 * Default Constructor
	 */
	public ObjectIPAPI() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param status
	 * @param org
	 * @param country
	 * @param query
	 * @param lat
	 * @param lon
	 */
	public ObjectIPAPI(String status, String org, String country, String query, float lat, float lon) {
		this();
		this.status = status;
		this.org = org;
		this.country = country;
		this.query = query;
		this.lat = lat;
		this.lon = lon;
	}

	// Methods

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ Status: ");
		if (status != null) {
			sb.append(status);
		} else {
			sb.append("null");
		}
		sb.append(" , Org: ");
		if (org != null) {
			sb.append(org);
		} else {
			sb.append("null");
		}
		sb.append(" , Country: ");
		if (country != null) {
			sb.append(country);
		} else {
			sb.append("null");
		}
		sb.append(" , Query: ");
		if (query != null) {
			sb.append(query);
		} else {
			sb.append("null");
		}
		sb.append(" , Lat: ");
		sb.append(String.valueOf(lat));
		sb.append(" , Lon: ");
		sb.append(String.valueOf(lon));
		sb.append(" }");
		return sb.toString();
	}

	// Getters/Setters

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
