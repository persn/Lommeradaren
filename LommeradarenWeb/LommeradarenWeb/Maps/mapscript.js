/**
* Script for handling GoogleMaps v3
*
*/

//var url = 'http://test.shiprep.no/shiprepwebuisys/api/NearbyShips?latitude=63.4385841&longitude=10.4007685&altitude=0.0&radius=1890'
var loc = new google.maps.LatLng(63.4385841, 12.1828);

function initialize() {    

    var mapOptions = {
        center: loc,
        zoom: 4
    };

    var map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);

    var marker = new google.maps.Marker({
        position: { lat: 63.4385841, lng: 10.4007685 },
        map: map,
        title: 'Test marker',
        visible: true,
        icon: {
            path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            scale: 3,
            rotation: 90
        }
    });

    var marker2 = new google.maps.Marker({
        position: { lat: 63.43405, lng: 10.40105 },
        map: map,
        title: 'Test marker',
        visible: true,
        icon: {
            path: google.maps.SymbolPath.FORWARD_OPEN_ARROW,
            scale: 3,
            rotation: 90
        }
    });

    var marker3 = new google.maps.Marker({
        position: { lat: 63.43942, lng: 10.400000 },
        map: map,
        title: 'Test marker',
        visible: true,
        icon: {
            path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            scale: 3,
            rotation: 90
        }
    });

    var infobox = new InfoBox({
        content: document.getElementById("infobox"),
        disableAutoPan: false,
        zIndex: null,
        boxStyle: {
            background: "url('http://google-maps-utility-library-v3.googlecode.com/svn/trunk/infobox/examples/tipbox.gif') no-repeat",
            opacity: 0.90,
            width: "280px",
            height: "249px",
            paddingLeft: "12px"
        },
        closeBoxMargin: "12px 4px 2px 2px",
        closeBoxURL: "http://www.google.com/intl/en_us/mapfiles/close.gif",
        infoBoxClearance: new google.maps.Size(1, 1)
    });

    google.maps.event.addListener(marker, 'click', function () {
        infobox.open(map, this);
        map.panTo(loc);
    });

    var markers = [];

    markers.push(marker);
    markers.push(marker2);
    markers.push(marker3);

    var markerCluster = new MarkerClusterer(map, markers);

}

google.maps.event.addDomListener(window, 'load', initialize);