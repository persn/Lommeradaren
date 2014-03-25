<%@ Page Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Maps.aspx.cs" Inherits="LommeradarenWeb.Maps" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>Lommeradaren Maps</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <link href="Maps.css" rel="stylesheet" type="text/css" />
    <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-2.1.0.js"></script>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD6bgJdC8gUDh2j-9rgs9L73_tRdmWAgF8&sensor=false"></script>
    <script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/infobox/src/infobox.js"></script>
    <script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer.js"></script>
    <script type="text/javascript" src="mapscript.js"></script>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="infobox-wrapper">
        <div id="infobox">
            <div id="content">
                <div id="siteNotice"></div>
                <h1 id="firstHeading" class="firstHeading">Namdalingen</h1>
                <div id="bodyContent">
                    <p>
                        <asp:Image ID="Image1" runat="server" src="http://www.kystverket.no/Content/1.0.148.036/Images/logo.png" />
                    </p>
                    <p>
                        <b>Latitude:</b> 63<br />
                        <b>Longitude:</b> 10<br />
                        <b>Elevation:</b> 0
                    </p>
                    <p>
                        <b>IMO:</b> 2131241<br />
                        <b>MMSI:</b> 12312421
                    </p>

                    <p>
                        <b>Speed:</b> Over 9000<br />
                        <b>Position Time:</b> 24.03.2014 14:30
                    </p>
                    <p><b>Website:</b> http://Blabla</p>
                </div>
            </div>
        </div>
    </div>
    <div id="map-canvas" style="width:800px; height:600px" />
</asp:Content>
