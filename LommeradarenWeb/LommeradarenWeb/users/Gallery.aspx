<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Gallery.aspx.cs" Inherits="LommeradarenWeb.users.Gallery" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <h1>Gallery_Placeholder
        
    </h1>
    <% List<string> pics = getPics();%>
    <% foreach (string s in pics){%>
    <% Response.Write("<img runat\"server\" src=" + s + ">"); %>
    <% } %>


    
</asp:Content>
