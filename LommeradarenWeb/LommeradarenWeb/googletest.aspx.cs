using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace LommeradarenWeb
{
    public partial class googletest : System.Web.UI.Page
    {
        private Authentication auth = new Authentication();
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Context.Request.QueryString.Count > 0)
            {
                String code = Context.Request.QueryString["code"];
                String res = auth.logintestthingy(code);
                Label0.Text = "Response code: " + code;
                Label1.Text = "result: " + res;
                Label2.Text = "Authenticated: " + User.Identity.IsAuthenticated;
                //Label3.Text = "Id token: " + ;
                //Label4.Text = "Expires in: " + ;
            }
        }

        protected void loginGoogleButton_Click(object sender, EventArgs e)
        {
            Response.Redirect(auth.GetAutenticationURI().ToString());
        }

       
    }
}