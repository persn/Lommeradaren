using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Collections.Specialized;
using System.IO;
using System.Diagnostics;
using Logic;

namespace LommeradarenWeb.users
{
    /// <summary>
    /// Handles uploading files via http requests
    /// </summary>
    public class UploadHandler : IHttpHandler
    {
        public void ProcessRequest(HttpContext context)
        {
            string token = context.Request.QueryString["token"];
            if (!token.Equals(""))
            {
                UploadController uController = new UploadController();
                UserController usrController = new UserController();
                string userEmail;
                try
                {
                    if (context.Request.Files.Count == 2 && usrController.isMobileUserInDB(token, out userEmail))
                    {
                        HttpFileCollection hfc = context.Request.Files;
                        bool imgDone = false;
                        bool dataDone = false;
                        byte[] img = new byte[0];
                        string exif = "";
                        string fileName = "";
                        foreach (string name in hfc)
                        {
                            if (name.Equals("image"))
                            {
                                HttpPostedFile file = hfc[name];
                                int imgSize = file.ContentLength;
                                img = new byte[imgSize];
                                fileName = file.FileName;
                                file.InputStream.Read(img, 0, imgSize);
                                imgDone = true;
                            }
                            if (name.Equals("image-data"))
                            {
                                exif = new StreamReader(hfc[name].InputStream).ReadToEnd();
                                dataDone = true;
                            }
                        }
                        if (dataDone && imgDone)
                        {
                            uController.saveImage(img, exif, userEmail, fileName);
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

