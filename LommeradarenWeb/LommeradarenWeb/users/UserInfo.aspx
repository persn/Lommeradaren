<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="UserInfo.aspx.cs" Inherits="LommeradarenWeb.users.UserInfo" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">


    <p>
        <asp:Label ID="nameLabel" runat="server" Text="Label" />
        <br />
        <asp:Label ID="emailLabel" runat="server" Text="Label" />
    </p>
    <p>
        Change Password:<br />
        <asp:TextBox ID="oldPasswordField" TextMode="Password" runat="server" />
        Old password<br />
        <asp:TextBox ID="newPasswordField" TextMode="Password" runat="server" />
        New password<br />
        <asp:TextBox ID="confirmNewPasswordField" TextMode="Password" runat="server" />
        Confirm new password<br />
        <asp:Button ID="ResetPasswordFieldButton" runat="server" OnClick="ResetPasswordFieldButton_Click" Text="Reset" />
        <asp:Button ID="ChangePasswordButton" runat="server" Text="Change Password" OnClick="ChangePasswordButton_Click" />
    </p>
    <p>
        Change Email:<br />
        <asp:TextBox ID="newEmailField" runat="server" />
        New email<br />
        <asp:TextBox ID="confirmNewEmailField" runat="server" />
        Confirm new email<br />
        <asp:Button ID="ResetEmailFieldsButton" runat="server" OnClick="ResetEmailFieldsButton_Click" Text="Reset" />
        <asp:Button ID="ChangeEmailButton" runat="server" OnClick="ChangeEmailButton_Click" Text="Change Email" />
    </p>
</asp:Content>
