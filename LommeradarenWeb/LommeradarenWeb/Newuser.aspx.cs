using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Logic;

namespace LommeradarenWeb
{
    /// <summary>
    /// Handles creation of new user accounts
    /// </summary>
    public partial class Newuser : System.Web.UI.Page
    {
        UserController userAuth = new UserController();

        protected void Page_Load(object sender, EventArgs e)
        {
        }
        /// <summary>
        /// Creates a new user, or an error response if something goes wrong
        /// </summary>
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
        /// <summary>
        /// Resets the input and output fields on the page
        /// </summary>
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