///////////////////////////
/////     Contacts    /////
///////////////////////////
//   @author Jonathan    //
//   @version 1.0        //
//   @since 01/03/2021   //
///////////////////////////

/* International Telephone Input */

// Initialise le plugin
var utilsScriptPath = path + '/js/plugins/intlTelInput/utils.js';
$('#textPhone').intlTelInput({
	utilsScript: utilsScriptPath
});

// Verifie le numero de telephone lors de la soumission d'un formulaire
$('#contactForm').submit(function() {
	// Recupere le numero au format international
	var intlNumber = $('#textPhone').intlTelInput('getNumber');
	// Verifie si le numero est valide ou vide
	var isValidOrEmpty = false;
	if (!intlNumber || $('#textPhone').intlTelInput('isValidNumber')) {
		isValidOrEmpty = true;
	}
	// Envoi le numero au formulaire
	$('#intlPhoneNumber').val(intlNumber);
	// Message d'erreur si besoin
	if (!isValidOrEmpty) {
		var $alertDanger = $('#alert-danger')
		if ($alertDanger.is(':hidden')) {
			$alertDanger.show();
		}
		if ($alertDanger.hasClass('visuallyhidden') | $alertDanger.hasClass('hidden')) {
			$alertDanger.removeClass('visuallyhidden');
			$alertDanger.removeClass('hidden');
		}
	}
	// Renvoi la validation
	return isValidOrEmpty;
});
