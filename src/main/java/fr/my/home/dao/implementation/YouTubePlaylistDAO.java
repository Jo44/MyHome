package fr.my.home.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.YouTubeActivPlaylist;
import fr.my.home.dao.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.properties.Settings;

/**
 * Classe YouTubePlaylistDAO qui gère le stockage des playlists YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/08/2021
 */
public class YouTubePlaylistDAO implements HibernateDAO<YouTubeActivPlaylist> {

	// Attributes

	private static final String YT_PLAYLIST_DAO_GET_ALL = Settings.getStringProperty("yt.playlist.get.all");
	private static final String YT_PLAYLIST_DAO_GET_ALL_ACTIVE = Settings.getStringProperty("yt.playlist.get.all.active");
	private static final String YT_PLAYLIST_DAO_GET_ONE = Settings.getStringProperty("yt.playlist.get.one");

	// Constructor

	/**
	 * Default Constructor
	 */
	public YouTubePlaylistDAO() {}

	// Methods

	/**
	 * Récupère la liste de toutes les playlists de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws TechnicalException
	 */
	public List<YouTubeActivPlaylist> getAllPlaylists(int userId) throws TechnicalException {
		List<YouTubeActivPlaylist> listPlaylist = new ArrayList<YouTubeActivPlaylist>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubeActivPlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ALL);
		query.setParameter("ytpl_id_user", userId);
		listPlaylist = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
		return listPlaylist;
	}

	/**
	 * Récupère la liste de toutes les playlists actives de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws TechnicalException
	 */
	public List<YouTubeActivPlaylist> getAllActivePlaylists(int userId) throws TechnicalException {
		List<YouTubeActivPlaylist> listPlaylist = new ArrayList<YouTubeActivPlaylist>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubeActivPlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ALL_ACTIVE);
		query.setParameter("ytpl_id_user", userId);
		listPlaylist = query.getResultList();
		DatabaseAccess.getInstance().validateSession(session);
		return listPlaylist;
	}

	/**
	 * Récupère une playlist selon l'ID de l'utilisateur et l'ID YouTube
	 * 
	 * @param playlistId
	 * @param userId
	 * @param youtubeId
	 * @return YouTubePlaylist
	 * @throws TechnicalException
	 */
	public YouTubeActivPlaylist getOnePlaylist(int userId, String youtubeId) throws TechnicalException {
		YouTubeActivPlaylist playlist = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubeActivPlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ONE);
		query.setParameter("ytpl_id_user", userId);
		query.setParameter("ytpl_id_yt", youtubeId);
		try {
			playlist = query.getSingleResult();
		} catch (NoResultException nre) {
			playlist = null;
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return playlist;
	}

	/**
	 * Ajoute une nouvelle playlist en base et renvoi son ID, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @param FonctionnalException
	 * @throws TechnicalException
	 * @return int
	 */
	@Override
	public int add(YouTubeActivPlaylist playlist) throws FonctionnalException, TechnicalException {
		int id = 0;
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			id = (int) session.save(playlist);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter la playlist");
		}
		return id;
	}

	/**
	 * Met à jour une playlist, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(YouTubeActivPlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(playlist);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour la playlist");
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
	public void delete(YouTubeActivPlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(playlist);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer la playlist");
		}
	}

}
