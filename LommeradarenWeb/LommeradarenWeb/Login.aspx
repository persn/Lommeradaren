<%@ Page Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="LommeradarenWeb.Login" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="/styling/contentdisplay.css" rel="stylesheet" type="text/css" />
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="header">
        <h3 class="headerlabels">Login Page</h3>
    </div>
    <div class="content">
    <asp:Table ID="Table1" runat="server" BorderStyle="None">
        <asp:TableRow>
            <asp:TableCell>
                <asp:Label ID="Label1" runat="server" Text="Username:"></asp:Label>
            </asp:TableCell>
            <asp:TableCell>
                <input id="txtUserName" type="text" runat="server" />
            </asp:TableCell>
            <%--<asp:TableCell>
                <asp:RequiredFieldValidator ControlToValidate="txtUserName"
                    Display="Static" ErrorMessage="*Required Field" runat="server"
                    ID="vUserName" />
            </asp:TableCell>--%>
        </asp:TableRow>
        <asp:TableRow>
            <asp:TableCell>
                <asp:Label ID="Label2" runat="server" Text="Password:"></asp:Label>
            </asp:TableCell>
            <asp:TableCell>
                <input id="txtUserPass" type="password" runat="server" />
            </asp:TableCell>
            <%--<asp:TableCell>
                <asp:RequiredFieldValidator ControlToValidate="txtUserPass"
                    Display="Static" ErrorMessage="*Required field" runat="server"
                    ID="vUserPass" />
            </asp:TableCell>--%>
        </asp:TableRow>
        <asp:TableRow>
            <asp:TableCell>
                <asp:Label ID="Label3" runat="server" Text="Remember Me:"></asp:Label>
            </asp:TableCell>
            <asp:TableCell>
                <asp:CheckBox ID="chkPersistCookie" runat="server" AutoPostBack="false" />
            </asp:TableCell>
        </asp:TableRow>
    </asp:Table>
    <asp:Button ID="Button1" runat="server" OnClick="Button1_Click" Text="Login" />
    <br /><br />
    <asp:Label ID="lblMsg" ForeColor="red" Font-Name="Verdana" Font-Size="10" runat="server" />
    </div>
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">
    <div class="content">
            <asp:Button runat="server" Text="Log In Using Google" ID="GoogleLoginButton" OnClick="GoogleLoginButton_Click" />
        </div>
</asp:Content>
