/////////////////////////////////
/////     Cookie Consent    /////
/////////////////////////////////
//   @author Jonathan          //
//   @version 1.0              //
//   @since 01/03/2021         //
/////////////////////////////////

// Initialise the cookie consent plugin
$(document).ready(function(){
    window.cookieconsent.initialise({
	'palette' : {
	    'popup' : {
		'background' : '#333333',
		'text' : '#dddddd'
	    },
	    'button' : {
		'background' : 'transparent',
		'text' : '#f0ad4e',
		'border' : '#f0ad4e'
	    }
	},
	'position' : 'bottom-right',
	'content' : {
	    'message' : cookieMsg,
	    'dismiss' : 'OK',
	    'link' : cookieLink
	}
    })
});
