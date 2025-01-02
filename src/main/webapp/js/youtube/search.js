////////////////////////////////
/////    YouTube Search    /////
////////////////////////////////
//   @author Jonathan         //
//   @version 1.1             //
//   @since 30/12/2024        //
////////////////////////////////

// Affichage du bloc de recherche
$('#searchBloc').show();

// Selection du mode de recherche
$('input[name=searchMethod]').on('change', function() {
	if (this.value == 'keyword') {
		// Keywords
		$('#textSearch').attr('placeholder', keywordExample);
	} else {
		// URL
		$('#textSearch').attr('placeholder', urlExample);
	}
});

// Gestion de la selection de resultats
var $checkboxes = $('input[name=cbUrlId]');
var $selectedVideoCount = $('#selectedVideoCount');
var $addVideoBtn = $('#addVideoBtn');
$checkboxes.on('change', function() {
	var countChecked = $checkboxes.filter(':checked').length;
	// Met a jour le compteur de selection
	$selectedVideoCount.html(countChecked);
	if (countChecked < 1) {
		// Met a jour le bouton
		$addVideoBtn.html(addVideo);
		// Desactive le bouton
		$addVideoBtn.attr('disabled', true);
	} else if (countChecked == 1) {
		// Met a jour le bouton
		$addVideoBtn.html(addVideo);
		// Active le bouton
		$addVideoBtn.attr('disabled', false);
	} else {
		// Met a jour le bouton
		$addVideoBtn.html(addVideos);
		// Active le bouton
		$addVideoBtn.attr('disabled', false);
	}
});

// Au chargement
$(document).ready(function() {
	// Gestion des boutons de pagination
	if (prevPageToken.trim()) {
		$('#prevResults').prop('disabled', false);
	}
	if (nextPageToken.trim()) {
		$('#nextResults').prop('disabled', false);
	}
});