using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Services;
using System.Web.Script.Services;
using System.Net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace LommeradarenWeb
{
    public partial class Maps : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {   
        }

        protected void Pre_Render(object sender, EventArgs e)
        {
        }

        protected void ShipsAvailableLbl_Load(object sender, EventArgs e)
        {
            JObject json = JObject.Parse(GetRawData());
            string status = json.GetValue("status").ToString();
            if(status != null && status.Equals("OK")){
                ShipsAvailableLbl.Text = "";
            }
            else
            {
                ShipsAvailableLbl.Text = "Ships could not be loaded, please try refreshing.";
            }
        }

        [WebMethod(EnableSession = true)]
        public static string GetRawData()
        {
            return new WebClient().DownloadString("http://test.shiprep.no/shiprepwebuisys/api/NearbyShips?latitude=63.4395831&longitude=10.4007685&altitude=0.0&radius=50000");
        }

    }
}