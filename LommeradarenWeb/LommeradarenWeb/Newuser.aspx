<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Newuser.aspx.cs" Inherits="LommeradarenWeb.Newuser" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">

    <h3>New User</h3>

    <div>
        <asp:Label ID="infobox" runat="server" />
        <p>
            Username:<asp:TextBox ID="usernameBox" runat="server" />
            <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" ControlToValidate="usernameBox" ErrorMessage="*Required"/>
        </p>
        <p>
            Email:<asp:TextBox ID="emailBox" runat="server" />
            <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" ControlToValidate="emailBox" ErrorMessage="*Required"/>
        </p>
        <p>
            Password:<asp:TextBox TextMode="Password" ID="passwordBox" runat="server" />
            <asp:RequiredFieldValidator ID="RequiredFieldValidator3" runat="server" ControlToValidate="passwordBox" ErrorMessage="*Required"/>
        </p>
        <p>
            Confirm Password:<asp:TextBox TextMode="Password" ID="confirmPasswordBox" runat="server" />
            <asp:CompareValidator ID="CompareValidator1" runat="server" ControlToCompare="passwordBox" ControlToValidate="confirmPasswordBox" ErrorMessage="*Passwords must match"/>
        </p>
        <asp:Button ID="registerNewUserButton" runat="server" Text="Register" OnClick="registerNewUserButton_Click" />
    </div>
</asp:Content>
