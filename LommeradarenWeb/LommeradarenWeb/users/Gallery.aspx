<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Gallery.aspx.cs" Inherits="LommeradarenWeb.users.Gallery" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="header">
        <h3 class="headerlabels">Gallery
        </h3>
    </div>
    <div class="content">
        <asp:Panel ID="Panel1" runat="server" ScrollBars="Horizontal">
            <asp:Table runat="server" ID="imageTable">
            </asp:Table>
        </asp:Panel>
    </div>
    <div class="content">
        <asp:Table runat="server" ID="infoTable" Visible="false">
            <asp:TableRow>
                <asp:TableCell>
                    <asp:Image ID="BigImage" runat="server" Visible="true" />
                </asp:TableCell>
                <asp:TableCell>
                    <asp:Label ID="LatitudeLabel" runat="server" Visible="true" Text="<b>Latitude:</b> placeholder"></asp:Label><br />
                    <asp:Label ID="LongitudeLabel" runat="server" Visible="true" Text="<b>Longitude:</b> placeholder"></asp:Label><br />
                    <asp:Label ID="ElevationLabel" runat="server" Visible="true" Text="<b>Elevation:</b> placeholder"></asp:Label><br />
                    <br />
                    <asp:Label ID="ImoLabel" runat="server" Visible="true" Text="<b>Imo:</b> placeholder"></asp:Label><br />
                    <asp:Label ID="MMSILabel" runat="server" Visible="true" Text="<b>MMSI:</b> placeholder"></asp:Label><br />
                    <br />
                    <asp:Label ID="SpeedLabel" runat="server" Visible="true" Text="<b>Speed:</b> placeholder"></asp:Label><br />
                    <asp:Label ID="PositionTimeLabel" runat="server" Visible="true" Text="<b>PositionTime:</b> placeholder"></asp:Label><br />
                    <br />
                    <asp:Label ID="WebsiteLabel" runat="server" Visible="true" Text="<b>Website:</b> placeholder"></asp:Label>
                    <br />
                </asp:TableCell>
            </asp:TableRow>
            <asp:TableRow>
                <asp:TableCell>
                    <asp:Button ID="DeleteImageButton" runat="server" Text="Delete" Visible="true" OnClick="DeleteImageButton_Click" />
                    <asp:Button ID="ViewLargeImageButton" runat="server" Text="View Large Image" Visible="true" OnClick="ViewLargeImageButton_Click" />
                </asp:TableCell>
            </asp:TableRow>
        </asp:Table>
        
    </div>
</asp:Content>
