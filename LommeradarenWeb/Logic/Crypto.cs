using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Security.Cryptography;

namespace Logic
{
    /// <summary>
    /// Contains functions to create and verify hashed passwords
    /// </summary>
    public class Crypto
    {
        private const int PBKDF2ITERCOUNT = 1000; // default for Rfc2898DeriveBytes
        private const int PBKDF2SUBKEYLENGTH = 256 / 8; // 256 bits
        private const int SALTSIZE = 128 / 8; // 128 bits

        /// <summary>
        /// Hashes and salts the input string
        /// </summary>
        /// <param name="password">String to hash</param>
        /// <returns></returns>
        public static string HashPassword(string password)
        {
            byte[] salt;
            byte[] subkey;
            using (var deriveBytes = new Rfc2898DeriveBytes(password, SALTSIZE, PBKDF2ITERCOUNT))
            {
                salt = deriveBytes.Salt;
                subkey = deriveBytes.GetBytes(PBKDF2SUBKEYLENGTH);
            }

            byte[] outputBytes = new byte[1 + SALTSIZE + PBKDF2SUBKEYLENGTH];
            Buffer.BlockCopy(salt, 0, outputBytes, 1, SALTSIZE);
            Buffer.BlockCopy(subkey, 0, outputBytes, 1 + SALTSIZE, PBKDF2SUBKEYLENGTH);
            return Convert.ToBase64String(outputBytes);
        }
        /// <summary>
        /// Hashes and salts the password parameter then checks if it corresponds with the hashed password from the hashedPassword parameter
        /// </summary>
        /// <param name="hashedPassword">Already hashed password to check</param>
        /// <param name="password">String to check</param>
        /// <returns></returns>
        public static bool VerifyHashedPassword(string hashedPassword, string password)
        {
            byte[] hashedPasswordBytes = Convert.FromBase64String(hashedPassword);

            // Wrong length or version header.
            if (hashedPasswordBytes.Length != (1 + SALTSIZE + PBKDF2SUBKEYLENGTH) || hashedPasswordBytes[0] != 0x00)
                return false;

            byte[] salt = new byte[SALTSIZE];
            Buffer.BlockCopy(hashedPasswordBytes, 1, salt, 0, SALTSIZE);
            byte[] storedSubkey = new byte[PBKDF2SUBKEYLENGTH];
            Buffer.BlockCopy(hashedPasswordBytes, 1 + SALTSIZE, storedSubkey, 0, PBKDF2SUBKEYLENGTH);

            byte[] generatedSubkey;
            using (var deriveBytes = new Rfc2898DeriveBytes(password, salt, PBKDF2ITERCOUNT))
            {
                generatedSubkey = deriveBytes.GetBytes(PBKDF2SUBKEYLENGTH);
            }
            return storedSubkey.SequenceEqual(generatedSubkey);
        }
    }


}