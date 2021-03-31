package com.runner.server.service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.HttpResponseData;
import com.runner.server.dao.entity.bo.ReqCaseData;
import com.runner.server.dao.entity.bo.ReqHeader;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


public class HttpClientUtil {


    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:10
     */
    public static HttpResponseData postData(ReqCaseData reqCaseData)  {
        String charset="utf-8";
        String type=reqCaseData.getReqInterface().getInterfaceType();
        String ip=reqCaseData.getEnvMachine().getIp();
        String port=reqCaseData.getEnvMachine().getPort();
        String url=reqCaseData.getReqInterface().getInterfaceUrl();
        url=String.format("%s://%s:%s/%s",type,ip,port,url);
        LogUtil.info("post请求链接="+url);
        HttpPost httpPost=new HttpPost(url);
        HttpResponse response=null;
        CloseableHttpClient httpClient=null;
        HttpResponseData httpResponseData=new HttpResponseData();
        try{
            if(type.equals("https")){
                httpClient=new SSLClient();
            }else{
                httpClient = HttpClients.custom().build();
            }
            //设置请求参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(Integer.MAX_VALUE)
                    .setConnectTimeout(Integer.MAX_VALUE).build();
            httpPost.setConfig(requestConfig);

            //设置请求头
            if(reqCaseData.getReqHeaders().size()>0){
                for(ReqHeader reqHeader:reqCaseData.getReqHeaders()){
                    if(StringUtils.isNotEmpty(reqHeader.getKey())){
                        httpPost.addHeader(reqHeader.getKey(),reqHeader.getValue());
                    }
                }
            }
            if(reqCaseData.getReqBodys().getType().equals("formData")){
                //设置消息体
                List<BasicNameValuePair> list=new ArrayList<>();
                for(ReqHeader body:reqCaseData.getReqBodys().getData()){
                    if(StringUtils.isNotEmpty(body.getKey())){
                        list.add(new BasicNameValuePair(body.getKey(),body.getValue()));
                    }
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }else if(reqCaseData.getReqBodys().getType().equals("json")){
                StringEntity entity = new StringEntity(reqCaseData.getReqBodys().getJsonData(), charset);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");
            }
            response = httpClient.execute(httpPost);
            if(response != null){
                httpResponseData.setRespHeaders(response.getAllHeaders());
                httpResponseData.setRespCode(response.getStatusLine().getStatusCode());
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    httpResponseData.setRespText(EntityUtils.toString(resEntity,charset));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            httpResponseData.setRespText(e.getMessage());
        }finally {
            if(response!=null){
                EntityUtils.consumeQuietly(response.getEntity());
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                httpResponseData.setRespText(e.getMessage());
            }
        }
        LogUtil.info("接口响应结果："+httpResponseData.getRespText());
        LogUtil.info("接口响应headers："+JSON.toJSONString(httpResponseData.getRespHeaders()));
        return httpResponseData;
    }


    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:10
     */
    public static HttpResponseData getData(ReqCaseData reqCaseData)  {
        String charset="utf-8";
        String type=reqCaseData.getReqInterface().getInterfaceType();
        String ip=reqCaseData.getEnvMachine().getIp();
        String port=reqCaseData.getEnvMachine().getPort();
        String url=reqCaseData.getReqInterface().getInterfaceUrl();
        url=String.format("%s://%s:%s/%s",type,ip,port,url);
        LogUtil.info("get请求链接="+url);
        HttpGet httpGet=null;
        HttpResponse response=null;
        CloseableHttpClient httpClient=null;
        HttpResponseData httpResponseData=new HttpResponseData();
        try{
            if(type.equals("https")){
                httpClient=new SSLClient();
            }else{
                httpClient = HttpClients.custom().build();
            }

            if(!reqCaseData.getReqBodys().getType().equals("json")){
                //设置消息体
                List<BasicNameValuePair> list=new ArrayList<>();
                for(ReqHeader body:reqCaseData.getReqBodys().getData()){
                    if(StringUtils.isNotEmpty(body.getKey())){
                        list.add(new BasicNameValuePair(body.getKey(),body.getValue()));
                    }
                }
                String params = EntityUtils.toString(new UrlEncodedFormEntity(list,charset));
                if(StringUtils.isNotBlank(params)){
                    httpGet=new HttpGet(url+"?"+params);
                }else{
                    httpGet=new HttpGet(url);
                }
            }else if(reqCaseData.getReqBodys().getType().equals("json")){
                httpResponseData.setRespText("json格式数据，暂不支持get请求！");
                return httpResponseData;
            }

            //设置请求参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(Integer.MAX_VALUE)
                    .setConnectTimeout(Integer.MAX_VALUE).build();
            httpGet.setConfig(requestConfig);

            //设置请求头
            if(reqCaseData.getReqHeaders().size()>0){
                for(ReqHeader reqHeader:reqCaseData.getReqHeaders()){
                    if(StringUtils.isNotEmpty(reqHeader.getKey())){
                        httpGet.addHeader(reqHeader.getKey(),reqHeader.getValue());
                    }
                }
            }

            response = httpClient.execute(httpGet);
            if(response != null){
                httpResponseData.setRespHeaders(response.getAllHeaders());
                httpResponseData.setRespCode(response.getStatusLine().getStatusCode());
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    httpResponseData.setRespText(EntityUtils.toString(resEntity,charset));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            httpResponseData.setRespText(e.getMessage());
        }finally {
            if(response!=null){
                EntityUtils.consumeQuietly(response.getEntity());
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                httpResponseData.setRespText(e.getMessage());
            }
        }
        LogUtil.info("接口响应结果："+httpResponseData.getRespText());
        LogUtil.info("接口响应headers："+JSON.toJSONString(httpResponseData.getRespHeaders()));
        return httpResponseData;
    }

}
