using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Logic;

namespace LommeradarenWeb
{
    public partial class Newuser : System.Web.UI.Page
    {
        UserController userAuth = new UserController();

        protected void Page_Load(object sender, EventArgs e)
        {
        }

        protected void registerNewUserButton_Click(object sender, EventArgs e)
        {
            switch (userAuth.registerNewUser(usernameBox.Text, emailBox.Text, passwordBox.Text))
            {
                case 1:
                    infobox.Text = "Username already in use";
                    break;
                case 2:
                    infobox.Text = "Email already in use";
                    break;
                case 3:
                    clear();
                    infobox.Text = "New user successfully created";
                    break;
                case 4:
                    infobox.Text = "Something went wrong";
                    break;
                default:
                    break;
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