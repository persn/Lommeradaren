using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LommeradarenWeb
{
    public class GoogleUser
    {
        public string id, displayName;
        public List<emails> emails;

        public string getAccountEmail(){
            foreach(emails e in emails){
                if(e.type.Equals("account")){
                    return e.value;
                }
            }
            return "not_found";
        }
    }

    public class MobileUser
    {
        public string iss, id, sub, azp, email, aud, cid, iat, exp;
        public bool email_verified, verified_email;

        public string toString()
        {
            string res = "iss: " + iss;
            res += "\nid: " + id;
            res += "\nsub: " + sub;
            res += "\nazp: " + azp;
            res += "\nemail: " + email;
            res += "\naud: " + aud;
            res += "\ncid: " + cid;
            res += "\niat: " + exp;
            res += "\nemail_verified: " + email_verified;
            res += "\nverified_email: " + verified_email;
            return res;
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

    public class emails
    {
        public string value, type;
    }
}