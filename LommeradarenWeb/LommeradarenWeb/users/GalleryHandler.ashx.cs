using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using LommeradarenWeb.db;

namespace LommeradarenWeb.users
{
    /// <summary>
    /// Summary description for GalleryHandler
    /// </summary>
    public class GalleryHandler : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            LommeradarDBEntities entities = new LommeradarDBEntities();
            int fileID = int.Parse(context.Request.QueryString["id"]);
            string filename = (from Pictures in entities.Pictures where Pictures.PictureID == fileID select Pictures.FileName).FirstOrDefault();
            byte[] rawData = (from Pictures in entities.Pictures where Pictures.PictureID == fileID select Pictures.Picture).FirstOrDefault();
            string[] split = filename.Split('.');
            string filetype = split[split.Length - 1];
            context.Response.Clear();
            context.Response.ClearContent();
            context.Response.ClearHeaders();
            context.Response.ContentType = "image/" + filetype;
            context.Response.BinaryWrite(rawData);
            context.Response.End();
        }

        public bool IsReusable
        {
            get
            {
                return true;
            }
        }
    }
}