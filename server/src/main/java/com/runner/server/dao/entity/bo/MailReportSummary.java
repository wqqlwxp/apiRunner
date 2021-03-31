package com.runner.server.dao.entity.bo;

import com.runner.server.dao.entity.po.CaseLog;

import java.util.List;

public class MailReportSummary {

    private String sys;  //测试项目

    private int total;   //执行用例总数

    private int success;  //用例成功数

    private int failed;  // 用例失败数

    private int error;  //  用例异常数

    private String startDate;  //  开始执行日期

    private String endDate;  //   结束执行日期

    private String env;  //  执行环境

    private List<CaseLog> data;  //明细



    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public List<CaseLog> getData() {
        return data;
    }

    public void setData(List<CaseLog> data) {
        this.data = data;
    }
}
