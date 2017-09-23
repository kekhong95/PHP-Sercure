package com.example.kekho.myapplication.encryto;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.example.kekho.myapplication.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Extension;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by kekho on 9/18/2017.
 */

public class CriptoKey {
    Context context;
    public static byte[] publicKey;
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public CriptoKey(Context context) {
        this.context = context;
        publicKey = Base64.decode(context.getString(R.string.app_server_key),Base64.DEFAULT);
    }
    public byte[] encrytion(byte[] input) {
        try {
            byte[] keyBytes = publicKey;
            X509EncodedKeySpec spec
                    = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey key = kf.generatePublic(spec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.PUBLIC_KEY, key);
            byte[] encryto = cipher.doFinal(input);
            return encryto;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public byte[] decrytion(byte[] input,int n,byte[] keyBytes) {
        try {
            PKCS8EncodedKeySpec spec
                    = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey key = kf.generatePrivate(spec);
            Cipher decode = Cipher.getInstance(CIPHER_ALGORITHM);
            decode.init(Cipher.PRIVATE_KEY, key);
            return decode.doFinal(input,0,n);
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public byte[] getEncode(String values,byte[] key){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key,"AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,keySpec);
            byte[] encode = cipher.doFinal(values.getBytes());
            return encode;
        }
        catch (Exception e){
            return null;
        }
    }
    public byte[] getDecode(byte[] values,byte[] key){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key,"AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,keySpec);
            byte[] encode = values;
            byte[] decode = cipher.doFinal(encode);
            return decode;
        }
        catch (Exception e){
            Log.d("Error : ",e.getMessage());
            return null;
        }
    }
    public static String hash256(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public byte[] getMd5(String sdk){
        try {
            MessageDigest digital =  MessageDigest.getInstance("MD5");
            return digital.digest(sdk.getBytes());
        } catch (Exception ex) {
            return null;
        }
    }
    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }
}
