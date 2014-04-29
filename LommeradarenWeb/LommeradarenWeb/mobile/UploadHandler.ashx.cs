using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using LommeradarenWeb.db;
using System.Collections.Specialized;
using System.IO;
using System.Diagnostics;

namespace LommeradarenWeb.users
{
    /// <summary>
    /// Summary description for UploadHandler
    /// </summary>
    public class UploadHandler : IHttpHandler
    {
        public void ProcessRequest(HttpContext context)
        {
            context.Request.SaveAs("c:\\temp\\HttpRequest.txt", true);
            string token = context.Request.QueryString["token"];
            if (!token.Equals(""))
            {
                LommeradarDBEntities entities = new LommeradarDBEntities();
                Authentication auth = new Authentication();
                try
                {
                    MobileUser mu = auth.CheckMobileToken(token);
                    bool userInDB = entities.Users.Where(user => user.UserEmail == mu.email).Any();

                    if (context.Request.Files.Count > 0 && userInDB)
                    {
                        HttpFileCollection hfc = context.Request.Files;
                        Pictures picture = new Pictures();
                        bool imgDone = false;
                        bool dataDone = false;
                        foreach (string name in hfc)
                        {
                            if (name.Equals("image"))
                            {
                                HttpPostedFile file = hfc[name];
                                int imgSize = file.ContentLength;
                                byte[] img = new byte[imgSize];
                                file.InputStream.Read(img, 0, imgSize);
                                picture.FileName = file.FileName;
                                picture.PictureName = file.FileName;
                                picture.Picture = img;
                                imgDone = true;
                            }
                            if (name.Equals("image-data"))
                            {
                                string rawData = new StreamReader(hfc[name].InputStream).ReadToEnd();
                                picture.UserUserID = (from user in entities.Users where user.UserEmail.Equals(mu.email) select user.UserID).First();
                                picture.ExifData = rawData;
                                dataDone = true;
                                    }
                                }
                                if (dataDone && imgDone)
                                {
                                entities.Pictures.Add(picture);
                                entities.SaveChanges();
                            }
                        
                    }
                    context.Response.ContentType = "text/plain";
                    context.Response.Write("upload successfull");
                }
                catch (Exception e)
                {
                    Debug.WriteLine(e.Message);
                }
            }
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}

