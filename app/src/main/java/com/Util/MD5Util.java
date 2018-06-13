package com.Util;

import java.security.MessageDigest;

/**
 * Created by masskywcy on 2016-12-29.
 */
//用于MD5加密
public class MD5Util {
    public static String md5(String str) {
        String digest = null;
        StringBuffer buffer = new StringBuffer();
        try {
            MessageDigest digester = MessageDigest.getInstance("md5");
            byte[] digestArray = digester.digest(str.getBytes());
            for (int i = 0; i < digestArray.length; i++) {
                buffer.append(String.format("%02x", digestArray[i]));
            }
            digest = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;
    }


}
