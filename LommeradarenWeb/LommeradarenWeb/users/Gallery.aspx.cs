using LommeradarenWeb.db;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.UI.WebControls;


namespace LommeradarenWeb.users
{
    public partial class Gallery : System.Web.UI.Page
    {
        private LommeradarDBEntities entities;
        private List<string[]> images;

        protected void Page_Load(object sender, EventArgs e)
        {
            entities = new LommeradarDBEntities();
            int userId = (from Users in entities.Users where Users.UserName.Equals(Context.User.Identity.Name) select Users.UserID).FirstOrDefault();
            int[] ids = (from Pictures in entities.Pictures where Pictures.UserUserID == userId select Pictures.PictureID).ToArray();
            images = new List<string[]>(); //0=id, 1=filename,2=rawdata
            for (int i = 0; i < ids.Length; i++)
            {
                int id = ids[i];
                string[] temp = { id.ToString(), (from Pictures in entities.Pictures where Pictures.PictureID == id select Pictures.PictureName).FirstOrDefault() };
                images.Add(temp);
            }
            fillTable();
        }

        public void fillTable()
        {
            foreach (string[] s in getImages())
            {
                TableRow row = new TableRow();
                TableCell cell1 = new TableCell();
                TableCell cell2 = new TableCell();
                Label label = new Label();
                Image img = new Image();
                label.Text = s[1];
                row.Cells.Add(cell1);
                cell1.Controls.Add(label);
                row.Cells.Add(cell2);
                img.ImageUrl= "\\GalleryHandler.ashx?id=" + s[0];
                cell2.Controls.Add(img);
                imageTable.Rows.Add(row);
            }

        }

        public List<string[]> getImages()
        {
            return images;
        }

    }
}