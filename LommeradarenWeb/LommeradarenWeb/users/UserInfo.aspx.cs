using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using LommeradarenWeb.db;

namespace LommeradarenWeb.users
{
    public partial class UserInfo : System.Web.UI.Page
    {
        private Users currentUser;
        private LommeradarDBEntities entities;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (User.Identity.IsAuthenticated)
            {
                entities = new LommeradarDBEntities();
                currentUser = (from user in entities.Users where user.UserName.Equals(User.Identity.Name) select user).First();
                emailLabel.Text = "Your eMail is: " + currentUser.UserEmail;
                nameLabel.Text = "Logged in as: " + currentUser.UserName;
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
                if (Crypto.VerifyHashedPassword(currentUser.UserPassword, oldPasswordField.Text) && newPasswordField.Text.Equals(confirmNewPasswordField.Text))
                {
                    currentUser.UserPassword = Crypto.HashPassword(newPasswordField.Text);
                    entities.SaveChanges();
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
                    currentUser.UserEmail = newEmailField.Text;
                    entities.SaveChanges();
                    ResetEmailFields();
                    emailLabel.Text = "Your eMail is: " + currentUser.UserEmail;
                }
            }
        }
    }
}