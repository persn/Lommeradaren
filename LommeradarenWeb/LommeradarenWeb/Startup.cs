using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(LommeradarenWeb.Startup))]
namespace LommeradarenWeb
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
