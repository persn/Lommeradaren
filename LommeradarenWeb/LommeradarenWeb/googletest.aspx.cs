using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using LommeradarenWeb.db;
using System.Web.Security;

namespace LommeradarenWeb
{
    public partial class googletest : System.Web.UI.Page
    {
        private Authentication auth = new Authentication();
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Context.Request.QueryString.Count > 0)
            {
                string code = Context.Request.QueryString["code"];
                GoogleUser res = auth.GoogleLogin(code);
                ValidateUser(res);
                //Label0.Text = "Response code: " + code;
                //Label1.Text = "result: " + res;
                //Label2.Text = "Authenticated: " + User.Identity.IsAuthenticated;
                //Label3.Text = "Test: " + auth.test();
                //Label4.Text = "Expires in: " + ;
            }
        }
        private void ValidateUser(GoogleUser glu)
        {
            LommeradarDBEntities entities = new LommeradarDBEntities();
            Users users = new Users();
            try
            {
                var u = entities.Users.Where(user => user.UserGoogleId == glu.id);
                if (u.Any())
                {
                    FormsAuthentication.RedirectFromLoginPage(glu.displayName,
                    true);
                }
                else
                {
                    var newUser = entities.Set<Users>();
                    newUser.Add(new Users { UserName = glu.displayName, UserGoogleId = glu.id, UserEmail = glu.getAccountEmail() });
                    entities.SaveChanges();
                    Label0.Text = "Logged in as: " + glu.displayName + "\nemail: "+ glu.getAccountEmail();
                    FormsAuthentication.RedirectFromLoginPage(glu.displayName,
                    true);
                }

            }
            catch (Exception exc)
            {
                System.Diagnostics.Debug.WriteLine(exc.Message);
            }
        }
    }
}