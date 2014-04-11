using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LommeradarenWeb
{
    public class GoogleUser
    {
        public string access_token, token_type, id_token, aud, iss, email_verified, at_hash, azp, email, sub;
        public int expires_in, exp, iat;

        public GoogleUser(gLoginInfo gli, gLoginClaims glc)
        {
            this.access_token = gli.access_token;
            this.token_type = gli.token_type;
            this.id_token = gli.id_token;
            this.expires_in = gli.expires_in;
            this.aud = glc.aud;
            this.iss = glc.iss;
            this.email_verified = glc.email_verified;
            this.at_hash = glc.at_hash;
            this.azp = glc.azp;
            this.email = glc.email;
            this.sub = glc.sub;
            this.exp = glc.exp;
            this.iat = glc.iat;
        }
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