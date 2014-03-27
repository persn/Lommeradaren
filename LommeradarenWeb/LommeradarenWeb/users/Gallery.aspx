<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Gallery.aspx.cs" Inherits="LommeradarenWeb.users.Gallery" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div>
        <h3>Gallery
        </h3>
    </div>
    <div>
        <asp:Table runat="server" ID="imageTable">
        </asp:Table>
    </div>
</asp:Content>
