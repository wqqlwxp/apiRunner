package com.runner.server.service.utils;

import com.alibaba.fastjson.JSON;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * @author 星空梦语
 * @desc
 * @date 2020/9/17 16:49
 */
public  class ConverterUtil {

    /**
     * @description  获取数值
     * @author 星空梦语
     * @date 2021/3/9 下午4:04
     */
    public static Number getAsNumber(Object obj) {
        if (obj != null) {
            if (obj instanceof Number) {
                return (Number) obj;
            } else if (obj instanceof String) {
                try {
                    String text = (String) obj;
                    return NumberFormat.getInstance().parse(text);
                } catch (ParseException e) {
                    LogUtil.err("格式转换异常，异常信息："+e.getMessage());
                }
            }
        }
        return null;
    }


    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:05
     */
    public static  Boolean  isBoolean(Object object){
        object=getAsBoolean(object);
        return object instanceof Boolean;
    }



    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:05
     */
    public static Boolean getAsBoolean(Object obj) {
        if (obj != null) {
            if (obj instanceof Boolean) {
                return true;
            } else if (obj instanceof String) {
                try{
                    Boolean.valueOf(obj.toString());
                    return true;
                }catch (Exception e){
                    return  null;
                }
            }
        }
        return null;
    }

    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:05
     */
    public static boolean isJSON(String str) {
        boolean result = false;
        try {
            Object obj= JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result=false;
        }
        return result;
    }

    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:05
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}
