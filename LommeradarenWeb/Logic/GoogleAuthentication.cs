using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Script.Serialization;

namespace Logic
{
    /// <summary>
    /// Contains functions to work with the google apis and oauth authentication
    /// </summary>
    public class GoogleAuthentication
    {
        private const String client_id = "413624543866-kailen70lui2e56nufddv72is61qr29e.apps.googleusercontent.com";
        private const String client_secret = "ROxpdlouZOJraKkFYAyEx7qT";
        private const String redirect_url = "http://localhost:2026/GoogleResponse.aspx";
        private const String scope = "https://www.googleapis.com/auth/plus.profile.emails.read";

        public Uri GetAutenticationURI()
        {
            List<String> postData = new List<String>();
            postData.Add("client_id=" + client_id);
            postData.Add("redirect_uri=" + redirect_url);
            postData.Add("scope=" + scope);
            postData.Add("response_type=code");
            return new Uri("https://accounts.google.com/o/oauth2/auth" + "?" + string.Join("&", postData.ToArray()));
        }

        /// <summary>
        /// Creates a user based on the return code from the request from googles login servers
        /// </summary>
        /// <param name="code">Response code from google</param>
        /// <returns></returns>
        public GoogleUser GoogleLogin(string code)
        {
            string grant_type = "authorization_code";
            string gurl = "code=" + code + "&client_id=" + client_id +
            "&client_secret=" + client_secret + "&redirect_uri=" + redirect_url + "&grant_type=" + grant_type;
            return (POSTResult(gurl));
        }

        /// <summary>
        /// Decodes a json string and creates a user to work with the uploadhandler for the mobile application
        /// </summary>
        /// <param name="token">Json string to parse</param>
        /// <returns></returns>
        public MobileUser CheckMobileToken(string token)
        {
            string[] tokenArray = token.Split(new Char[] { '.' });
            JavaScriptSerializer js = new JavaScriptSerializer();
            MobileUser mu = js.Deserialize<MobileUser>(base64Decode(tokenArray[1]));
            return mu;
        }

        /// <summary>
        /// Handles login via googles oauth service
        /// </summary>
        /// <param name="gurl">The url to use when sending requests to google</param>
        /// <returns></returns>
        private GoogleUser POSTResult(string gurl)
        {
            try
            {
                // variables to store parameter values
                string url = "https://accounts.google.com/o/oauth2/token";

                // creates the post data for the POST request
                string postData = (gurl);

                string googleAuth = DoPostRequest(url, postData);

                //process JSON array
                JavaScriptSerializer js = new JavaScriptSerializer();
                gLoginInfo gli = js.Deserialize<gLoginInfo>(googleAuth);

                string[] tokenArray = gli.id_token.Split(new Char[] { '.' });
                gLoginClaims glc = js.Deserialize<gLoginClaims>(base64Decode(tokenArray[1]));

                

                string userUrl = "https://www.googleapis.com/plus/v1/people/me?access_token=" + gli.access_token;

                string gUser = DoGetRequest(userUrl);
                System.Diagnostics.Debug.WriteLine(gUser);
                GoogleUser glu = js.Deserialize<GoogleUser>(gUser);

                return glu;
                //return "authResponse: " + googleAuth + "infoResponse: " + base64Decode(tokenArray[1]);
            }
            catch (Exception exc)
            {
                throw new Exception("Error in Postresult: " + exc.Message);
            }

        }

        /// <summary>
        /// Starts a http post request to the given url with the given data
        /// </summary>
        /// <param name="url">Where to send the request</param>
        /// <param name="data">Which parameters to use</param>
        /// <returns></returns>
        private string DoPostRequest(string url, string data)
        {
            try
            {
            // create the POST request
            HttpWebRequest webRequest = (HttpWebRequest)WebRequest.Create(url);
            webRequest.Method = "POST";
            webRequest.ContentType = "application/x-www-form-urlencoded";
            webRequest.ContentLength = data.Length;

            // POST the data
            using (StreamWriter requestWriter2 = new StreamWriter(webRequest.GetRequestStream()))
            {
                requestWriter2.Write(data);
            }

            //This actually does the request and gets the response back
            HttpWebResponse resp = (HttpWebResponse)webRequest.GetResponse();

            string response;

            using (StreamReader responseReader = new StreamReader(webRequest.GetResponse().GetResponseStream()))
            {
                //dumps the HTML from the response into a string variable
                response = responseReader.ReadToEnd();
            }
            return response;
            }
                 catch (Exception exc)
            {
                throw new Exception("Error in DoPostRequest: " + exc.Message);
            }
        }

        /// <summary>
        /// Sends a http get request to the given url
        /// </summary>
        /// <param name="url">Where to send the request</param>
        /// <returns></returns>
        private string DoGetRequest(string url)
        {
            try
            {
                // create the GET request
                HttpWebRequest webRequest = (HttpWebRequest)WebRequest.Create(url);
                HttpWebResponse resp = (HttpWebResponse)webRequest.GetResponse();

                string response;

                using (StreamReader responseReader = new StreamReader(webRequest.GetResponse().GetResponseStream()))
                {
                    //dumps the HTML from the response into a string variable
                    response = responseReader.ReadToEnd();
                }

                return response;
            }
            catch (Exception exc)
            {
                throw new Exception("Error in DoGetRequest: " + exc.Message);
            }
        }

        /// <summary>
        /// Decodes a base64 string into a readable string
        /// </summary>
        /// <param name="data">Data to decode</param>
        /// <returns></returns>
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