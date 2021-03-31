package com.runner.server.service.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/2/27 下午8:19
 */
public class DesUtil {
    public static final String KEY = "12345678";//这个密码因子必须8位


    /**
     * @description  解密
     * @author 星空梦语
     * @date 2021/2/27 下午8:28
     */
    public static String decrypt(String message,String key) throws Exception {
        LogUtil.info("DES开始解密，解密密文："+message+" ;解密key："+key);
        byte[] bytesrc =parseHexStr2Byte(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }


    /**
     * @description 加密
     * @author 星空梦语
     * @date 2021/2/27 下午8:29
     */
    public static String encrypt(String message, String key) throws Exception {
        LogUtil.info("DES开始加密，密文："+message+" ;key："+key);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return parseByte2HexStr(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }



    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        String value="{\"status\":true,\"message\":\"\",\"text\":\"阿斯蒂芬撒旦法，大师傅（撒旦法）+55pa555qE6Zi\\/5pav6JKC6Iqsc2Fk\"}";
        System.out.println("加密数据:"+value);
        String a=encrypt(value, KEY);
        System.out.println("加密后的数据为:"+a);

        String b=decrypt(a,KEY);
        System.out.println("解密后的数据:"+b);

    }




}
