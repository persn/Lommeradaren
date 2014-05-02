using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Security;
using Logic;

namespace LommeradarenWeb
{
    /// <summary>
    /// Authenticates users based on http response from googles oauth servers
    /// </summary>
    public partial class GoogleResponse : System.Web.UI.Page
    {
        private UserController userAuth = new UserController();
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Context.Request.QueryString.Count > 0)
            {
                string code = Context.Request.QueryString["code"];
                string userName;
                if (userAuth.validateGoogleUser(code, out userName))
                {
                    FormsAuthentication.RedirectFromLoginPage(userName,
                    true);
                }
                else
                {
                    //ERROR
                }
            }
        }
    }
}