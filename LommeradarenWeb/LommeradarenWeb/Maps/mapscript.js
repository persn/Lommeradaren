/**
* Script for handling GoogleMaps v3
*
*/
var loc = new google.maps.LatLng(63.4385841, 12.1828);
var markers = [];

google.maps.event.addDomListener(window, 'load', initialize);

function initialize() {

    var mapOptions = {
        center: loc,
        zoom: 4
    };

    var map = new google.maps.Map(document.getElementById("map-canvas"),mapOptions);

    for (var i = 0; i < jsonArray.length; i++) {
        var marker = assembleMarker(map, jsonArray[i].title, parseFloat(jsonArray[i].lat), parseFloat(jsonArray[i].lng), parseFloat(jsonArray[i].course), jsonArray[i].webpage);
        marker["elevation"] = jsonArray[i].elevation;
        marker["imo"] = jsonArray[i].imo;
        marker["mmsi"] = jsonArray[i].mmsi;
        marker["speed"] = jsonArray[i].speed;
        marker["positionTime"] = jsonArray[i].positionTime;
        marker["webpage"] = jsonArray[i].webpage;

        var infobox = assembleInfoWindow();
        attachListenerToMarker(map,marker,infobox);

        markers.push(marker);
    }

    var markerCluster = new MarkerClusterer(map, markers);
}

/**
* Creates a marker on GoogleMap
*/
function assembleMarker(googleMap,title,lat,lng,rotation,url) {
    var newMarker = new google.maps.Marker({
        position: { lat: lat, lng: lng },
        map: googleMap,
        title: title,
        visible: true,
        icon: {
            path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            scale: 3,
            rotation: rotation
        }
    });
    return newMarker;
}

function assembleInfoWindow() {
    var newInfobox = new InfoBox({
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
        infoBoxClearance: new google.maps.Size(1, 1)
    });
    return newInfobox;
}

function assembleInfoWindowContent(title, lat, lng, elevation, imo, mmsi, speed, positionTime, url) {
    var infoContent = '<div id="infobox">'
    + '<div id="content">'
    + '<div id="siteNotice"></div>'
    + '<h1 id="firstHeading" class="firstHeading">' + title + '</h1>'
    + '<div id="bodyContent">'
    + '<p><img src="http://www.kystverket.no/Content/1.0.148.036/Images/logo.png" /></p>'
    + '<p><b>Latitude:</b> ' + lat + '<br /><b>Longitude:</b> ' + lng +'<br /><b>Elevation:</b> ' + elevation +'</p>'
    + '<p><b>IMO:</b> ' + imo + '<br /><b>MMSI:</b> ' + mmsi + '</p>'
    + '<p><b>Speed:</b> ' + speed +'<br /><b>Position Time:</b> ' + positionTime +'</p>'
    + '<p><b>Website:</b> <a href="' + url +'">' + title +'</a></p>'
    + '</div></div></div>'
    return infoContent;
}

function attachListenerToMarker(map,marker,infobox) {
    google.maps.event.addListener(marker, 'click', function () {
        console.log(this.getTitle());
        infobox.setContent(assembleInfoWindowContent(this.getTitle(), this.getPosition().lat(), this.getPosition().lng(), this.elevation, this.imo, this.mmsi, this.speed, this.positionTime, this.webpage));
        infobox.open(map, this);
        //map.panTo(loc);
    });
}
