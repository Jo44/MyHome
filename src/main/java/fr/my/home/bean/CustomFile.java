package fr.my.home.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Bean CustomFile que peut uploader/downloader un utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
@Entity
@Table(name = "file")
public class CustomFile implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	// Attributes

	private int id;
	private int idUser;
	private String name;
	private long weight;
	private Timestamp uploadDate;

	// Constructors

	/**
	 * Default Constructor
	 */
	public CustomFile() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param idUser
	 * @param name
	 * @param weight
	 * @param uploadDate
	 */
	public CustomFile(int idUser, String name, long weight, Timestamp uploadDate) {
		this();
		this.idUser = idUser;
		this.name = name;
		this.weight = weight;
		this.uploadDate = uploadDate;
	}

	// Methods

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ID: ");
		sb.append(String.valueOf(id));
		sb.append(" , User: ");
		sb.append(String.valueOf(idUser));
		sb.append(" , Name: ");
		if (name != null) {
			sb.append(name);
		} else {
			sb.append("null");
		}
		sb.append(" , Weight: ");
		sb.append(String.valueOf(weight));
		sb.append(" , Upload Date: ");
		if (uploadDate != null) {
			sb.append(uploadDate.toString());
		} else {
			sb.append("null");
		}
		sb.append(" }");
		return sb.toString();
	}

	// Getters/Setters (setters en privé car seulement utilisé par Hibernate)

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "file_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "file_id_user")
	public int getIdUser() {
		return idUser;
	}

	@SuppressWarnings("unused")
	private void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "file_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "file_weight")
	public long getWeight() {
		return weight;
	}

	@SuppressWarnings("unused")
	private void setWeight(long weight) {
		this.weight = weight;
	}

	@Column(name = "file_upload_date")
	public Timestamp getUploadDate() {
		return uploadDate;
	}

	@SuppressWarnings("unused")
	private void setUploadDate(Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}

}
