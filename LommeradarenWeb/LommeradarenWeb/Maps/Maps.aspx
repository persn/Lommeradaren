<%@ Page Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Maps.aspx.cs" Inherits="LommeradarenWeb.Maps" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
    <title>Lommeradaren Maps</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <link href="Maps.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD6bgJdC8gUDh2j-9rgs9L73_tRdmWAgF8&sensor=false"></script>
    <script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/infobox/src/infobox.js"></script>
    <script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/trunk/markerclusterer/src/markerclusterer.js"></script>
    <script type="text/javascript">
        var jsonRawData = JSON.parse('<%= GetRawData() %>');
        var jsonArray = jsonRawData.results;
    </script>
    <script type="text/javascript" src="mapscript.js"></script>

</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="header">
        <h3 class="headerlabels">Ship Map</h3>
    </div>
    <div id="map-canvas" style="width: 800px; height: 600px"></div>
    <div><asp:Label ID="ShipsAvailableLbl" runat="server" OnLoad="ShipsAvailableLbl_Load"></asp:Label></div>
</asp:Content>
