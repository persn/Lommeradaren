using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data;

namespace Logic
{
    public class UploadController
    {
        private DBConnection dataConnection = new DBConnection();

        public bool isMobileUserInDB(string token, out string userEmail)
        {
            GoogleAuthentication gAuth = new GoogleAuthentication();
            MobileUser mu = gAuth.CheckMobileToken(token);
            userEmail = mu.email;
            return dataConnection.userEmailInDB(mu.email);
        }
        public void saveImage(byte[] rawData, string exif, string userEmail, string fileName)
        {
            

        }
    }
}
