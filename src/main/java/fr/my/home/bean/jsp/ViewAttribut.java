package fr.my.home.bean.jsp;

import java.io.Serializable;

/**
 * Bean ViewAttribut permettant de stocker les attributs d'une view de la JSP
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public class ViewAttribut implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private String key;
	private Object value;

	// Constructors

	/**
	 * Default Constructor
	 */
	public ViewAttribut() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param key
	 * @param value
	 */
	public ViewAttribut(String key, Object value) {
		this();
		this.key = key;
		this.value = value;
	}

	// Methods

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ Key: ");
		if (key != null) {
			sb.append(key.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Value: ");
		if (value != null) {
			sb.append(value.toString());
		} else {
			sb.append("null");
		}
		sb.append(" }");
		return sb.toString();
	}

	// Getters/Setters

	protected String getKey() {
		return key;
	}

	protected Object getValue() {
		return value;
	}

}
