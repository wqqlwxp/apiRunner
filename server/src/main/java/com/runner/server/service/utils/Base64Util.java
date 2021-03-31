package com.runner.server.service.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/2/27 下午8:31
 */
public class Base64Util {

    /**
     * @description 加密
     * @author 星空梦语
     * @date 2021/2/27 下午8:33
     */
    public static String encrypt(String str) {
        String encodeBase64String = Base64.encodeBase64String(str.getBytes());
        return encodeBase64String;
    }


    /**
     * @description 解密
     * @author 星空梦语
     * @date 2021/2/27 下午8:33
     */
    public static String decrypt(String s) {
        byte[] decodeBase64 = Base64.decodeBase64(s);
        s=new String(decodeBase64);
        return s;
    }

    public static void main(String[] args) {
        String a ="{\"status\":true,\"message\":\"\",\"text\":\"阿斯蒂芬撒旦法，大师傅（撒旦法）+55pa555qE6Zi\\/5pav6JKC6Iqsc2Fk\"}";
        String base64 = encrypt(a);
        System.out.println("加密后值："+base64);
        String fromBase64 = decrypt(base64);
        System.out.println("解密后："+fromBase64);
    }
}
