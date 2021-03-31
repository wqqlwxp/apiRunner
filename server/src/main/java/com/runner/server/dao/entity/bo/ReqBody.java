package com.runner.server.dao.entity.bo;

import java.util.List;


public class ReqBody {
    private String type; //  参数类型
    private List<ReqHeader> data; //form-data值
    private String jsonData;  //json值

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ReqHeader> getData() {
        return data;
    }

    public void setData(List<ReqHeader> data) {
        this.data = data;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
