/////////////////////////////////
/////        MyHome         /////
/////   Global Javascript   /////
/////////////////////////////////
//    @author Jonathan         //
//    @version 1.0             //
//    @since 01/03/2021        //
/////////////////////////////////

// Met à jour l'heure affichée toutes les secondes
function setClock() {
	// Actualisation
    function refresh() {
		var date = new Date();
		var str = (date.getHours() < 10 ? '0' : '') + date.getHours();
		str += ':' + (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
		str += ':' + (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();
		$('#clock').html(str);
    }
    refresh();
    setInterval(refresh, 1000);
};

// Affiche les reCaptcha
function showRecaptcha() {
    $('#recaptchaBox').show();
}

// Fermeture des alertes danger (error)
var dangerBox = $('#alert-danger');
$('#close-alert-danger').on('click', function () {
    if (!dangerBox.hasClass('hidden')) {
		dangerBox.addClass('visuallyhidden');
		dangerBox.one('transitionend', function(e) {
		    dangerBox.addClass('hidden');
		});
    }
});

// Fermeture des alertes success
var successBox = $('#alert-success');
$('#close-alert-success').on('click', function () {
    if (!successBox.hasClass('hidden')) {
		successBox.addClass('visuallyhidden');
		successBox.one('transitionend', function(e) {
		    successBox.addClass('hidden');
		});
    }
});

// Affiche le bouton pour effacer le champs de recherche
$('#clearSearch').show();
$('#inputSearch').addClass('big-marged-left');

// Efface le champs de recherche
$('#clearSearch').on('click', function () {
    // Supprime le contenu du texte de recherche
    $('#inputSearch').val('');
    // Focus sur le texte de recherche
    $('#inputSearch').focus();
});


// Image de chargement AJAX
$(document).ajaxStart(function(){
    $('#loadingImg').fadeIn('slow');
});
$(document).ajaxComplete(function(){
    $('#loadingImg').fadeOut('slow');
});
	
// Au chargement
$(document).ready(function(){
    // Chargement de l'horloge
    setClock();
    // Affichage des reCaptcha
    showRecaptcha();
});