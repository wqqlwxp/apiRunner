package com.runner.server.service.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/2/27 下午7:33
 */
public class AesUtil {

    public static final String KEY = "625202f9149e061d";//这个密码因子必须16位


    /**
     * @description  加密
     * @author 星空梦语
     * @date 2021/2/27 下午8:28
     */
    public static String encrypt(String input, String key,String iv) throws Exception {
        LogUtil.info("AES开始加密，密文："+input+" ;key："+key);
        byte[] crypted = null;
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher=null;
        IvParameterSpec ivText=null;
        if(StringUtils.isNotBlank(iv)){
            ivText = new IvParameterSpec(iv.getBytes());
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey,ivText);
        }else{
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
        }
        crypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
//        return new String(Base64.encodeBase64(crypted));
        return parseByte2HexStr(crypted);
    }


    /**
     * @description 解密
     * @author 星空梦语
     * @date 2021/2/27 下午8:28
     */
    public static String decrypt(String input, String key,String iv) throws Exception {
        LogUtil.info("AES开始解密，密文："+input+" ;key："+key);
        byte[] decryptText = parseHexStr2Byte(input);
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        byte[] output = null;
        IvParameterSpec ivText = null;
        Cipher cipher=null;
        if(StringUtils.isNotBlank(iv)){
            ivText = new IvParameterSpec(iv.getBytes());
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey,ivText);
        }else{
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
        }
//        output = cipher.doFinal(Base64.decodeBase64(input.getBytes()));
        output = cipher.doFinal(decryptText);
        return new String(output);
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
        String data = "asassadfasdfsa";
        String text=AesUtil.encrypt(data, KEY,"5efd3f6060e20330");
        System.out.println(AesUtil.decrypt(text, KEY,"5efd3f6060e20330"));


    }

}
