using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.IO;
using System.Drawing;
using LommeradarenWeb.db;


namespace LommeradarenWeb.users
{
    public partial class Gallery : System.Web.UI.Page
    {
        List<Bitmap> images = new List<Bitmap>();
        List<String> pictures = new List<String>();
        protected void Page_Load(object sender, EventArgs e)
        {
            LommeradarDBEntities entities = new LommeradarDBEntities();
            foreach (Pictures pic in entities.Pictures)
            {
                try
                {
                    MemoryStream stream = new MemoryStream(pic.Picture);
                    //Bitmap img = new Bitmap(stream);
                    //images.Add(img);
                    pictures.Add("data:image/jpg;base64,"+Convert.ToBase64String(stream.ToArray()));
                    
                }
                catch (Exception exc)
                {
                }
            }
            
        }

        public List<string> getPics()
        {
            return pictures;
        }
    }
}