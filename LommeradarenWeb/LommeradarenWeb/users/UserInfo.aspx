<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="UserInfo.aspx.cs" Inherits="LommeradarenWeb.users.UserInfo" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="header">
        <h3 class="headerlabels">Account Management</h3>
    </div>
    <div class="header">
        <asp:Label ID="userStatusHeaderLabel" runat="server" Text="<b>Status:</b>" CssClass="headerlabels" />
    </div>
    <div id="userStatus" class="content">
        <asp:Label ID="nameLabel" runat="server" Text="Label" />
        <br />
        <asp:Label ID="emailLabel" runat="server" Text="Label" />
    </div>

    <div class="header">
        <asp:Label ID="Label1" runat="server" Text="<b>Change Password:</b>" CssClass="headerlabels"></asp:Label>
    </div>
    <div class="content">
        <asp:TextBox ID="oldPasswordField" TextMode="Password" runat="server" />
        <asp:Label ID="Label3" runat="server" Text="Old password"></asp:Label><br />
        <asp:TextBox ID="newPasswordField" TextMode="Password" runat="server" />
        <asp:Label ID="Label4" runat="server" Text="New password"></asp:Label><br />
        <asp:TextBox ID="confirmNewPasswordField" TextMode="Password" runat="server" />
        <asp:Label ID="Label5" runat="server" Text="Confirm new password"></asp:Label><br />
        <asp:Button ID="ResetPasswordFieldButton" runat="server" OnClick="ResetPasswordFieldButton_Click" Text="Reset" />
        <asp:Button ID="ChangePasswordButton" runat="server" Text="Change Password" OnClick="ChangePasswordButton_Click" />
    </div>
    <div class="header">
        <asp:Label ID="Label2" runat="server" Text="<b>Change Email:</b>" CssClass="headerlabels"></asp:Label>
    </div>
    <div class="content">
        Change Email:<br />
        <asp:TextBox ID="newEmailField" runat="server" />
        New email<br />
        <asp:TextBox ID="confirmNewEmailField" runat="server" />
        Confirm new email<br />
        <asp:Button ID="ResetEmailFieldsButton" runat="server" OnClick="ResetEmailFieldsButton_Click" Text="Reset" />
        <asp:Button ID="ChangeEmailButton" runat="server" OnClick="ChangeEmailButton_Click" Text="Change Email" />
    </div>
</asp:Content>
