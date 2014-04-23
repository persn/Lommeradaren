using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LommeradarenWeb
{
    public class GoogleUser
    {
        public string id, displayName;
    }
    public class gLoginInfo
    {
        public string access_token, token_type, id_token;
        public int expires_in;
    }

    public class gLoginClaims
    {
        public string aud, iss, email_verified, at_hash, azp, email, sub;
        public int exp, iat;
    }
}