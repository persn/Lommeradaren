﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Security;
using LommeradarenWeb.db;

namespace LommeradarenWeb
{
    public partial class Login : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        protected void Button1_Click(object sender, EventArgs e)
        {
            if (ValidateUser(txtUserName.Value, txtUserPass.Value))
                FormsAuthentication.RedirectFromLoginPage(txtUserName.Value,
                chkPersistCookie.Checked);
            else
                Response.Redirect("Login.aspx", true);
        }
        private bool ValidateUser(String userName, String passWord)
        {
            bool userValid = false;
            using (LommeradarDBEntities entities = new LommeradarDBEntities())
            {
                try
                {
                    string hashedPW = (from user in entities.Users where user.UserName.Equals(userName) select user.UserPassword).First();
                    userValid = Crypto.VerifyHashedPassword(hashedPW, passWord);
                }
                catch (Exception e)
                {
                    
                }
            }
            return userValid;
        }
    }
}