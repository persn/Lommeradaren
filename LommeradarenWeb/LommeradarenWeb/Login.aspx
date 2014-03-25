<%@ Page Language="C#" MasterPageFile="~/Master1.Master" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="LommeradarenWeb.Login" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">

            <h3>Login Page
            </h3>
            <table>
                <tr>
                    <td>Username:</td>
                    <td>
                        <input id="txtUserName" type="text" runat="server" /></td>
                    <td>
                        <asp:RequiredFieldValidator ControlToValidate="txtUserName"
                            Display="Static" ErrorMessage="*" runat="server"
                            ID="vUserName" /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td>
                        <input id="txtUserPass" type="password" runat="server" /></td>
                    <td>
                        <asp:RequiredFieldValidator ControlToValidate="txtUserPass"
                            Display="Static" ErrorMessage="*" runat="server"
                            ID="vUserPass" />
                    </td>
                </tr>
                <tr>
                    <td>Persistent Cookie:</td>
                    <td>
                        <asp:CheckBox ID="chkPersistCookie" runat="server" AutoPostBack="false" /></td>
                    <td></td>
                </tr>
            </table>
            <asp:Button ID="Button1" runat="server" OnClick="Button1_Click" Text="Button" />
            <p></p>
            <asp:Label ID="lblMsg" ForeColor="red" Font-Name="Verdana" Font-Size="10" runat="server" />

</asp:Content>
