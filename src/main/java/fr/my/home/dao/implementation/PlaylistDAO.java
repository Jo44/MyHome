package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.ActivePlaylist;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.HibernateUtil;
import fr.my.home.tool.properties.Settings;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

/**
 * Classe PlaylistDAO
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class PlaylistDAO implements HibernateDAO<ActivePlaylist> {

	/**
	 * Attributs
	 */

	private static final String YT_PLAYLIST_DAO_GET_ALL = Settings.getStringProperty("playlist.get.all");
	private static final String YT_PLAYLIST_DAO_GET_ALL_ACTIVE = Settings.getStringProperty("playlist.get.all.active");
	private static final String YT_PLAYLIST_DAO_GET_ONE = Settings.getStringProperty("playlist.get.one");

	/**
	 * Constructeur
	 */
	public PlaylistDAO() {}

	/**
	 * Récupère la liste de toutes les playlists de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws TechnicalException
	 */
	public List<ActivePlaylist> getAllPlaylists(int userId) throws TechnicalException {
		List<ActivePlaylist> listPlaylist = new ArrayList<ActivePlaylist>();
		Session session = HibernateUtil.getInstance().openSession();
		Query<ActivePlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ALL, ActivePlaylist.class);
		query.setParameter("id_user", userId);
		listPlaylist = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
		return listPlaylist;
	}

	/**
	 * Récupère la liste de toutes les playlists actives de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws TechnicalException
	 */
	public List<ActivePlaylist> getAllActivePlaylists(int userId) throws TechnicalException {
		List<ActivePlaylist> listPlaylist = new ArrayList<ActivePlaylist>();
		Session session = HibernateUtil.getInstance().openSession();
		Query<ActivePlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ALL_ACTIVE, ActivePlaylist.class);
		query.setParameter("id_user", userId);
		listPlaylist = query.getResultList();
		HibernateUtil.getInstance().validateSession(session);
		return listPlaylist;
	}

	/**
	 * Récupère une playlist selon l'ID de l'utilisateur et l'ID YouTube
	 * 
	 * @param userId
	 * @param youtubeId
	 * @return YouTubePlaylist
	 * @throws TechnicalException
	 */
	public ActivePlaylist getOnePlaylist(int userId, String youtubeId) throws TechnicalException {
		ActivePlaylist playlist = null;
		Session session = HibernateUtil.getInstance().openSession();
		Query<ActivePlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ONE, ActivePlaylist.class);
		query.setParameter("id_user", userId);
		query.setParameter("id_youtube", youtubeId);
		query.setMaxResults(1);
		try {
			playlist = query.getSingleResult();
		} catch (NoResultException nre) {
			playlist = null;
		} finally {
			HibernateUtil.getInstance().validateSession(session);
		}
		return playlist;
	}

	/**
	 * Ajoute une nouvelle playlist, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(ActivePlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.persist(playlist);
			session.flush();
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter la playlist");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Met à jour une playlist, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(ActivePlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.merge(playlist);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour la playlist");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

	/**
	 * Supprime une playlist, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(ActivePlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = HibernateUtil.getInstance().openSession();
		try {
			session.remove(playlist);
			HibernateUtil.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer la playlist");
		} finally {
			if (session != null && session.isOpen()) {
				HibernateUtil.getInstance().closeSession(session);
			}
		}
	}

}
