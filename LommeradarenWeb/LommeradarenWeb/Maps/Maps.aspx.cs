using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Net;
using Newtonsoft.Json;

namespace LommeradarenWeb
{
    public partial class Maps : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            var json = new WebClient().DownloadString("http://test.shiprep.no/shiprepwebuisys/api/NearbyShips?latitude=63.4385841&longitude=10.4007685&altitude=0.0&radius=50");
            //System.Diagnostics.Debug.WriteLine(json);
            
            //JsonConvert.
            //JsonConvert.DeserializeObject<List<Marker>>(json);
            

        }
    }
}