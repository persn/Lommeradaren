<%@ Page Title="" Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Newuser.aspx.cs" Inherits="LommeradarenWeb.Newuser" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="header">
        <h3 class="headerlabels">New User</h3>
    </div>
    <div class="content">
        <asp:Label ID="infobox" runat="server" />
        <br /><br />
        <asp:Table ID="Table1" runat="server" BorderStyle="None" >
            <asp:TableRow>
                <asp:TableCell>
                    <asp:Label ID="Label1" runat="server" Text="Username:"></asp:Label>
                </asp:TableCell>
                <asp:TableCell>
                    <asp:TextBox ID="usernameBox" runat="server" />
                </asp:TableCell><asp:TableCell>
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" ControlToValidate="usernameBox" ErrorMessage="*Required" />
                </asp:TableCell></asp:TableRow><asp:TableRow>
                <asp:TableCell>
                    <asp:Label ID="Label2" runat="server" Text="Email:"></asp:Label>
                </asp:TableCell><asp:TableCell>
                    <asp:TextBox ID="emailBox" runat="server" />
                </asp:TableCell><asp:TableCell>
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" ControlToValidate="emailBox" ErrorMessage="*Required" />
                </asp:TableCell></asp:TableRow><asp:TableRow>
                                 <asp:TableCell>
                    <asp:Label ID="Label3" runat="server" Text="Password:"></asp:Label>
                </asp:TableCell><asp:TableCell>
                    <asp:TextBox TextMode="Password" ID="passwordBox" runat="server" />
                </asp:TableCell><asp:TableCell>
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator3" runat="server" ControlToValidate="passwordBox" ErrorMessage="*Required" />
                </asp:TableCell></asp:TableRow><asp:TableRow>
                                <asp:TableCell>
                    <asp:Label ID="Label4" runat="server" Text="Confirm Password"></asp:Label>
                </asp:TableCell><asp:TableCell>
                    <asp:TextBox TextMode="Password" ID="confirmPasswordBox" runat="server" />
                </asp:TableCell><asp:TableCell>
                    <asp:CompareValidator ID="CompareValidator1" runat="server" ControlToCompare="passwordBox" ControlToValidate="confirmPasswordBox" ErrorMessage="*Passwords must match" />
                </asp:TableCell></asp:TableRow></asp:Table><br />
        <asp:Button ID="registerNewUserButton" runat="server" Text="Register" OnClick="registerNewUserButton_Click" />
    </div>
</asp:Content>
