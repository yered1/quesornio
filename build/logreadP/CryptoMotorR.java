/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package logreadP;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 */
public class CryptoMotorR {

    CryptoMotorR(byte[] salt, String password, byte[] ivBytes) {
        this.saltBytes = salt;
        this.password = password;
        this.ivBytes = ivBytes;
    }
    CryptoMotorR(String password) throws NoSuchAlgorithmException{
            this.saltBytes = generateSalt();
            this.password = password;
    }

    /**
     * @return the saltBytes
     */
    public byte[] getSaltBytes() {
        return saltBytes;
    }
    
    public  String getSaltBytesAsB64() {
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    /**
     * @param aSaltBytes the saltBytes to set
     */
    public  void setSaltBytes(byte[] aSaltBytes) {
        saltBytes = aSaltBytes;
    }

    private String password;
    private byte[] saltBytes;
    private static int pswdIterations = 1;
    private static int keySize = 128;
    private byte[] ivBytes;
    
    
    
    
    public String encrypt(String plainText) throws Exception {

        //get salt
        
        //byte[] saltBytes = salt.getBytes("UTF-8");

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(
                getPassword().toCharArray(), getSaltBytes(),
                pswdIterations,
                keySize
        );

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        //encrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        setIvBytes(params.getParameterSpec(IvParameterSpec.class).getIV());
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        //return new Base64().encodeAsString(encryptedTextBytes);
        //String asB64 = Base64.getEncoder().encodeToString("some string".getBytes("utf-8"));
        //System.out.println(asB64); // Output will be: c29tZSBzdHJpbmc=
        String B64 = Base64.getEncoder().encodeToString(encryptedTextBytes);
        return B64;
    }

    //expects input in base64
    @SuppressWarnings("static-access")
    public String decrypt(String encryptedText) throws Exception {

        //byte[] saltBytes = salt.getBytes("UTF-8");

        //byte[] encryptedTextBytes = new Base64.decode(encryptedText);
        //byte[] asBytes = Base64.getDecoder().decode("c29tZSBzdHJpbmc=");
        //System.out.println(new String(asBytes, "utf-8")); // And the output is: some string
        byte[] encryptedTextBytes = Base64.getDecoder().decode(encryptedText);
        //byte[] encryptedTextBytes = encryptedText;

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(
                getPassword().toCharArray(), getSaltBytes(),
                pswdIterations,
                keySize
        );

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // Decrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(getIvBytes()));

        byte[] decryptedTextBytes = null;
        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return new String(decryptedTextBytes);
    }

    /*public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        String s = new String(bytes);
        return s;
    }*/
    
    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        // VERY important to use SecureRandom instead of just Random
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        // Generate a 16 byte (128 bit) salt
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the ivBytes
     */
    public byte[] getIvBytes() {
        return ivBytes;
    }
    
    public  String getIvBytesAsB64() {
        return Base64.getEncoder().encodeToString(ivBytes);
    }

    /**
     * @param ivBytes the ivBytes to set
     */
    public void setIvBytes(byte[] ivBytes) {
        this.ivBytes = ivBytes;
    }
}
