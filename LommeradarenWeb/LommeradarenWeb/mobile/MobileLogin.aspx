<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="MobileLogin.aspx.cs" Inherits="LommeradarenWeb.MobileLogin" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
        <div>
            <asp:Table ID="Table1" runat="server" BorderStyle="None">
                <asp:TableRow>
                    <asp:TableCell>
                        <asp:Label ID="Label1" runat="server" Text="Username:"></asp:Label>
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell>
                        <input id="txtUserName" type="text" runat="server" />
                    </asp:TableCell>
                    <asp:TableCell>
                        <asp:RequiredFieldValidator ControlToValidate="txtUserName"
                            Display="Static" ErrorMessage="*Required" runat="server"
                            ID="vUserName" />
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell>
                        <asp:Label ID="Label2" runat="server" Text="Password:"></asp:Label>
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell>
                        <input id="txtUserPass" type="password" runat="server" />
                    </asp:TableCell><asp:TableCell>
                        <asp:RequiredFieldValidator ControlToValidate="txtUserPass"
                            Display="Static" ErrorMessage="*Required" runat="server"
                            ID="vUserPass" />
                    </asp:TableCell>
                </asp:TableRow>
                <asp:TableRow>
                    <asp:TableCell>
                        <asp:Button ID="Button1" runat="server" OnClick="Button1_Click" Text="Login" />
                        <asp:Label ID="lblMsg" ForeColor="red" Font-Name="Verdana" Font-Size="10" runat="server" />
                    </asp:TableCell>
                </asp:TableRow>
            </asp:Table>
        </div>
    </form>
</body>
</html>
