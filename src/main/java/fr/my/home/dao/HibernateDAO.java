package fr.my.home.dao;

import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Interface HibernateDAO qui déclare les 3 méthodes génériques CREATE / UPDATE / DELETE
 * 
 * @author Jonathan
 * @version 1.0
 * @since 15/07/2021
 */
public interface HibernateDAO<T> {

	// Methods

	/**
	 * Ajoute un nouvel objet en base et renvoi son ID
	 * 
	 * @param object
	 * @return int
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public int add(T object) throws FonctionnalException, TechnicalException;

	/**
	 * Met à jour un objet
	 * 
	 * @param object
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void update(T object) throws FonctionnalException, TechnicalException;

	/**
	 * Supprime un objet
	 * 
	 * @param object
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void delete(T object) throws FonctionnalException, TechnicalException;

}
