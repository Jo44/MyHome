////////////////////////////
/////      Search      /////
////////////////////////////
//   @author Jonathan     //
//   @version 1.0         //
//   @since 01/03/2021    //
////////////////////////////

// Construis le lien de recherche en fonction du champs de recherche et du radio button selectionne
$('#searchBtn').on('click', function() {
    // Recupere le texte de recherche
    var inputSearchContent = $('#inputSearch');
    // Recupere le bouton
    var btn = $('#searchBtn');
    // En fonction du texte de recherche
    if (inputSearchContent.val()) {
		// Si texte present
		btn.attr('target', '_blank');
		if ($('#rdSearchGoogle').is(':checked')) {
		    btn.attr('href', '//www.google.com/search?q=' + inputSearchContent.val());
		} else if ($('#rdSearchYouTube').is(':checked')) {
		    btn.attr('href', '//www.youtube.com/results?search_query=' + inputSearchContent.val()); 
		} else if ($('#rdSearchAmazon').is(':checked')) {
		    btn.attr('href', '//www.amazon.com/s?url=search-alias%3Daps&field-keywords=' + inputSearchContent.val());
		}
    } else {
		// Si texte non present
		btn.attr('target', '_self');
		btn.attr('href', '');
    }
});
