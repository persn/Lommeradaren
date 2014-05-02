using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data
{
    public class DBConnection
    {
        private LommeradarDBEntities entities = new LommeradarDBEntities();

        public string getHashedPw(string userName)
        {
            return (from user in entities.Users where user.UserName.Equals(userName) select user.UserPassword).First();
        }

        public void setUserPassword(string newPassword, string userName)
        {
            entities.Users.Where(usr => usr.UserName == userName).First().UserPassword = newPassword;
            entities.SaveChanges();
        }

        public List<string[]> getAllUserPictures(string userName)
        {
            int userId = (from user in entities.Users where user.UserName.Equals(userName) select user.UserID).First();
            int[] ids = (from p in entities.Pictures where p.UserUserID == userId select p.PictureID).ToArray();
            List<string[]> images = new List<string[]>(); //0=id, 1=filename
            for (int i = 0; i < ids.Length; i++)
            {
                int id = ids[i];
                string[] temp = { ids[i].ToString(), (from p in entities.Pictures where p.PictureID == id select p.PictureName).First() };
                images.Add(temp);
            }
            return images;
        }
        public bool deleteImage(int id)
        {
            try
            {
                Pictures pic = (from p in entities.Pictures where p.PictureID == id select p).First();
                entities.Pictures.Remove(pic);
                entities.SaveChanges();
                return true;
            }
            catch (Exception exc)
            {
                Debug.WriteLine(exc.Message);
                return false;
            }
        }
        public bool saveImage(byte[] rawData, string exif, string userEmail, string fileName)
        {
            try
            {
                int userID = (from user in entities.Users where user.UserEmail.Equals(userEmail) select user.UserID).First();
                var newPic = entities.Set<Pictures>();
                newPic.Add(new Pictures { Picture = rawData, ExifData = exif, UserUserID = userID, FileName = fileName });
                entities.SaveChanges();
                return true;
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                return false;
            }
        }
        public string getExifData(int imgID)
        {
            return (from p in entities.Pictures where p.PictureID == imgID select p.ExifData).First();
        }
        public bool userNameInDB(string userName)
        {
            return entities.Users.Where(usr => usr.UserName == userName).Any();
        }
        public bool userEmailInDB(string email)
        {
            return entities.Users.Where(usr => usr.UserEmail == email).Any();
        }

        public string getUserEmail(string userName)
        {
            return entities.Users.Where(usr => usr.UserName == userName).First().UserEmail;
        }

        public void setUserEmail(string newEmail, string userName)
        {
            entities.Users.Where(usr => usr.UserName == userName).First().UserEmail = newEmail;
            entities.SaveChanges();
        }
        public bool userGoogleIDInDB(string googleID)
        {
            return entities.Users.Where(usr => usr.UserGoogleId == googleID).Any();
        }
        public bool addNewUserToDB(string userName, string eMail, string hashedPw, string googleId)
        {
            try
            {
                var newUser = entities.Set<Users>();
                newUser.Add(new Users { UserName = userName, UserEmail = eMail, UserPassword = hashedPw, UserGoogleId = googleId });
                entities.SaveChanges();
                return true;
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                return false;
            }
        }
        public string getPictureFileName(int fileID)
        {
            return (from p in entities.Pictures where p.PictureID == fileID select p.FileName).First();
        }
        public byte[] getPictureRawData(int fileID)
        {
            return (from p in entities.Pictures where p.PictureID == fileID select p.Picture).First();
        }
    }
}
