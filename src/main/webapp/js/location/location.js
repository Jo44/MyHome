///////////////////////////
/////     Location    /////
///////////////////////////
//   @author Jonathan    //
//   @version 1.0        //
//   @since 01/03/2021   //
///////////////////////////

// Show map
$('#map').show();

// Set Google Map
async function initMap() {
	const { Map } = await google.maps.importLibrary("maps");
    var coordonnees = {lat: latParam, lng: lngParam};
    var map = new Map(document.getElementById('map'), {
	center: coordonnees,
	zoom: 6,
	styles: [
	    {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
	    {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
	    {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
	    {
		featureType: 'administrative.locality',
		elementType: 'labels.text.fill',
		stylers: [{color: '#d59563'}]
	    },
	    {
		featureType: 'poi',
		elementType: 'labels.text.fill',
		stylers: [{color: '#d59563'}]
	    },
	    {
		featureType: 'poi.park',
		elementType: 'geometry',
		stylers: [{color: '#263c3f'}]
	    },
	    {
		featureType: 'poi.park',
		elementType: 'labels.text.fill',
		stylers: [{color: '#6b9a76'}]
	    },
	    {
		featureType: 'road',
		elementType: 'geometry',
		stylers: [{color: '#38414e'}]
	    },
	    {
		featureType: 'road',
		elementType: 'geometry.stroke',
		stylers: [{color: '#212a37'}]
	    },
	    {
		featureType: 'road',
		elementType: 'labels.text.fill',
		stylers: [{color: '#9ca5b3'}]
	    },
	    {
		featureType: 'road.highway',
		elementType: 'geometry',
		stylers: [{color: '#746855'}]
	    },
	    {
		featureType: 'road.highway',
		elementType: 'geometry.stroke',
		stylers: [{color: '#1f2835'}]
	    },
	    {
		featureType: 'road.highway',
		elementType: 'labels.text.fill',
		stylers: [{color: '#f3d19c'}]
	    },
	    {
		featureType: 'transit',
		elementType: 'geometry',
		stylers: [{color: '#2f3948'}]
	    },
	    {
		featureType: 'transit.station',
		elementType: 'labels.text.fill',
		stylers: [{color: '#d59563'}]
	    },
	    {
		featureType: 'water',
		elementType: 'geometry',
		stylers: [{color: '#17263c'}]
	    },
	    {
		featureType: 'water',
		elementType: 'labels.text.fill',
		stylers: [{color: '#515c6d'}]
	    },
	    {
		featureType: 'water',
		elementType: 'labels.text.stroke',
		stylers: [{color: '#17263c'}]
	    }
	]
    });
    var marker = new google.maps.Marker({
		position: coordonnees,
		map: map
    });
};
