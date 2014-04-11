using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Script.Serialization;

namespace LommeradarenWeb
{
    public class Authentication
    {
        private const String client_id = "413624543866-kailen70lui2e56nufddv72is61qr29e.apps.googleusercontent.com";
        private const String client_secret = "ROxpdlouZOJraKkFYAyEx7qT";
        private const String redirect_url = "http://localhost:2026/googletest.aspx";
       // private const String scope = "https://www.googleapis.com/auth/plus.profile.emails.read";
       // private const String scope = "https://www.googleapis.com/auth/plus.login";
        private const String scope = "profile";

        public Uri GetAutenticationURI()
        {
            List<String> postData = new List<String>();
            postData.Add("client_id=" + client_id);
            postData.Add("redirect_uri=" + redirect_url);
            postData.Add("scope=" + scope);
            postData.Add("response_type=code");
            return new Uri("https://accounts.google.com/o/oauth2/auth" + "?" + string.Join("&", postData.ToArray()));
        }

        public String logintestthingy(String code)
        {
            string grant_type = "authorization_code";
            string gurl = "code=" + code + "&client_id=" + client_id +
            "&client_secret=" + client_secret + "&redirect_uri=" + redirect_url + "&grant_type=" + grant_type;
            return (POSTResult(gurl));
        }

        private String POSTResult(string e)
        {
            try
            {
                // variables to store parameter values
                string url = "https://accounts.google.com/o/oauth2/token";

                // creates the post data for the POST request
                string postData = (e);

                // create the POST request
                HttpWebRequest webRequest = (HttpWebRequest)WebRequest.Create(url);
                webRequest.Method = "POST";
                webRequest.ContentType = "application/x-www-form-urlencoded";
                webRequest.ContentLength = postData.Length;

                // POST the data
                using (StreamWriter requestWriter2 = new StreamWriter(webRequest.GetRequestStream()))
                {
                    requestWriter2.Write(postData);
                }

                //This actually does the request and gets the response back
                HttpWebResponse resp = (HttpWebResponse)webRequest.GetResponse();

                string googleAuth;

                using (StreamReader responseReader = new StreamReader(webRequest.GetResponse().GetResponseStream()))
                {
                    //dumps the HTML from the response into a string variable
                    googleAuth = responseReader.ReadToEnd();
                }
                
                //process JSON array
                JavaScriptSerializer js = new JavaScriptSerializer();
                gLoginInfo gli = js.Deserialize<gLoginInfo>(googleAuth);

                string[] tokenArray = gli.id_token.Split(new Char[] { '.' });
                gLoginClaims glc = js.Deserialize<gLoginClaims>(base64Decode(tokenArray[1]));

                GoogleUser glu = new GoogleUser(gli, glc);

               // return "email: "+glc.email+" email verified: "+glc.email_verified;
                return "authResponse: "+googleAuth +"infoResponse: "+ base64Decode(tokenArray[1]);
            }
            catch (Exception exc)
            {
                throw new Exception("Error in Postresult: " + exc.Message);
            }
           
        }

        private string base64Decode(string data)
        {
            //add padding with '=' to string to accommodate C# Base64 requirements
            int strlen = data.Length + (4 - (data.Length % 4));
            char pad = '=';
            string datapad;

            if (strlen == (data.Length + 4))
            {
                datapad = data;
            }
            else
            {
                datapad = data.PadRight(strlen, pad);
            }

            try
            {
                System.Text.UTF8Encoding encoder = new System.Text.UTF8Encoding();
                System.Text.Decoder utf8Decode = encoder.GetDecoder();

                // create byte array to store Base64 string
                byte[] todecode_byte = Convert.FromBase64String(datapad);
                int charCount = utf8Decode.GetCharCount(todecode_byte, 0, todecode_byte.Length);
                char[] decoded_char = new char[charCount];
                utf8Decode.GetChars(todecode_byte, 0, todecode_byte.Length, decoded_char, 0);
                string result = new String(decoded_char);
                return result;
            }
            catch (Exception e)
            {
                throw new Exception("Error in base64Decode: " + e.Message);
            }
        }
    }
}