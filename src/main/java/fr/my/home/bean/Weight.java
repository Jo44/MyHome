package fr.my.home.bean;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Bean Weight
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
@Entity
@Table(name = "weights")
public class Weight {

	/**
	 * Attributs
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "id_user", nullable = false)
	private int id_user;
	@Column(name = "value", nullable = false)
	private float value;
	@Column(name = "save_date", nullable = false)
	private Timestamp save_date;

	/**
	 * Constructeur
	 */
	public Weight() {}

	/**
	 * Constructeur
	 * 
	 * @param id_user
	 * @param value
	 * @param save_date
	 */
	public Weight(int id_user, float value, Timestamp save_date) {
		this.id_user = id_user;
		this.value = value;
		this.save_date = save_date;
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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Timestamp getSaveDate() {
		return save_date;
	}

	public String getSaveDateJS() {
		return save_date.toLocalDateTime().toString();
	}

	public void setSaveDate(Timestamp save_date) {
		this.save_date = save_date;
	}

}
