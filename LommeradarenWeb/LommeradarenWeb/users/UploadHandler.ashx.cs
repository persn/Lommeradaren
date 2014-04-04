using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using LommeradarenWeb.db;

namespace LommeradarenWeb.users
{
    /// <summary>
    /// Summary description for UploadHandler
    /// </summary>
    public class UploadHandler : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            LommeradarDBEntities entities = new LommeradarDBEntities();
            if (context.Request.Files.Count > 0)
            {

                foreach (HttpPostedFile file in context.Request.Files)
                {                
                    string pictureName = context.Request.QueryString["pictureName"];
                    Pictures picture = new Pictures();
                    int imgSize = file.ContentLength;
                    byte[] img = new byte[imgSize];
                    file.InputStream.Read(img, 0, imgSize);
                    picture.FileName = file.FileName;
                    picture.Picture = img;
                    picture.PictureName = pictureName;
                    entities.Pictures.Add(picture);
                }
                entities.SaveChanges();
            }
            context.Response.ContentType = "text/plain";
            context.Response.Write(context.Request.QueryString["pictureName"]);
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