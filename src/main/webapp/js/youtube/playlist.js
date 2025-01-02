//////////////////////////////////
/////    YouTube Playlist    /////
//////////////////////////////////
//   @author Jonathan           //
//   @version 1.1               //
//   @since 30/12/2024          //
//////////////////////////////////

// Activation des playlists via AJAX
function setActive(path, index, id, checkbox) {
    var url = path + '/active_youtube_playlist';
    var newState = checkbox.checked;
    // POST
    $.post(
	    url,
	    {
		indexPlaylist : index,
		idPlaylist : id,
		checkboxActive : newState
	    },
	    // Callback
	    function callbackActivate(rawResult){
		var json = $.parseJSON(rawResult);
		// Parse le JSON retour
		var cbIndex = json.index;
		var state = json.state;
		var result = json.result;
		// Si retour avec succes
		if (state === 'success') {
		    // Met a jour la checkbox en fonction de la valeur du resultat
		    $('#checkboxIndex' + cbIndex).prop('checked', result);
		} else {
		    $('#checkboxIndex' + cbIndex).prop('checked', !newState);
		}
	    },
	    'text'
    );
}

// Au chargement
$(document).ready(function(){
    // Active les checkbox
    $('input[type=checkbox]:disabled').removeAttr('disabled');
});
