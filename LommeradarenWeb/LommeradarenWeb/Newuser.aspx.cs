using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using LommeradarenWeb.db;

namespace LommeradarenWeb
{
    public partial class Newuser : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void registerNewUserButton_Click(object sender, EventArgs e)
        {
            LommeradarDBEntities entities = new LommeradarDBEntities();
            Users users = new Users();
            try
            {
                var u = entities.Users.Where(usr => usr.UserName == usernameBox.Text);
                if (u.Any())
                {
                    infobox.Text = "Username already in use";
                    return;
                }
                var m = entities.Users.Where(usr => usr.UserEmail == emailBox.Text);
                if (m.Any())
                {
                    infobox.Text = "Email already in use";
                    return;
                }
                    var newUser = entities.Set<Users>();
                    newUser.Add(new Users { UserName = usernameBox.Text, UserEmail = emailBox.Text, UserPassword = Crypto.HashPassword(passwordBox.Text) });
                    entities.SaveChanges();
                    clear();
            }
            catch (Exception exc)
            {
                System.Diagnostics.Debug.WriteLine(exc.Message);
            }
        }

        private void clear()
        {
            infobox.Text = null;
            usernameBox.Text = null;
            emailBox.Text = null;
            passwordBox.Text = null;
            confirmPasswordBox.Text = null;
        }
    }
}