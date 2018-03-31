package com.aldroid.opencamera.util;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(toKey(initVector).getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(toKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeToString(encrypted, Base64.DEFAULT));

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(toKey(initVector).getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(toKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String toKey(String key){
        if(key == null){
            return "0123456789123456";
        }else{
            if(key.length() > 16){
                return key.substring(0, 16);
            }else{
                int sa = 16 - key.length();
                String resp = key;
                while(sa > 0){
                    resp = resp + "0";
                    sa--;
                }
                return resp;
            }
        }
    }

}