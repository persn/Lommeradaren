using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data;
using System.Diagnostics;

namespace Logic
{
    /// <summary>
    /// Contains functions for verifying and authenticating users
    /// </summary>
    public class UserController
    {
        private DBConnection dataConnection = new DBConnection();
        private GoogleAuthentication gAuth = new GoogleAuthentication();

        public bool isMobileUserInDB(string token, out string userEmail)
        {
            GoogleAuthentication gAuth = new GoogleAuthentication();
            MobileUser mu = gAuth.CheckMobileToken(token);
            userEmail = mu.email;
            return dataConnection.userEmailInDB(mu.email);
        }

        /// <summary>
        /// Checks if the given username and password combination exists in the database
        /// </summary>
        /// <param name="userName"></param>
        /// <param name="passWord"></param>
        /// <returns></returns>
        public bool ValidateUserLogin(string userName, string passWord)
        {
            bool userValid = false;
            string hashedPW = dataConnection.getHashedPw(userName);
            userValid = Crypto.VerifyHashedPassword(hashedPW, passWord);
            return userValid;
        }

        public void setNewPassword(string newPassword, string userName)
        {
            dataConnection.setUserPassword(Crypto.HashPassword(newPassword), userName);
        }
        public string getUserEmail(string userName)
        {
            return dataConnection.getUserEmail(userName);
        }
        public void setNewEmail(string userName, string newEmail)
        {
            dataConnection.setUserEmail(newEmail, userName);
        }

        /// <summary>
        /// Verifies a user based on the response code from googles oauth servers
        /// </summary>
        /// <param name="code">Response code from google</param>
        /// <param name="userName">Returns the users username from the response code</param>
        /// <returns></returns>
        public bool validateGoogleUser(string code, out string userName)
        {
            GoogleUser glu = gAuth.GoogleLogin(code);
            if (dataConnection.userGoogleIDInDB(glu.id))
            {
                userName = glu.displayName;
                return true;
            }
            else
            {
                if (dataConnection.addNewUserToDB(glu.displayName, glu.getAccountEmail(), null, glu.id))
                {
                    userName = glu.displayName;
                    return true;
                }
                else
                {
                    userName = "";
                    return false;
                }
            }
        }

        /// <summary>
        /// Adds a new user to the database
        /// </summary>
        /// <param name="userName"></param>
        /// <param name="email"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        public int registerNewUser(string userName, string email, string password)
        {

            if (dataConnection.userNameInDB(userName))
            {
                return 1;
            }
            else if (dataConnection.userEmailInDB(email))
            {
                return 2;
            }
            else
            {
                if (dataConnection.addNewUserToDB(userName, email, Crypto.HashPassword(password), null))
                {
                    return 3;
                }
                else
                {
                    return 4;
                }
            }
        }
    }
}
