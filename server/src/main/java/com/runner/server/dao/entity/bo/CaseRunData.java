package com.runner.server.dao.entity.bo;

import com.runner.server.dao.entity.po.CaseLog;

import java.util.List;


public class CaseRunData {
    private List<CaseLog> caseLogList;  //用例执行过程中的执行记录
    private HttpResponseData httpResponseData;  // 接口响应数据

    public List<CaseLog> getCaseLogList() {
        return caseLogList;
    }

    public void setCaseLogList(List<CaseLog> caseLogList) {
        this.caseLogList = caseLogList;
    }

    public HttpResponseData getHttpResponseData() {
        return httpResponseData;
    }

    public void setHttpResponseData(HttpResponseData httpResponseData) {
        this.httpResponseData = httpResponseData;
    }
}
