﻿<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="googletest.aspx.cs" Inherits="LommeradarenWeb.googletest" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="header">
        <h3 class="headerlabels">Google Login Api test</h3>
    </div>
    <div class="content">
        <asp:Button runat="server" ID="loginGoogleButton" Text="Log In With Google" OnClick="loginGoogleButton_Click" /><br />
        <asp:Label runat="server" ID="Label0" Text=""></asp:Label><br />
        <asp:Label runat="server" ID="Label1" Text=""></asp:Label><br />
        <asp:Label runat="server" ID="Label2" Text=""></asp:Label><br />
        <asp:Label runat="server" ID="Label3" Text=""></asp:Label><br />
        <asp:Label runat="server" ID="Label4" Text=""></asp:Label>
    </div>
</asp:Content>
