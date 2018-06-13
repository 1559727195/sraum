package com.Util;

import static android.R.attr.data;
import static java.lang.Integer.parseInt;

/**
 * Created by zhu on 2017/6/29.
 */

public class ByteUtils {

    private static int crc;

    /**
     * 16进制字符串转换为byte[]
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase().replace(" ", "");
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * byte[]转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src,int length) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null ||length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 初始化UDP - CRC权值
     * @param CRC_privew
     */
    public static byte[] init_crc_weight_udp(String CRC_privew) {
//            String crc_s = TextUtils.isEmpty(et_send_value1.getText().toString().trim()) ? "7101" : et_send_value1.getText().toString().trim();
        String crc_s = "A5C9";//A5C9为UDP广播的CRC校验值
        //7101为权值
        //将字符串的CRC校验权值转化为int 型 ---0x7101
//        int a = 28929;//要转换的数!
//        int l=(a&0x00ff)<<8;
//        int h=(a&0xff00)>>8;
//        a=h|l;

        crc =   parseInt(crc_s, 16);//java标签（label）求16进制字符串的整数和 把一个整数转为4个16进制字符表示
//        crc =   Integer.parseInt("7101", 16);
        String stringContent1 = "983030381FFD247C91";//16进制字符串


//        int length = stringContent1.length();
//        String CRC= stringContent1.substring(length - 6,length -2).trim();//拿到CRC值
//        //拿到STX
//        String STX = stringContent1.substring(0,2);//拿到STX
//        String ADDR = stringContent1.substring(2,4);//ADDR
//
//        String  ETX = stringContent1.substring(length - 2,length);//ETX
        return hexStringToBytes (CRC_privew);
    }


    public static String testCrc(byte[] data) {//添加修改权值的控件

        //AA 0C 01 00 01 00 00 04 05 17 05 01 A0 86 01 00
        //结果为：F2E3
        return CrcUtil.setParamCRC(data,crc);
//        if (CrcUtil.isPassCRC(crcData, 2,crc)) {
//            System.out.println("验证通过");
//        } else {
//            System.out.println("验证失败");
//        }
    }
}
