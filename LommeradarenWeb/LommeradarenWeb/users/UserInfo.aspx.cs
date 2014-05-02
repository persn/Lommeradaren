using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Logic;

namespace LommeradarenWeb.users
{
    /// <summary>
    /// Displays basic information about the current user as well as allowing the user to change the registered email/password
    /// </summary>
    public partial class UserInfo : System.Web.UI.Page
    {
        private UserController userAuth = new UserController();

        /// <summary>
        /// Shows the current users username and email on their labels
        /// </summary>
        protected void Page_Load(object sender, EventArgs e)
        {
            emailLabel.Text = "Your eMail is: " + userAuth.getUserEmail(User.Identity.Name);
            nameLabel.Text = "Logged in as: " + User.Identity.Name;
        }

        /// <summary>
        /// blanks the inputs related to password change
        /// </summary>
        protected void ResetPasswordFieldButton_Click(object sender, EventArgs e)
        {
            oldPasswordField.Text = null;
            newPasswordField.Text = null;
            confirmNewPasswordField.Text = null;
        }

        /// <summary>
        /// Handles the password change
        /// </summary>
        protected void ChangePasswordButton_Click(object sender, EventArgs e)
        {
            if (oldPasswordField.Text != null && newPasswordField != null)
            {
                if (userAuth.ValidateUserLogin(User.Identity.Name, oldPasswordField.Text) && newPasswordField.Text.Equals(confirmNewPasswordField.Text))
                {
                    userAuth.setNewPassword(newPasswordField.Text, User.Identity.Name);
                }
            }
        }

        /// <summary>
        /// resets the input fields related to email change
        /// </summary>
        protected void ResetEmailFieldsButton_Click(object sender, EventArgs e)
        {
            newEmailField.Text = null;
            confirmNewEmailField.Text = null;
        }

        /// <summary>
        /// Handles the email change
        /// </summary>
        protected void ChangeEmailButton_Click(object sender, EventArgs e)
        {
            if (newEmailField.Text != null && confirmNewEmailField != null)
            {
                if (newEmailField.Text.Equals(confirmNewEmailField.Text))
                {
                    userAuth.setNewEmail(User.Identity.Name, newEmailField.Text);
                }
            }
        }
    }
}