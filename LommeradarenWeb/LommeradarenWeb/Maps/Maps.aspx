<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Maps.aspx.cs" Inherits="LommeradarenWeb.Maps" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        html {
            height: 100%;
        }

        body {
            height: 100%;
            margin: 0;
            padding: 0;
        }

        #map-canvas {
            height: 100%;
        }

    </style>
    <script type="text/javascript"
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD6bgJdC8gUDh2j-9rgs9L73_tRdmWAgF8&sensor=false">
    </script>
    <script type="text/javascript">
        function initialize() {

            var mapOptions = {
                center: { lat: 63.5926, lng: 12.1828 },
                zoom: 4
                //mapTypeControlOptions: {
                //    mapTypeIds: [google.maps.MapTypeId.ROADMAP, google.maps.MapTypeId.TERRAIN, google.maps.MapTypeId.SATELLITE, google.maps.MapTypeId.HYBRID]
                //}
            };

            var map = new google.maps.Map(document.getElementById("map-canvas"),
                mapOptions);

            var marker = new google.maps.Marker({
                position: { lat: 63.4385841, lng: 10.4007685 },
                map: map,
                title: 'Test marker'
            });

            var infowindow = new google.maps.InfoWindow({
                content: '<div id="content">'
                    + '<div id="siteNotice"></div>'
                    + '<h1 id="firstHeading" class="firstHeading">Kua</h1>'
                    + '<div id="bodyContent">'
                    + '<p>Kua er et vakkert dyr, frodig og skjønn</p>'
                    + '</div></div>'
            });

            google.maps.event.addListener(marker, 'click', function () {
                infowindow.open(map, marker);
            });

        }
        google.maps.event.addDomListener(window, 'load', initialize);
    </script>
</head>
<body>
    <form id="form1" runat="server">
        <div id="map-canvas" />
    </form>
</body>
</html>
