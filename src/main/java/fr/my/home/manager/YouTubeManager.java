package fr.my.home.manager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import fr.my.home.bean.ActivePlaylist;
import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.dao.implementation.PlaylistDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.exception.youtube.playlists.CantDeleteActiveException;
import fr.my.home.exception.youtube.playlists.DescriptionException;
import fr.my.home.exception.youtube.playlists.TitleException;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.GoogleServices;
import jakarta.servlet.http.HttpSession;

/**
 * Manager qui prends en charge la gestion des playlists/vidéos YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 15/01/2025
 */
public class YouTubeManager {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(YouTubeManager.class);
	private YouTube youtube;
	private PlaylistDAO ytPlaylistDAO;

	/**
	 * Constructeur
	 */
	public YouTubeManager(HttpSession session) throws FonctionnalException, TechnicalException {
		// YouTube Service
		youtube = GoogleServices.getYouTubeService(session);
		// YouTube Active Playlist DAO
		ytPlaylistDAO = new PlaylistDAO();
	}

	/**
	 * Renvoi la liste des playlists actives de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws TechnicalException
	 */
	public List<ActivePlaylist> getActivePlaylists(int userId) throws TechnicalException {
		List<ActivePlaylist> listActivePlayslist = new ArrayList<ActivePlaylist>();
		try {
			// Récupère la liste des playlists actives
			listActivePlayslist = ytPlaylistDAO.getAllActivePlaylists(userId);
			logger.debug("Récupération des playlists actives");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listActivePlayslist;
	}

	/**
	 * Renvoi la liste des playlists de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws TechnicalException
	 */
	public List<ActivePlaylist> getPlaylists(int userId) throws TechnicalException {
		List<ActivePlaylist> listPlaylist = new ArrayList<ActivePlaylist>();
		try {
			// Récupère la liste des playlists
			listPlaylist = ytPlaylistDAO.getAllPlaylists(userId);
			logger.debug("Récupération des playlists enregistrés");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listPlaylist;
	}

	/**
	 * Récupère la playlist demandée
	 * 
	 * @param userId
	 * @param youtubeId
	 * @return YouTubeActivPlaylist
	 * @thorws TechnicalException
	 */
	public ActivePlaylist getPlaylist(int userId, String youtubeId) throws TechnicalException {
		ActivePlaylist playlist = null;
		try {
			// Récupère la playlist
			playlist = ytPlaylistDAO.getOnePlaylist(userId, youtubeId);
			logger.debug("Récupération de la playlist");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return playlist;
	}

	/**
	 * Vérifie si la playlist demandée appartient bien à l'utilisateur et renvoi boolean
	 * 
	 * @param idPlaylist
	 * @param userId
	 * @param youtubeId
	 * @return boolean
	 * @thorws TechnicalException
	 */
	public boolean checkPlaylist(int idPlaylist, int userId, String youtubeId) throws TechnicalException {
		boolean valid = false;
		try {
			// Récupère la playlist
			ActivePlaylist playlist = ytPlaylistDAO.getOnePlaylist(userId, youtubeId);
			logger.debug("Récupération de la playlist");
			if (playlist != null) {
				valid = true;
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return valid;
	}

	/**
	 * Ajoute la nouvelle playlist et renvoi son ID
	 * 
	 * @param userId
	 * @param youtubeId
	 * @param active
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 * @return String
	 */
	public String addPlaylist(int userId, String youtubeId, boolean active) throws FonctionnalException, TechnicalException {
		String id = "";
		try {
			// Ajoute la nouvelle playlist
			ActivePlaylist playlist = new ActivePlaylist(userId, youtubeId, active, Timestamp.valueOf(LocalDateTime.now()));
			ytPlaylistDAO.add(playlist);
			id = playlist.getIdYouTube();
			logger.debug("Ajout d'une nouvelle playlist réussi");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return id;
	}

	/**
	 * Modifie la playlist
	 * 
	 * @param playlist
	 * @param active
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void updatePlaylist(ActivePlaylist playlist, boolean active) throws FonctionnalException, TechnicalException {
		try {
			// Modifie la playlist
			playlist.setActive(active);
			ytPlaylistDAO.update(playlist);
			logger.debug("Playlist modifiée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Vérifie si les paramètres d'une playlist sont valides sinon renvoi exception fonctionnelle
	 * 
	 * @param title
	 * @param description
	 * @throws FonctionnalException
	 */
	public void checkParamsPlaylist(String title, String description) throws FonctionnalException {
		if (title == null || title.trim().length() < 1 || title.trim().length() > 150) {
			throw new TitleException("Le titre n'est pas valide");
		}
		if (description != null && description.trim().length() > 150) {
			throw new DescriptionException("La description n'est pas valide");
		}
	}

	/**
	 * Récupère la playlist désirée, vérifie si elle n'est pas 'active' puis supprime la playlist et toutes ses vidéos, ou exception si impossible
	 * 
	 * @param playlist
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deletePlaylist(ActivePlaylist playlist) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'une playlist en cours ..");
		try {
			// Vérifie si la playlist n'est pas celle qui est active, sinon renvoi exception fonctionnelle
			if (playlist.isActive()) {
				throw new FonctionnalException("Impossible de supprimer une playlist active");
			} else {
				// Supprime la playlist de la base de donnée
				ytPlaylistDAO.delete(playlist);
				logger.debug("Suppression de la playlist {" + playlist.getId() + "} effectuée");
			}
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw new CantDeleteActiveException("Impossible de supprimer une playlist active");
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Récupère le channel ID de l'utilisateur
	 * 
	 * @return String
	 */
	private String getChannelIDYTData() {
		String channelId = "";
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("id");
			String field = "items(id)";

			// Récupère le channel de l'utilisateur
			YouTube.Channels.List request = youtube.channels().list(parts);
			request.setMine(true);
			request.setFields(field);

			// Execute the request
			ChannelListResponse response = request.execute();

			if (response != null && response.getItems() != null && response.getItems().size() > 0) {
				channelId = response.getItems().get(0).getId();
			}
		} catch (GoogleJsonResponseException jsonex) {
			logger.error("YouTube Data - getChannelIDYTData - Impossible de parse la réponse : ", jsonex.getMessage());
		} catch (Throwable th) {
			th.printStackTrace();
			logger.error("YouTube Data - getChannelIDYTData - Impossible de récupérer le channel ID");
		}
		return channelId;
	}

	/**
	 * Récupère le nom de la chaine YouTube de l'utilisateur
	 * 
	 * @return String
	 */
	public String getChannelNameYTData() {
		String channelName = "";
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet");
			String field = "items(snippet/title)";

			// Récupère le channel de l'utilisateur
			YouTube.Channels.List request = youtube.channels().list(parts);
			request.setMine(true);
			request.setFields(field);

			// Execute the request
			ChannelListResponse response = request.execute();

			if (response != null && response.getItems() != null && response.getItems().size() > 0) {
				channelName = response.getItems().get(0).getSnippet().getTitle();
			}
		} catch (GoogleJsonResponseException jsonex) {
			logger.error("YouTube Data - getChannelNameYTData - Impossible de parse la réponse : ", jsonex.getMessage());
		} catch (Throwable th) {
			th.printStackTrace();
			logger.error("YouTube Data - getChannelNameYTData - Impossible de récupérer le channel name");
		}
		return channelName;
	}

	/**
	 * Récupère la liste des playlists de l'utilisateur
	 * 
	 * @param pageToken
	 * @return List<YouTubePlaylist>
	 */
	public List<YouTubePlaylist> getPlaylistsYTData(String pageToken) {
		List<YouTubePlaylist> listPlaylist = new ArrayList<YouTubePlaylist>();
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet,status");
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field",
					"pageInfo(totalResults),nextPageToken,prevPageToken,items(id,snippet(publishedAt,title,description,thumbnails(default(url))),status(privacyStatus))");
			parameters.put("maxResults", "30");
			parameters.put("channelId", getChannelIDYTData());
			parameters.put("pageToken", pageToken);

			if (parameters.get("channelId") != null || parameters.get("channelId") != "") {
				// Récupère les playlists de l'utilisateur OAuth
				YouTube.Playlists.List request = youtube.playlists().list(parts);
				request.setChannelId(parameters.get("channelId").toString());
				request.setFields(parameters.get("field").toString());
				request.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
				if (parameters.containsKey("pageToken") && parameters.get("pageToken") != null && !parameters.get("pageToken").isEmpty()) {
					request.setPageToken(pageToken);
				}
				// Execute the request
				PlaylistListResponse response = request.execute();
				// Récupère la pagination et la liste des playlists
				List<Playlist> listPlaylistYTData = response.getItems();
				int totalResults = response.getPageInfo().getTotalResults();
				String prevPageToken = response.getPrevPageToken();
				String nextPageToken = response.getNextPageToken();
				// Récupère la pagination et la liste des playlist items

				// Conversion en playlist
				if (listPlaylistYTData != null && listPlaylistYTData.size() > 0) {
					for (Playlist playlistYTData : listPlaylistYTData) {
						String id = playlistYTData.getId();
						String title = playlistYTData.getSnippet().getTitle();
						String description = playlistYTData.getSnippet().getDescription();
						String urlImage = playlistYTData.getSnippet().getThumbnails().getDefault().getUrl();
						String privacy = playlistYTData.getStatus().getPrivacyStatus();
						Timestamp publishedAt = new Timestamp(playlistYTData.getSnippet().getPublishedAt().getValue());
						// Ajoute la playlist à la liste
						YouTubePlaylist playlist = new YouTubePlaylist(id, GlobalTools.encodeLatin(title), GlobalTools.encodeLatin(description),
								urlImage, privacy, false, publishedAt, totalResults, prevPageToken, nextPageToken);
						listPlaylist.add(playlist);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de récupérer la liste des playlists");
		}
		return listPlaylist;
	}

	/**
	 * Récupère la playlist selon son ID
	 * 
	 * @param idPlaylist
	 * @return YouTubePlaylist
	 */
	public YouTubePlaylist getPlaylistYTData(String idPlaylist) {
		YouTubePlaylist playlist = null;
		// Récupère la playlist
		Playlist playlistYTData = getRawPlaylistYTData(idPlaylist);
		// Conversion en playlist
		if (playlistYTData != null) {
			String id = playlistYTData.getId();
			String title = playlistYTData.getSnippet().getTitle();
			String description = playlistYTData.getSnippet().getDescription();
			String urlImage = playlistYTData.getSnippet().getThumbnails().getDefault().getUrl();
			String privacy = playlistYTData.getStatus().getPrivacyStatus();
			Timestamp publishedAt = new Timestamp(playlistYTData.getSnippet().getPublishedAt().getValue());
			// Met à jour la playlist
			playlist = new YouTubePlaylist(id, title, description, urlImage, privacy, false, publishedAt, 1, null, null);
		}
		return playlist;
	}

	/**
	 * Récupère la playlist selon son ID (conserve l'objet YouTube Data pour modification)
	 * 
	 * @param idPlaylist
	 * @return Playlist
	 */
	public Playlist getRawPlaylistYTData(String idPlaylist) {
		Playlist playlistYTData = null;
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet,status");
			List<String> id = Collections.singletonList(idPlaylist);
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field", "items(id,snippet(publishedAt,title,description,thumbnails(default(url))),status(privacyStatus))");
			parameters.put("playlistId", idPlaylist);

			if (parameters.get("playlistId") != "") {
				// Récupère la playlist de l'utilisateur OAuth selon l'ID YouTube
				YouTube.Playlists.List request = youtube.playlists().list(parts);
				request.setId(id);
				request.setFields(parameters.get("field"));
				// Execute the request
				PlaylistListResponse response = request.execute();
				// Renvoi la playlist
				playlistYTData = response.getItems().get(0);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de récupérer la playlist");
		}
		return playlistYTData;
	}

	/**
	 * Met à jour l'attribut 'active' dans la liste de YouTubePlaylists à partir de la liste de YouTubeActivPlaylist
	 * 
	 * @param listPlaylistYTData
	 * @param listActivPlaylist
	 * @return List<YouTubePlaylist>
	 */
	public List<YouTubePlaylist> checkActive(List<YouTubePlaylist> listPlaylistYTData, List<ActivePlaylist> listActivPlaylist) {
		if (listPlaylistYTData != null && listActivPlaylist != null) {
			// Parcours la liste des playlists YouTube Data
			for (YouTubePlaylist playlistYTData : listPlaylistYTData) {
				// Parcours la liste des playlists en base
				for (ActivePlaylist activPlaylist : listActivPlaylist) {
					if (playlistYTData.getId().equals(activPlaylist.getIdYouTube())) {
						playlistYTData.setActive(activPlaylist.isActive());
					}
				}
			}
		}
		return listPlaylistYTData;
	}

	/**
	 * Récupère la liste des vidéos d'une playlist selon son ID et son token de page
	 * 
	 * @param idPlaylist
	 * @param pageToken
	 * @return List<YouTubeVideo>
	 */
	public List<YouTubeVideo> getVideosYTData(String idPlaylist, String pageToken) {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet,contentDetails");
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field",
					"pageInfo(totalResults),nextPageToken,prevPageToken,items(id,snippet(title,thumbnails(default(url))),contentDetails(videoId))");
			parameters.put("maxResults", "10");
			parameters.put("playlistId", idPlaylist);
			parameters.put("pageToken", pageToken);

			if (parameters.containsKey("playlistId") && parameters.get("playlistId") != "") {
				// Récupère la liste des vidéos de l'utilisateur OAuth selon l'ID YouTube
				YouTube.PlaylistItems.List request = youtube.playlistItems().list(parts);
				request.setPlaylistId(parameters.get("playlistId").toString());
				request.setFields(parameters.get("field").toString());
				request.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
				if (parameters.containsKey("pageToken") && parameters.get("pageToken") != null && !parameters.get("pageToken").isEmpty()) {
					request.setPageToken(pageToken);
				}
				// Execute the request
				PlaylistItemListResponse response = request.execute();
				// Récupère la pagination et la liste des playlist items
				int totalResults = response.getPageInfo().getTotalResults();
				String prevPageToken = response.getPrevPageToken();
				String nextPageToken = response.getNextPageToken();
				List<PlaylistItem> playlistItems = response.getItems();

				// Conversion en videos
				if (playlistItems != null && playlistItems.size() > 0) {
					for (PlaylistItem item : playlistItems) {
						String id = item.getContentDetails().getVideoId();
						String itemIdPlaylist = item.getId();
						String title = item.getSnippet().getTitle();
						String urlImage;
						if (item.getSnippet().getThumbnails().getDefault() != null) {
							urlImage = item.getSnippet().getThumbnails().getDefault().getUrl();
						} else {
							urlImage = "https://my-home.ovh/img/youtube/private.png";
						}
						// Ajoute la vidéo à la liste
						YouTubeVideo video = new YouTubeVideo(id, idPlaylist, itemIdPlaylist, GlobalTools.encodeLatin(title), urlImage, totalResults,
								prevPageToken, nextPageToken);
						listVideo.add(video);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de récupérer les vidéos d'une playlist");
		}
		return listVideo;
	}

	/**
	 * Ajoute la playlist et renvoi son ID
	 * 
	 * @param title
	 * @param description
	 * @param privacy
	 * @return String
	 */
	public String addPlaylistYTData(String title, String description, String privacy) {
		String idPlaylist = "";
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet,status");
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field", "id");

			// Construction de la playlist avec snippet et status
			PlaylistSnippet snippet = new PlaylistSnippet();
			snippet.setTitle(title);
			snippet.setDescription(description);
			PlaylistStatus status = new PlaylistStatus();
			if (privacy.trim().equals("private")) {
				status.setPrivacyStatus("private");
			} else if (privacy.trim().equals("unlisted")) {
				status.setPrivacyStatus("unlisted");
			} else {
				status.setPrivacyStatus("public");
			}
			Playlist playlist = new Playlist();
			playlist.setSnippet(snippet);
			playlist.setStatus(status);

			// Ajoute la playlist de l'utilisateur OAuth
			YouTube.Playlists.Insert request = youtube.playlists().insert(parts, playlist);
			request.setFields(parameters.get("field").toString());
			// Execute the request
			Playlist response = request.execute();

			// Renvoi l'ID YouTube de la playlist
			idPlaylist = response.getId();
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible d'ajouter la playlist");
		}
		return idPlaylist;
	}

	/**
	 * Modifie la playlist et renvoi son ID
	 * 
	 * @param playlist
	 * @param title
	 * @param description
	 * @param privacy
	 * @return String
	 */
	public String updatePlaylistYTData(Playlist playlist, String title, String description, String privacy) {
		String idPlaylist = "";
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet,status");
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field", "id,snippet(title,description),status(privacyStatus)");

			// Modification de la playlist avec snippet et status
			PlaylistSnippet snippet = playlist.getSnippet();
			snippet.setTitle(title);
			snippet.setDescription(description);
			PlaylistStatus status = playlist.getStatus();
			if (privacy.trim().equals("private")) {
				status.setPrivacyStatus("private");
			} else if (privacy.trim().equals("unlisted")) {
				status.setPrivacyStatus("unlisted");
			} else {
				status.setPrivacyStatus("public");
			}
			playlist.setSnippet(snippet);
			playlist.setStatus(status);

			// Modifie la playlist de l'utilisateur OAuth
			YouTube.Playlists.Update request = youtube.playlists().update(parts, playlist);
			request.setFields(parameters.get("field").toString());
			// Execute the request
			Playlist response = request.execute();

			// Renvoi l'ID YouTube de la playlist
			idPlaylist = response.getId();
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de mettre à jour la playlist");
		}
		return idPlaylist;
	}

	/**
	 * Supprime la playlist et renvoi boolean
	 * 
	 * @param idPlaylist
	 * @return boolean
	 */
	public boolean deletePlaylistYTDAta(String idPlaylist) {
		boolean deleted = false;
		try {
			// Préparation des paramètres
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("id", idPlaylist);

			// Supprime la playlist de l'utilisateur OAuth selon l'ID YouTube
			youtube.playlists().delete(parameters.get("id").toString()).execute();
			deleted = true;
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de supprimer la playlist");
		}
		return deleted;
	}

	/**
	 * Ajoute la vidéo à la playlist et renvoi son ID
	 * 
	 * @param idPlaylist
	 * @param urlId
	 * @return String
	 */
	public String addVideoPlaylistYTData(String idPlaylist, String urlId) {
		String videoId = "";
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet");
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field", "id");

			// Modification de l'item playlist avec snippet
			ResourceId resourceId = new ResourceId();
			resourceId.set("kind", "youtube#video");
			resourceId.set("videoId", urlId);
			PlaylistItemSnippet snippet = new PlaylistItemSnippet();
			snippet.setResourceId(resourceId);
			snippet.setPlaylistId(idPlaylist);
			PlaylistItem playlistItem = new PlaylistItem();
			playlistItem.setSnippet(snippet);

			// Ajoute la vidéo à la playlist de l'utilisateur OAuth
			YouTube.PlaylistItems.Insert request = youtube.playlistItems().insert(parts, playlistItem);
			request.setFields(parameters.get("field").toString());
			// Execute the request
			PlaylistItem response = request.execute();

			// Renvoi l'ID YouTube de la vidéo
			videoId = response.getId();
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible d'ajouter la vidéo à la playlist");
		}
		return videoId;
	}

	/**
	 * Supprime la vidéo de la playlist et renvoi boolean
	 * 
	 * @param playlistItemId
	 * @return boolean
	 */
	public boolean deleteVideoPlaylistYTDAta(String playlistItemId) {
		boolean deleted = false;
		try {
			// Préparation des paramètres
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("id", playlistItemId);

			// Supprime la vidéo de la playlist de l'utilisateur OAuth selon l'ID YouTube
			youtube.playlistItems().delete(parameters.get("id").toString()).execute();

			deleted = true;
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de supprimer la vidéo de la playlist");
		}
		return deleted;
	}

	/**
	 * Recherche des vidéos à partir de mots clefs et du token de page
	 * 
	 * @param searchInput
	 * @param pageToken
	 * @return List<YouTubeVideo>
	 */
	public List<YouTubeVideo> searchKeywordsResults(String searchInput, String pageToken) {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet");
			List<String> type = Collections.singletonList("video");
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field", "pageInfo(totalResults),nextPageToken,prevPageToken,items(id(videoId),snippet(title,thumbnails(default(url))))");
			parameters.put("maxResults", "10");
			parameters.put("embeddable", "true");
			parameters.put("q", searchInput);
			parameters.put("pageToken", pageToken);

			if (parameters.containsKey("q") && parameters.get("q") != "") {
				// Recherche des vidéos
				YouTube.Search.List request = youtube.search().list(parts);
				request.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
				request.setQ(parameters.get("q").toString());
				request.setType(type);
				request.setFields(parameters.get("field").toString());
				request.setVideoEmbeddable(parameters.get("embeddable").toString());
				if (parameters.containsKey("pageToken") && parameters.get("pageToken") != null && !parameters.get("pageToken").isEmpty()) {
					request.setPageToken(pageToken);
				}
				// Execute the request
				SearchListResponse response = request.execute();

				// Récupère la pagination et la liste des résultats
				int totalResults = response.getPageInfo().getTotalResults();
				String prevPageToken = response.getPrevPageToken();
				String nextPageToken = response.getNextPageToken();
				List<SearchResult> listSearchResult = response.getItems();

				// Conversion en videos
				if (listSearchResult != null && listSearchResult.size() > 0) {
					for (SearchResult searchResult : listSearchResult) {
						String id = searchResult.getId().getVideoId();
						String title = searchResult.getSnippet().getTitle();
						String urlImage = searchResult.getSnippet().getThumbnails().getDefault().getUrl();
						// Ajoute la vidéo à la liste
						YouTubeVideo video = new YouTubeVideo(id, "", "", title, urlImage, totalResults, prevPageToken, nextPageToken);
						listVideo.add(video);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de rechercher la liste des vidéos par mots-clefs");
		}
		return listVideo;
	}

	/**
	 * Recherche des vidéos à partir d'une URL (contenant Playlist ou Video) et de son token de page
	 * 
	 * @param searchInput
	 * @param pageToken
	 * @return List<YouTubeVideo>
	 */
	public List<YouTubeVideo> searchUrlResults(String searchInput, String pageToken) {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		// Traitement de la saisie de recherche
		String urlId = parseUrlId(searchInput);

		if (urlId.indexOf("PL") == 0 && urlId.length() == 34) {
			// Recherche d'une playlist par URL
			listVideo = searchPlaylistUrlResults(urlId, pageToken);
		} else if (urlId.length() == 11) {
			// Recherche d'une vidéo par URL
			listVideo = searchVideoUrlResults(urlId);
		}
		return listVideo;
	}

	/**
	 * Recherche des vidéos à partir d'une URL de playlist et de son token de page
	 * 
	 * @param urlIdPlaylist
	 * @param pageToken
	 * @return List<YouTubeVideo>
	 */
	private List<YouTubeVideo> searchPlaylistUrlResults(String urlIdPlaylist, String pageToken) {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();

		// Récupère la liste des vidéos de la playlist
		listVideo = getVideosYTData(urlIdPlaylist, pageToken);

		return listVideo;
	}

	/**
	 * Recherche des vidéos à partir d'une URL de vidéo
	 * 
	 * @param urlVideoId
	 * @return List<YouTubeVideo>
	 */
	private List<YouTubeVideo> searchVideoUrlResults(String urlVideoId) {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		try {
			// Préparation des paramètres
			List<String> parts = Collections.singletonList("snippet");
			List<String> id = Collections.singletonList(urlVideoId);
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("field", "items(id,snippet(title,thumbnails(default(url))))");

			if (parameters.containsKey("id") && parameters.get("id") != "") {
				// Recherche la vidéo
				YouTube.Videos.List request = youtube.videos().list(parts);
				request.setId(id);
				request.setFields(parameters.get("field").toString());
				// Execute the request
				VideoListResponse response = request.execute();

				// Conversion en video
				if (response != null) {
					Video videoResponse = response.getItems().get(0);
					String vidId = videoResponse.getId();
					String title = videoResponse.getSnippet().getTitle();
					String urlImage = videoResponse.getSnippet().getThumbnails().getDefault().getUrl();
					// Ajoute la vidéo à la liste
					YouTubeVideo video = new YouTubeVideo(vidId, "", "", title, urlImage, 1, "", "");
					listVideo.add(video);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			logger.error("YouTube Data - Impossible de rechercher la vidéo par URL");
		}
		return listVideo;
	}

	/**
	 * Parse l'Url ID (video ou playlist) à partir d'une recherche par Url
	 * 
	 * @param searchInput
	 * @return String
	 */
	private String parseUrlId(String searchInput) {
		String urlId = "";
		try {
			if (searchInput != null) {
				searchInput = searchInput.trim();

				if (searchInput.lastIndexOf("list=PL") != -1) {
					// Format ex: https://www.youtube.com/watch?v=q4y0KOeXViI&list=PL590L5WQmH8cUsRyHkk1cPGxW0j5kmhm0&index=2&t=0s
					// Récupère l'ID de la playlist (34 caractères)
					int index = searchInput.lastIndexOf("list=PL");
					urlId = searchInput.substring(index + 5, index + 39);
				} else {

					if (searchInput.lastIndexOf("youtube") != -1 && searchInput.lastIndexOf("watch?") != -1 && searchInput.lastIndexOf("v=") != -1) {
						// Format ex: https://www.youtube.com/watch?v=GX8Hg6kWQYI
						// Récupère l'ID de la vidéo (11 caractères)
						int index = searchInput.lastIndexOf("v=");
						urlId = searchInput.substring(index + 2, index + 13);
					} else if (searchInput.lastIndexOf("youtu.be/") != -1) {
						// Format ex: https://youtu.be/GX8Hg6kWQYI
						// Récupère l'ID de la vidéo (11 caractères)
						int index = searchInput.lastIndexOf("youtu.be/");
						urlId = searchInput.substring(index + 9, index + 20);
					}
				}
			}
		} catch (IndexOutOfBoundsException iobe) {
			logger.error("Impossible de parser une URL ID (vidéo/playlist)");
		}
		return urlId;
	}

	/**
	 * Renvoi le token de page en fonction de la méthode d'envoi
	 * 
	 * @param prevPageToken
	 * @param nextPageToken
	 * @param submitMethod
	 * @return String
	 */
	public String getPageToken(String prevPageToken, String nextPageToken, String submitMethod) {
		String pageToken = "";
		if (submitMethod != null) {
			if (submitMethod.equals("prevPage")) {
				// prevPage
				pageToken = prevPageToken == null ? "" : prevPageToken;
			} else if (submitMethod.equals("nextPage")) {
				// nextPage
				pageToken = nextPageToken == null ? "" : nextPageToken;
			}
		}
		return pageToken;
	}

	/**
	 * Renvoi la nouvelle page actuelle en fonction de la méthode d'envoi
	 * 
	 * @param currentPage
	 * @param submitMethod
	 * @return int
	 */
	public int getNewPage(int currentPage, String submitMethod) {
		if (submitMethod != null) {
			if (submitMethod.equals("prevPage")) {
				// prevPage
				if (currentPage <= 1) {
					currentPage = 1;
				} else {
					currentPage -= 1;
				}
			} else if (submitMethod.equals("nextPage")) {
				// nextPage
				currentPage += 1;
			} else {
				// initPage
				currentPage = 1;
			}
		}
		return currentPage;
	}

}
