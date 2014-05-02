using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data;

namespace Logic
{
    /// <summary>
    /// Contains functions for saving images to the database
    /// </summary>
    public class UploadController
    {
        private DBConnection dataConnection = new DBConnection();

        /// <summary>
        /// saves an image to the database
        /// </summary>
        /// <param name="rawData">The raw byte data for the image file</param>
        /// <param name="exif">The json string containing our image information</param>
        /// <param name="userEmail">Email of the user uploading the image</param>
        /// <param name="fileName">Filename of the image</param>
        public void saveImage(byte[] rawData, string exif, string userEmail, string fileName)
        {
            dataConnection.saveImage(rawData, exif, userEmail, fileName);
        }
    }
}
