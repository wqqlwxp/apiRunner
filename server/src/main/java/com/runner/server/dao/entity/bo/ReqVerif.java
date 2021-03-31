package com.runner.server.dao.entity.bo;

import java.util.List;


public class ReqVerif {

    private List<VerifData> jsonData;  //验证json格式数据

    private String data;  //验证全部数据

    private String type;  //验证类型


    public List<VerifData> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<VerifData> jsonData) {
        this.jsonData = jsonData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
