using Data;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;

namespace Logic
{
    /// <summary>
    /// Contains functions to handle images, such as getting the raw files from the database as well as saving new ones
    /// </summary>
    public class GalleryController
    {
        private DBConnection dataConnection = new DBConnection();
        private GoogleAuthentication gAuth = new GoogleAuthentication();

        public void getPicture(int fileID, out string fileName, out byte[] rawData)
        {
            fileName = dataConnection.getPictureFileName(fileID);
            rawData = dataConnection.getPictureRawData(fileID);
            return;
        }
        /// <summary>
        /// returns a list of image ids and corresponding filenames for a given user
        /// </summary>
        /// <param name="userName">User to get pictures for</param>
        /// <returns></returns>
        public List<string[]> getPictures(string userName)
        {
            return dataConnection.getAllUserPictures(userName);
        }
        /// <summary>
        /// returns the image data saved in the database
        /// </summary>
        /// <param name="id">Image id to grab data from</param>
        /// <returns></returns>
        public ImageData getImageData(int id)
        {
            JavaScriptSerializer js = new JavaScriptSerializer();
            return js.Deserialize<ImageData>(dataConnection.getExifData(id));
        }
        public bool deleteImage(int id)
        {
                return dataConnection.deleteImage(id);
        }

    }

    public class ImageData
    {
        public string id, name, lat, lng, alt, mmsi, distance, has_detail_page, webpage, positionTime, imo, speed, course;
    }
}
