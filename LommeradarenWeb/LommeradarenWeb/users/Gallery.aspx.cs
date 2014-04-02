using LommeradarenWeb.db;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.UI.WebControls;
using System.Web.UI;


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
            TableRow row = new TableRow();
            
            for (int i = 0; i < images.Count(); i++)
            {
                TableCell cell1 = new TableCell();
                Label label = new Label();
                ImageButton img = new ImageButton();
                img.ImageUrl = "GalleryHandler.ashx?id=" + images[i][0];
                img.Height = 150;
                img.ID = images[i][0];
                img.Click += new ImageClickEventHandler(onImageClick);
                cell1.Controls.Add(img);
                cell1.Controls.Add(new LiteralControl("<br/>"));
                label.Text = images[i][1];
                cell1.Controls.Add(label);
                row.Cells.Add(cell1);
            }
            imageTable.Rows.Add(row);
        }

        public void onImageClick(object sender, EventArgs e)
        {
            if (!infoTable.Visible)
            {
                infoTable.Visible = true;
            }
            ImageButton img = (ImageButton)sender;
            BigImage.ImageUrl = img.ImageUrl;
            if (BigImage.Height.Value > BigImage.Width.Value)
            {
                BigImage.Height = 300;
            }
            else
            {
                BigImage.Width = 600;
            }
        }

        protected void DeleteImageButton_Click(object sender, EventArgs e)
        {
            int id = int.Parse(BigImage.ImageUrl[BigImage.ImageUrl.Length - 1].ToString());
            try
            {
                Pictures pic = (from Pictures in entities.Pictures where Pictures.PictureID == id select Pictures).First();
                entities.Pictures.Remove(pic);
                entities.SaveChanges();
            }
            catch
            {
                return;
            }
            
            Response.Redirect("Gallery.aspx");
            
        }

        protected void ViewLargeImageButton_Click(object sender, EventArgs e)
        {
            Response.Redirect(BigImage.ImageUrl);
        }
    }
}