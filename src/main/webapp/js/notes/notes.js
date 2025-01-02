///////////////////////////
/////      Notes      /////
///////////////////////////
//   @author Jonathan    //
//   @version 1.0        //
//   @since 01/03/2021   //
///////////////////////////

// Initialisation de NicEditor
bkLib.onDomLoaded(function() {
    new nicEditor({
	maxHeight : 250,
	fullPanel : false,
	buttonList : [ 'fontFormat', 'fontSize', 'fontFamily', 'bold', 'italic', 'underline', 'strikethrough', 'left', 'center',
	    		'right', 'justify', 'ol', 'ul', 'indent', 'outdent', 'forecolor', 'bgcolor', 'link', 'unlink', 'xhtml' ]
    }).panelInstance('areaComment');
});