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
    /// <summary>
    /// Handles logging in via username/password or googles oauth services
    /// </summary>
    public partial class Login : System.Web.UI.Page
    {
        private GoogleAuthentication gAuth = new GoogleAuthentication();
        private UserController uAuth = new UserController();

        protected void Page_Load(object sender, EventArgs e)
        {
        }
        /// <summary>
        /// Allows a user to log in using the username/password combination in the input fields
        /// </summary>
        protected void Button1_Click(object sender, EventArgs e)
        {
            if (!txtUserName.Value.Equals("") && !txtUserPass.Equals(""))
            {
                if (uAuth.ValidateUserLogin(txtUserName.Value, txtUserPass.Value))
                    FormsAuthentication.RedirectFromLoginPage(txtUserName.Value,
                    chkPersistCookie.Checked);
                else
                    Response.Redirect("Login.aspx", true);
            }
        }

        /// <summary>
        /// Allows a user to log in using googles oauth service
        /// </summary>
        protected void GoogleLoginButton_Click(object sender, EventArgs e)
        {
            Response.Redirect(gAuth.GetAutenticationURI().ToString());
        }
    }
}