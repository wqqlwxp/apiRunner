package com.runner.server.dao.entity.bo;

public class ReqHeader {

    private String key;  //请求header 名称

    private String value; //请求header 值


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
