package com.runner.server.dao.entity.bo;

import org.apache.commons.httpclient.Header;
import org.springframework.util.ObjectUtils;


public class HttpResponseData {
    private Object respText;  //响应消息体
    private Object respHeaders;  //响应头
    private int respCode; //响应状态码




    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public Object getRespHeaders() {
        return respHeaders;
    }

    public void setRespHeaders(Object respHeaders) {
        this.respHeaders = respHeaders;
    }

    public Object getRespText() {
        return respText;
    }

    public void setRespText(Object respText) {
        this.respText = respText;
    }
}
