using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.UI.WebControls;
using System.Web.UI;
using System.Web.Script.Serialization;
using System.Diagnostics;
using Logic;


namespace LommeradarenWeb.users
{
    /// <summary>
    /// Contains functions handling our gallery page
    /// </summary>
    public partial class Gallery : System.Web.UI.Page
    {
        private List<string[]> images;
        private int selectedBigImage;
        private GalleryController gController = new GalleryController();

        /// <summary>
        /// Reads all the images contained in the database owned by the currently logged in user and displays them on the page
        /// </summary>
        protected void Page_Load(object sender, EventArgs e)
        {
            images = gController.getPictures(User.Identity.Name);
            fillTable();
        }

        /// <summary>
        /// Places the images from Page_Load into a scrollable field for easy overview
        /// </summary>
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

        /// <summary>
        /// Shows the selected image in more detail
        /// </summary>
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
                ImageData imgData = gController.getImageData(imageID);
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

        /// <summary>
        /// Deletes the currently selected image
        /// </summary>
        protected void DeleteImageButton_Click(object sender, EventArgs e)
        {
                int id = int.Parse(BigImage.ImageUrl.Split('=')[1]);
                gController.deleteImage(id);
                Response.Redirect("Gallery.aspx");
        }

        /// <summary>
        /// Returns the actual imagefile that is currently displayed in detail view
        /// </summary>
        protected void ViewLargeImageButton_Click(object sender, EventArgs e)
        {
            Response.Redirect(BigImage.ImageUrl);
        }
        /// <summary>
        /// Updates the text in the labels in detail view for the selected image based on the supplied imagedata
        /// </summary>
        /// <param name="imgData">Data to update the labels from</param>
        private void updateLabels(ImageData imgData)
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
    
}
