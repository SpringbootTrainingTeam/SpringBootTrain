package com.hsbc.springboot.util;

/**
 * <p> Function: </p>
 *
 * @author : zhangsunjiankun 2018/6/24 下午12:33
 */
public class CommonUtil {

    private CommonUtil() {}

    /**
     * 二进制转换十六进制
     *
     * @param bytes 二进制数组
     * @return 十六进制字符串
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte aBytes : bytes) {
            String hex = Integer.toHexString(aBytes & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    /**
     * 十六进制转换二进制
     *
     * @param hex 十六进制字符串
     * @return 二进制数组
     */
    public static byte[] hex2Byte(String hex) {
        int binary = 2;
        int hexadecimal = 16;
        if (hex.length() < 1) {
            return new byte[0];
        }
        byte[] result = new byte[hex.length() / binary];
        for(int i = 0; i < hex.length() / binary; i++) {
            int high = Integer.parseInt(hex.substring(i * binary, i * binary +1), hexadecimal);
            int low = Integer.parseInt(hex.substring(i * binary + 1, i * binary + binary), hexadecimal);
            result[i] = (byte) (high * hexadecimal + low);
        }
        return result;
    }

    /**
     * 校验手机号码的格式是否正确
     *
     * @param mobile 手机号码
     * @return 校验结果
     */
    public static boolean checkMobile(String mobile) {
        return mobile.matches("[1][34578][\\d]{9}");
    }

}
