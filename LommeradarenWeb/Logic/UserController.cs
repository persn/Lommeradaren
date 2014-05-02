using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data;
using System.Diagnostics;

namespace Logic
{
    public class UserController
    {
        private DBConnection dataConnection = new DBConnection();
        private GoogleAuthentication gAuth = new GoogleAuthentication();

        public bool ValidateUserLogin(string userName, string passWord)
        {
            bool userValid = false;
            string hashedPW = dataConnection.getHashedPw(userName);
            userValid = Crypto.VerifyHashedPassword(hashedPW, passWord);
            return userValid;
        }

        public bool veryfiPassword(string password, string userName)
        {
            return Crypto.VerifyHashedPassword(dataConnection.getHashedPw(userName), password);
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
