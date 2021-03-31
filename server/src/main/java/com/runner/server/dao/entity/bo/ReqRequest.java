package com.runner.server.dao.entity.bo;

import com.runner.server.dao.entity.po.TestCase;

import java.util.List;

public class ReqRequest {
    private String id;  //用例id
    private String projectCode;  //用例所属项目
    private String moduleCode;   //用例所属模块
    private String env;   //用例单独执行时所属环境
    private TestCase reqPre;  //用例前置数据
    private List<ReqCaseData> reqCaseDatas;  //用例明细数据
    private String planID; //测试计划执行id


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public TestCase getReqPre() {
        return reqPre;
    }

    public void setReqPre(TestCase reqPre) {
        this.reqPre = reqPre;
    }


    public List<ReqCaseData> getReqCaseDatas() {
        return reqCaseDatas;
    }

    public void setReqCaseDatas(List<ReqCaseData> reqCaseDatas) {
        this.reqCaseDatas = reqCaseDatas;
    }

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }
}
