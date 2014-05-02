﻿using Logic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LommeradarenWeb.users
{
    /// <summary>
    /// Summary description for GalleryHandler
    /// </summary>
    public class GalleryHandler : IHttpHandler
    {
        private GalleryController galleryHandler = new GalleryController();
        public void ProcessRequest(HttpContext context)
        {
            int fileID = int.Parse(context.Request.QueryString["id"]);
            string filename;
            byte[] rawData;
            galleryHandler.getPicture(fileID, out filename, out rawData);
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