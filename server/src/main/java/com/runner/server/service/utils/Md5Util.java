package com.runner.server.service.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/2/27 下午8:35
 */
public class Md5Util {


    /**
     * @description 加密
     * @author 星空梦语
     * @date 2021/2/27 下午8:57
     */
    public static String sign(String text, String key) {
        LogUtil.info("MD5开始加密，密文："+text+" ;key："+key);
        return DigestUtils.md5Hex(getContentBytes(text + key, "UTF-8"));
    }

    /**
     * 验证
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, "UTF-8"));
        return mysign.equals(sign);
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    public static void main(String[] args) {
        String value = "{\"status\":true,\"message\":\"\",\"text\":\"水电费阿斯蒂芬，发+55pa555qE6Zi\\/5pav6JKC6Iqsc2Fk\"}";
        String key="2312ghgjhhgghjgg423";
        String data=Md5Util.sign(value,key);
        System.out.println("加密后："+data);
        System.out.println("验证后："+Md5Util.verify(value,data,key));

    }
}
