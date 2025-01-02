/////////////////////////////////
/////        Max Rows       /////
/////   (results by page)   /////
/////////////////////////////////
//   @author Jonathan          //
//   @version 1.0              //
//   @since 01/03/2021         //
/////////////////////////////////

// Changement du maxRows via AJAX
function setNewMaxRows(path) {
    var url = path + '/set_max_rows';
    var newMax = $("#maxRows option:selected").val();
    // POST
    $.post(
	    url,
	    {
			newMaxRows : newMax
	    },
	    // Callback
	    function callbackMaxRows(rawResult){
			var json = $.parseJSON(rawResult);
			// Parse le JSON retour
			var state = json.state;
			var result = json.result;
			// Si retour sans succes
			if (state != 'success') {
			    alert('change max rows -> FAIL -> ' + result);
			}
	    },
	    'text'
    );
}
