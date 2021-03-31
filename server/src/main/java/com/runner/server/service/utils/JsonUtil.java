package com.runner.server.service.utils;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;

/**
 * @author 星空梦语
 * @desc
 * @date 2020/9/18 19:55
 */

public class JsonUtil {

    /**
     * @description  jsonpath解析数据
     * @author 星空梦语
     * @date 2021/3/1 下午8:07
     */
    public static  String jsonPathRead(String value,String path){
        if(StringUtils.isBlank(path)){
            return  value;  //加解密密文路径为空时，返回原参
        }
        String data="";
        try{
            Object resp= JsonPath.read(value,path);
            if(resp instanceof String) {
                data= (String) resp;
            }else if(resp instanceof net.minidev.json.JSONArray){
                net.minidev.json.JSONArray array= (net.minidev.json.JSONArray) resp;
                if(array.size()==0){  //未解析到密文数据时，返回原参
                    return value;
                }
                data=array.get(0).toString();
            }else{
                data=String.valueOf(resp);
            }
            return  data;
        }catch (Exception e){
            LogUtil.err("jsonpath解析变量:"+path+" 异常,"+e.getMessage());
            return  "jsonpath解析变量:"+path+" 异常,"+e.getMessage();
        }
    }


}
