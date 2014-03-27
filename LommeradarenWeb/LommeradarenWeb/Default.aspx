<%@ Page Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="LommeradarenWeb.Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="header">
        <h3 class="headerlabels">Lommeradaren</h3>
    </div>
    <div class="content">
        <div>
            <asp:Label ID="Label1" runat="server" Text="Bachelor assignment HiST"></asp:Label>
            <br /><br />
            <asp:Label ID="Label2" runat="server" Text="<b>Lommeradaren.</b> The main application which is in focus of this assignment."></asp:Label>
            <br />
            <asp:Label ID="Label3" runat="server" Text="Lommeradaren is an Android application developed in the edition of Eclipse which comes with the Android SDK."></asp:Label>
            <br />
            <asp:Label ID="Label4" runat="server" Text="It will utilize Augmented Reality and databases to track ships."></asp:Label>
            <br />
        </div>
        <br />
        <div>
            <asp:Label ID="Label5" runat="server" Text="<b>LommeradarenWeb.</b> A web backend for Lommeradaren, developed in Visual Studio 2013, using C#, ASP.NET and Entity Frameworks."></asp:Label>
        </div>
    </div>
</asp:Content>
