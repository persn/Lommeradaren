using LommeradarenWeb.db;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.UI.WebControls;
using System.Web.UI;
using System.Web.Script.Serialization;
using System.Diagnostics;


namespace LommeradarenWeb.users
{
    public partial class Gallery : System.Web.UI.Page
    {
        private LommeradarDBEntities entities;
        private List<string[]> images;
        private int selectedBigImage;

        protected void Page_Load(object sender, EventArgs e)
        {
            entities = new LommeradarDBEntities();
            int userId = (from Users in entities.Users where Users.UserName.Equals(Context.User.Identity.Name) select Users.UserID).FirstOrDefault();
            int[] ids = (from Pictures in entities.Pictures where Pictures.UserUserID == userId select Pictures.PictureID).ToArray();
            images = new List<string[]>(); //0=id, 1=filename
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
            try
            {

                if (!infoTable.Visible)
                {
                    infoTable.Visible = true;
                }
                ImageButton img = (ImageButton)sender;
                BigImage.ImageUrl = img.ImageUrl;
                int imageID = int.Parse(img.ID);
                selectedBigImage = imageID;
                JavaScriptSerializer js = new JavaScriptSerializer();
                imageData imgData = js.Deserialize<imageData>((from Pictures in entities.Pictures where Pictures.PictureID == imageID select Pictures.ExifData).FirstOrDefault());
                updateLabels(imgData);
                if (BigImage.Height.Value > BigImage.Width.Value)
                {
                    BigImage.Height = 300;
                }
                else
                {
                    BigImage.Width = 600;
                }
            }
            catch (Exception exc)
            {
                Debug.WriteLine(exc.Message);
            }

        }

        protected void DeleteImageButton_Click(object sender, EventArgs e)
        {
            try
            {
                int id = int.Parse(BigImage.ImageUrl.Split('=')[1]);
                Pictures pic = (from Pictures in entities.Pictures where Pictures.PictureID == id select Pictures).FirstOrDefault();
                entities.Pictures.Remove(pic);
                entities.SaveChanges();
                Response.Redirect("Gallery.aspx");
            }
            catch (Exception exc)
            {
                Debug.WriteLine(exc.Message);
            }
        }

        protected void ViewLargeImageButton_Click(object sender, EventArgs e)
        {
            Response.Redirect(BigImage.ImageUrl);
        }
        private void updateLabels(imageData imgData)
        {
            LatitudeLabel.Text = "<b>Latitude: </b>" + imgData.lat;
            LongitudeLabel.Text = "<b>Longitude: </b>" + imgData.lng;
            ElevationLabel.Text = "<b>Elevation: </b>" + imgData.alt;
            ImoLabel.Text = "<b>Imo: </b>" + imgData.imo;
            MMSILabel.Text = "<b>MMSI: </b>" + imgData.mmsi;
            SpeedLabel.Text = "<b>Speed: </b>" + imgData.speed;
            PositionTimeLabel.Text = "<b>PositionTime: </b>" + imgData.positionTime;
            WebsiteLabel.Text = "<b>Website: </b>" + imgData.webpage;
        }
    }
    public class imageData
    {
        public string id, name, lat, lng, alt, mmsi, distance, has_detail_page, webpage, positionTime, imo, speed, course;
    }
}
