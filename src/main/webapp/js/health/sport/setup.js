//////////////////////////////
/////     Sport Setup    /////
//////////////////////////////
//   @author Jonathan       //
//   @version 1.0           //
//   @since 28/12/2024      //
//////////////////////////////

// Calcul le nombre de jours entre deux dates
function dayCount(fromDate, toDate) {
    if (!(fromDate instanceof Date) || !(toDate instanceof Date)) {
        return NaN;
    }
    const difMs = Math.abs(toDate.getTime() - fromDate.getTime());
    const difDays = Math.ceil(difMs / (1000 * 60 * 60 * 24));
    return difDays;
}

// Reconstitution des données quotidiennes de sport
function generateDailyDatas(sportDatas) {
    // Variables
    var selectFromDateTime = $('#selectFromDateTime');
    var selectToDateTime = $('#selectToDateTime');
	const dataDays = [];
	const dataSport = [];

    // Parse dates (avec gestion des erreurs)
    var dateFromValue = selectFromDateTime.val();
    var dateToValue = selectToDateTime.val();
    let dateFrom;
    let dateTo;
    const now = new Date();
    const nowISO = now.toISOString().slice(0, 16); // Format YYYY-MM-DDTHH:mm

    try {
        dateFrom = dateFromValue ? new Date(dateFromValue) : new Date(nowISO);
        dateTo = dateToValue ? new Date(dateToValue) : new Date(nowISO);
    } catch (error) {
        console.error("Erreur lors de l'analyse des dates :", error);
        return { dates: [], sport: [] };
    }

    if (isNaN(dateFrom) || isNaN(dateTo)) {
        console.error("Dates invalides. Veuillez utiliser le format AAAA-MM-JJTHH:mm.");
        return { dates: [], sport: [] };
    }

	// Calcul du nombre de jours
    const difDays = dayCount(dateFrom, dateTo);
    if (isNaN(difDays)) {
        console.error("Erreur lors du calcul de la différence de jours.");
        return { dates: [], sport: [] };
    }

	// Reconstruction des données quotidiennes
    for (let i = difDays; i >= 0; i--) {
        const currentDate = new Date(dateTo);
        currentDate.setDate(dateTo.getDate() - i);
        const formattedCurrentDate = currentDate.toISOString().split('T')[0];
        dataDays.push(currentDate.toISOString());

        let sportDay = 0;
        for (const sportData of sportDatas) {
			// Jour de sport
            if (sportData.startsWith(formattedCurrentDate)) {
                sportDay = 1;
                break;
            }
        }
        dataSport.push(sportDay);
    }

	// Retour des données
    return { dates: dataDays, sport: dataSport };
}
