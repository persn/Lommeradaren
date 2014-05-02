using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Logic;

namespace LommeradarenWeb.users
{
    public partial class UserInfo : System.Web.UI.Page
    {
        private UserController userAuth = new UserController();
        protected void Page_Load(object sender, EventArgs e)
        {
            if (User.Identity.IsAuthenticated)
            {
                emailLabel.Text = "Your eMail is: " + userAuth.getUserEmail(User.Identity.Name);
                nameLabel.Text = "Logged in as: " + User.Identity.Name;
            }
        }

        protected void ResetPasswordFieldButton_Click(object sender, EventArgs e)
        {
            oldPasswordField.Text = null;
            newPasswordField.Text = null;
            confirmNewPasswordField.Text = null;
        }

        protected void ChangePasswordButton_Click(object sender, EventArgs e)
        {
            if (oldPasswordField.Text != null && newPasswordField != null)
            {
                if (userAuth.veryfiPassword(oldPasswordField.Text, User.Identity.Name) && newPasswordField.Text.Equals(confirmNewPasswordField.Text))
                {
                    userAuth.setNewPassword(newPasswordField.Text, User.Identity.Name);
                }
            }
        }


        protected void ResetEmailFieldsButton_Click(object sender, EventArgs e)
        {
            ResetEmailFields();
        }

        private void ResetEmailFields()
        {
            newEmailField.Text = null;
            confirmNewEmailField.Text = null;
        }

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