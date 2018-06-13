package com.massky.sraumzigbee;

import java.util.Arrays;

/**
 *
 * CRC数组处理工具类及数组合并
 */

public class CrcUtil {

    /**
     * 为Byte数组最后添加两位CRC校验
     *
     * @param buf （验证的byte数组）
     * @param crc
     * @return
     */
    public static String setParamCRC(byte[] buf, int crc) {
        int checkCode = 0;
        checkCode = crc_16_CCITT_False(buf, buf.length,crc);
//        byte[] crcByte = new byte[2];
//        crcByte[0] = (byte) ((checkCode >> 8) & 0xff);
//        crcByte[1] = (byte) (checkCode & 0xff);
//        // 将新生成的byte数组添加到原数据结尾并返回
//        return concatAll(buf, crcByte);
        //10进制化为16进制：String Long.toHexString(Long v);
      return  Integer.toHexString(checkCode);
    }

    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     *
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     *
     * @param bytes
     * @param length
     * @return
     */
    public static int crc_16_CCITT_False(byte[] bytes, int length,int crc) {
//        int crc = 0xffff; // initial value //权重
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        //输出String字样的16进制
        String strCrc = Integer.toHexString(crc).toUpperCase();
        System.out.println(strCrc);
        return crc;
    }

    /***
     * CRC校验是否通过
     *
     * @param srcByte
     * @param length(验证码字节长度)
     * @return
     */
    public static boolean isPassCRC(byte[] srcByte, int length,int crc) {

        // 取出除crc校验位的其他数组，进行计算，得到CRC校验结果
        int calcCRC = calcCRC(srcByte, 0, srcByte.length - length,crc);
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((calcCRC >> 8) & 0xff);
        bytes[1] = (byte) (calcCRC & 0xff);

        // 取出CRC校验位，进行计算
        int i = srcByte.length;
        byte[] b = { srcByte[i - 2] ,srcByte[i - 1] };

        // 比较
        return bytes[0] == b[0] && bytes[1] == b[1];
    }

    /**
     * 对buf中offset以前crcLen长度的字节作crc校验，返回校验结果
     * @param  buf
     * @param crcLen
     */
    private static int calcCRC(byte[] buf, int offset, int crcLen,int crc) {
        int start = offset;
        int end = offset + crcLen;
//        int crc = 0xffff; // initial value//权重-更改
        int polynomial = 0x1021;
        for (int index = start; index < end; index++) {
            byte b = buf[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    /**
     * 多个数组合并
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}