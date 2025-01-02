////////////////////////////////
/////    YouTube Player    /////
////////////////////////////////
//   @author Jonathan         //
//   @version 1.1             //
//   @since 30/12/2024        //
////////////////////////////////

/* Global */

// Variables
var player;
var playedVideos = [];
var currentIndex = 0;
var timer;

// Charge le lecteur YouTube
function onYouTubeIframeAPIReady() {
	player = new YT.Player('player', {
		height: '700',
		width: '100%',
		events: {
			'onReady': onPlayerReady,
			'onStateChange': onPlayerStateChange,
			'onError': onPlayerError
		},
		playerVars: {
			'controls': 0,
			'fs': 1,
			'iv_load_policy': 3,
			'rel': 0,
			'showinfo': 0
		}
	});
}

// Lorsque le lecteur est charge
function onPlayerReady(event) {
	// Initialisation
	initialize(event);
}

// Initialisation
function initialize(event) {
	// Prepare la liste des videos
	initPlayedVideos();
	// Charge le lecteur
	loadPlayer();
	// Regle le volume par defaut
	player.setVolume(50);
}

// Initialisation de la liste des videos a lire par defaut
function initPlayedVideos() {
	// Ajoute toutes les videos de playlists actives
	for (var i = 0; i < videos.length; i++) {
		playedVideos.push(videos[i]);
	}
}

// Charge la video dans le lecteur
function loadPlayer() {
	if (playedVideos.length > 0) {
		// Lance la video
		player.loadVideoById(playedVideos[currentIndex][0]);
		// Cache le contenu No Valid Video
		$('#noValid').hide();
		// Affiche le lecteur
		$('#playerBox').show();
		// Affiche les controles
		$('#playerControls').show();
		// Met a jour les controles au chargement
		updateTimerDisplay();
		updateProgressBar();
		updateVolumeDisplay();
		updateMuteDisplay();
		// Demarre l'intervalle pour mettre a jour la duree ecoulee
		// et la progression de la barre toutes les 0.5 secondes
		timer = setInterval(function() {
			updateTimerDisplay();
			updateProgressBar();
			updateVolumeDisplay();
		}, 1000);
	} else {
		// Met en pause le lecteur
		player.pauseVideo();
		// Cache le lecteur
		$('#playerBox').hide();
		// Cache les controls
		$('#playerControls').hide();
		// Affiche le contenu No Valid Video
		$('#noValid').show();
	}
}

/* UI Update */

// Met a jour la duree ecoulee
function updateTimerDisplay() {
	if (player != null && player.getCurrentTime() != null && player.getCurrentTime() >= 0) {
		$('#ytCurrentTime').text(formatTime(player.getCurrentTime()));
	} else {
		$('#ytCurrentTime').text('--:--');
	}
	if (player != null && player.getDuration() != null && player.getDuration() >= 0) {
		$('#ytDuration').text(formatTime(player.getDuration()));
	} else {
		$('#ytDuration').text('--:--');
	}
}

// Formatte l'affichage des temps
function formatTime(time) {
	var formattedTime = '';
	time = Math.round(time);
	var hours = Math.floor(time / 3600);
	var minutes = Math.floor((time - hours * 3600) / 60);
	var seconds = time - (hours * 3600) - (minutes * 60);
	minutes = (hours > 0 && minutes < 10) ? '0' + minutes : minutes;
	seconds = seconds < 10 ? '0' + seconds : seconds;
	formattedTime = hours > 0 ? hours + ':' + minutes + ':' + seconds : minutes + ':' + seconds;
	return formattedTime;
}

// Met a jour la barre de progression
function updateProgressBar() {
	if (player != null && player.getCurrentTime() != null && player.getCurrentTime() >= 0 && player.getDuration() != null && player.getDuration() > 0) {
		$('#ytProgressBar').val((player.getCurrentTime() / player.getDuration()) * 100);
	}
}

// Met a jour le volume actuel affiche
function updateVolumeDisplay() {
	if (player != null && player.getVolume() != null && player.getVolume() >= 0 && player.getVolume() <= 100) {
		$('#volValueVid').text(player.getVolume());
	} else {
		$('#volValueVid').text('--');
	}
}

// Met a jour le bouton mute/unmute
function updateMuteDisplay() {
	if (player.isMuted()) {
		$('#volMuteVid').show(0).css('display', 'inline-block');
		$('#volUnmuteVid').hide();
	} else {
		$('#volUnmuteVid').show(0).css('display', 'inline-block');
		$('#volMuteVid').hide();
	}
}

// En fonction de l'etat du lecteur
function onPlayerStateChange(event) {
	if (event.data == YT.PlayerState.PLAYING) {
		// Video en lecture, afficher bouton 'Pause'
		$('#pauseVid').show(0).css('display', 'inline-block');
		$('#playVid').hide();
		// Actualise le nom affiche
		var displayName = playedVideos[currentIndex][2];
		$('#ytVideoArtistTitle').text(displayName);
	} else if (event.data == YT.PlayerState.PAUSED) {
		// Video en pause, afficher bouton 'Play'
		$('#playVid').show(0).css('display', 'inline-block');
		$('#pauseVid').hide();
	} else if (event.data == YT.PlayerState.ENDED) {
		// Fin de video, efface le nom affiche
		$('#ytVideoArtistTitle').text('');
		// Passe automatiquement a la suivante
		nextVideo();
	}
}

/* UI Error */

// Lors d'une erreur de lecture de la video
function onPlayerError(event) {
	// Recupere l'ID de la video en erreur
	var errorID = playedVideos[currentIndex][0];
	// Retire la video de la liste de lecture
	playedVideos.splice(currentIndex, 1);
	// Affiche l'alerte erreur
	$('#errorId').text(errorID);
	var $alertDanger = $('#alert-danger')
	if ($alertDanger.is(':hidden')) {
		$alertDanger.show();
	}
	if ($alertDanger.hasClass('visuallyhidden') | $alertDanger.hasClass('hidden')) {
		$alertDanger.removeClass('visuallyhidden');
		$alertDanger.removeClass('hidden');
	}
	// Passe a la video suivante
	currentIndex++;
	if (currentIndex >= playedVideos.length) {
		currentIndex = 0;
	}
	// Charge le lecteur
	loadPlayer();
}

/* UI Actions */

// Lors de la selection d'une playlist
$('#selectPlaylist').on('change', function() {
	// Vide l'ancienne liste de videos
	playedVideos = [];
	// Recupere l'ID de la playlist selectionnee
	var playlistId = $(this).val();
	// Recupere les videos et genere le select
	var rawHtml = '<option value="-1" selected>' + allVideo + '</option>';
	for (var i = 0; i < videos.length; i++) {
		if (playlistId == -1 || playlistId == videos[i][1]) {
			var displayName = videos[i][2];
			if (displayName.length > 25) {
				displayName = displayName.substring(0, 25) + '..';
			}
			// Ajoute l'option
			rawHtml += '<option value="' + videos[i][0] + '">' + displayName + '</option>';
			// Ajoute la video a la liste des videos jouees
			playedVideos.push(videos[i]);
		}
	}
	// Met a jour le select
	$('#selectVideo').html(rawHtml);
	// Charge le lecteur
	currentIndex = 0;
	loadPlayer();
});

// Lors de la selection d'une video
$('#selectVideo').on('change', function() {
	// Vide l'ancienne liste de videos
	playedVideos = [];
	// Recupere l'ID de la video selectionnee
	var videoId = $(this).val();
	// Recupere l'ID de la playlist selectionnee
	var playlistId = $('#selectPlaylist').val();
	// Recupere la video
	for (var i = 0; i < videos.length; i++) {
		if (videoId == -1) {
			if (playlistId == -1) {
				// Ajoute la video a la liste
				playedVideos.push(videos[i]);
			} else if (playlistId == videos[i][1]) {
				// Ajoute la video a la liste
				playedVideos.push(videos[i]);
			}
		} else if (videoId == videos[i][0]) {
			// Ajoute la video a la liste
			playedVideos.push(videos[i]);
		}
	}
	// Charge le lecteur
	currentIndex = 0;
	loadPlayer();
});

// Lors de la selection d'une nouvelle valeur de progression
$('#ytProgressBar').on('mouseup touchend', function(e) {
	// Calcule le nouveau temps pour la video
	// Nouveau temps en secondes = durée totale en secondes * ( valeur de la progress bar / 100 )
	var newTime = player.getDuration() * (e.target.value / 100);

	// Avance/recule la video jusqu'au nouveau temps
	player.seekTo(newTime);
});

// Lors du clic sur 'Volume Down', baisse le volume de la video puis demute si besoin
$('#volDownVid').on('click', function() {
	// Recupere le volume actuel
	var currentVol = player.getVolume();
	// Determine le nouveau volume
	var newVol = 0;
	if (currentVol - 10 < 0) {
		newVol = 0;
	} else {
		newVol = currentVol - 10;
	}
	// Attribue le nouveau volume
	player.setVolume(newVol);
	$('#volValueVid').text(newVol);
	// Demute si besoin
	if (player.isMuted()) {
		player.unMute();
		// Met a jour le bouton
		$('#volUnmuteVid').show(0).css('display', 'inline-block');
		$('#volMuteVid').hide();
	}
});

// Lors du clic sur 'Volume Up', augmente le volume de la video puis demute si besoin
$('#volUpVid').on('click', function() {
	// Recupere le volume actuel
	var currentVol = player.getVolume();
	// Determine le nouveau volume
	var newVol = 0;
	if (currentVol + 10 > 100) {
		newVol = 100;
	} else {
		newVol = currentVol + 10;
	}
	// Attribue le nouveau volume
	player.setVolume(newVol);
	$('#volValueVid').text(newVol);
	// Demute si besoin
	if (player.isMuted()) {
		player.unMute();
		// Met a jour le bouton
		$('#volUnmuteVid').show(0).css('display', 'inline-block');
		$('#volMuteVid').hide();
	}
});

// Lors du clic sur 'Precedent', passe a la video precedente
$('#prevVid').on('click', function() {
	currentIndex--;
	if (currentIndex < 0) {
		currentIndex = playedVideos.length - 1;
	}
	// Charge le lecteur
	loadPlayer();
});

// Lors du clic sur 'Lecture', reprend la lecture de la video
$('#playVid').on('click', function() {
	player.playVideo();
});

// Lors du clic sur 'Pause', met en pause la video
$('#pauseVid').on('click', function() {
	player.pauseVideo();
});

// Lors du clic sur 'Suivant', passe a la video suivante
$('#nextVid').on('click', nextVideo);
function nextVideo() {
	currentIndex++;
	if (currentIndex >= playedVideos.length) {
		currentIndex = 0;
	}
	// Charge le lecteur
	loadPlayer();
}

// Lors du clic sur 'Mute', active le son
$('#volMuteVid').on('click', function() {
	// Active le son
	if (player.isMuted()) {
		player.unMute();
	}
	// Met a jour le bouton
	$('#volUnmuteVid').show(0).css('display', 'inline-block');
	$('#volMuteVid').hide();
});

// Lors du clic sur 'Unmute', desactive le son
$('#volUnmuteVid').on('click', function() {
	// Desactive le son
	if (!player.isMuted()) {
		player.mute();
	}
	// Met a jour le bouton
	$('#volMuteVid').show(0).css('display', 'inline-block');
	$('#volUnmuteVid').hide();
});

// Lors du clic sur 'Plein ecran', bascule l'affichage de la video en plein ecran
$('#fullscreenVid').on('click', function() {
	var iframe = document.getElementById('player');
	var requestFullScreen = iframe.requestFullScreen || iframe.mozRequestFullScreen || iframe.webkitRequestFullScreen;
	if (requestFullScreen) {
		requestFullScreen.bind(iframe)();
	}
});

// Au chargement
$(document).ready(function() {
	if (videos.length > 0) {
		$('#selectVideo').show();
		$('#selectPlaylist').show();
	}
});