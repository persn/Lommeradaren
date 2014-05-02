using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Security;
using Logic;
using System.Diagnostics;

namespace LommeradarenWeb
{
    public partial class Login : System.Web.UI.Page
    {
        private GoogleAuthentication auth = new GoogleAuthentication();
        private UserController userAuth = new UserController();

        protected void Page_Load(object sender, EventArgs e)
        {
        }
        protected void Button1_Click(object sender, EventArgs e)
        {
            if (!txtUserName.Value.Equals("") && !txtUserPass.Equals(""))
            {
                if (userAuth.ValidateUserLogin(txtUserName.Value, txtUserPass.Value))
                    FormsAuthentication.RedirectFromLoginPage(txtUserName.Value,
                    chkPersistCookie.Checked);
                else
                    Response.Redirect("Login.aspx", true);
            }
        }

        protected void GoogleLoginButton_Click(object sender, EventArgs e)
        {
            Response.Redirect(auth.GetAutenticationURI().ToString());
        }
    }
}