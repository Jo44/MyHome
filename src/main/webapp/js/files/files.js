///////////////////////////
/////      Files      /////
///////////////////////////
//   @author Jonathan    //
//   @version 1.0        //
//   @since 01/03/2021   //
///////////////////////////

// Affiche le detail de chaque fichier et determine si l'upload est valide ou non
$('#fileInput').on('change', function() {
	// Variables
	var validFile = true;
	var validRequest = true;
	var totalRequestWeight = 0;
	var filesDetails = '';
	// Recupere les elements du DOM
	var fileInput = document.getElementById('fileInput');
	var selectedFile = document.getElementById('selectedFiles');
	var sendFilesButton = document.getElementById('sendFilesButton');
	// Valide ou verifie si aucun fichier n'est selectionne
	if (fileInput.files.length > 0) {
		// Pour chaque fichier selectionne
		for (var i = 0; i <= fileInput.files.length - 1; i++) {
			// Recupere le nom et le poids du fichier
			var fName = fileInput.files.item(i).name;
			var fSizeRaw = fileInput.files.item(i).size;
			// Determine si le poids du fichier est valide
			validFile = fSizeRaw <= (100 * (1024 * 1024));
			// Ajout le poids au poids total
			totalRequestWeight += fSizeRaw;
			// Formatte le poids du fichier
			var fSize = formatSize(fSizeRaw);
			// Affiche les details du fichier
			filesDetails += '<br />';
			if (validFile) {
				filesDetails += fName + ' (' + fSize + ') <i class="fas fa-check-circle fa-fw green"></i>';
			} else {
				validRequest = false;
				filesDetails += fName + ' (' + fSize + ') <i class="fas fa-times-circle fa-fw red"></i>';
			}
		}
		// Determine si le poids total des fichiers est valide
		if (validRequest) {
			validRequest = totalRequestWeight <= (250 * (1024 * 1024));
		}
		// Ajoute le total des fichiers
		if (fileInput.files.length < 2) {
			if (totalRequestWeight <= (250 * (1024 * 1024))) {
				filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">1</span> fichier (' + formatSize(totalRequestWeight) + ') <i class="fas fa-check-circle fa-fw green"></i><br />';
			} else {
				filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">1</span> fichier (' + formatSize(totalRequestWeight) + ') <i class="fas fa-times-circle fa-fw red"></i><br />';
			}
		} else {
			if (totalRequestWeight <= (250 * (1024 * 1024))) {
				filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">' + fileInput.files.length + '</span> fichiers (' + formatSize(totalRequestWeight) + ') <i class="fas fa-check-circle fa-fw green"></i><br />';
			} else {
				filesDetails += '<br /><br /><span class="bold">Total</span> : <span class="bold">' + fileInput.files.length + '</span> fichiers (' + formatSize(totalRequestWeight) + ') <i class="fas fa-times-circle fa-fw red"></i><br />';
			}
		}
		// Affiche les details
		selectedFile.innerHTML = filesDetails;
	} else {
		// Efface les details si aucun fichier selectionne
		selectedFile.innerHTML = '';
		validRequest = false;
	}
	// Active ou non le bouton 'Envoyer'
	sendFilesButton.disabled = !validRequest;
});

//Formatte l'affichage du poids d'un fichier
function formatSize(fSizeRaw) {
	var fSize;
	if (fSizeRaw <= 1) {
		// Poids en octet
		fSize = '<span class="bold">' + fSizeRaw + '</span> octet';
	} else if (fSizeRaw < 1024) {
		// Poids en octets
		fSize = '<span class="bold">' + fSizeRaw + '</span> octets';
	} else if (fSizeRaw < (1024 * 1024)) {
		// Poids en ko
		fSize = '<span class="bold">' + (fSizeRaw / 1024).toFixed(2) + '</span> ko';
	} else if (fSizeRaw < (1024 * 1024 * 1024)) {
		// Poids en Mo
		fSize = '<span class="bold">' + (fSizeRaw / (1024 * 1024)).toFixed(2) + '</span> Mo';
	} else {
		// Poids en Go
		fSize = '<span class="bold">' + (fSizeRaw / (1024 * 1024 * 1024)).toFixed(2) + '</span> Go';
	}
	return fSize;
}
