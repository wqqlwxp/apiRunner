package com.runner.server.dao.entity.bo;

import java.util.Map;


public class PageObject {
    private int offset; //分页码
    private int limit; //分页数
    private Map<String,Object> data;  //数据

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
