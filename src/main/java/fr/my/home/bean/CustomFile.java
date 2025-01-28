package fr.my.home.bean;

import java.sql.Timestamp;

import fr.my.home.tool.GlobalTools;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Bean CustomFile
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@Entity
@Table(name = "files")
public class CustomFile {

	/**
	 * Attributs
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "id_user", nullable = false)
	private int id_user;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "weight", nullable = false)
	private long weight;
	@Column(name = "upload_date", nullable = false)
	private Timestamp upload_date;

	/**
	 * Constructeur
	 */
	public CustomFile() {}

	/**
	 * Constructeur
	 * 
	 * @param id_user
	 * @param name
	 * @param weight
	 * @param upload_date
	 */
	public CustomFile(int id_user, String name, long weight, Timestamp upload_date) {
		this.id_user = id_user;
		this.name = name;
		this.weight = weight;
		this.upload_date = upload_date;
	}

	/**
	 * Getters/Setters
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUser() {
		return id_user;
	}

	public void setIdUser(int id_user) {
		this.id_user = id_user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getWeight() {
		return weight;
	}

	public String getFormattedWeight() {
		return GlobalTools.formatWeightToString(weight);
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	public Timestamp getUploadDate() {
		return upload_date;
	}

	public void setUploadDate(Timestamp upload_date) {
		this.upload_date = upload_date;
	}

}
