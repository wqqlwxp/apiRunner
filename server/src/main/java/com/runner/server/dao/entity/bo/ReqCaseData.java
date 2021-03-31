package com.runner.server.dao.entity.bo;

import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.dao.entity.po.TestCase;

import java.util.List;

public class ReqCaseData {
    private String  tabIndex;  //用例接口维度展示tab对应index
    private int zkId;
    private String zkApp;
    private String zkInterface;
    private String zkMethod;
    private ReqInterface reqInterface;  //接口请求消息体
    private List<ReqHeader> reqHeaders;  //接口请求header
    private ReqBody reqBodys;  //接口请求消息体
    private ReqVerif reqVerifs;  //接口参数校验
    private EnvMachine envMachine;  //用例执行环境
    private String projectCode;   //用例所属项目
    private String moduleCode;  //用例所属模块


    public String getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    public ReqInterface getReqInterface() {
        return reqInterface;
    }

    public void setReqInterface(ReqInterface reqInterface) {
        this.reqInterface = reqInterface;
    }

    public List<ReqHeader> getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(List<ReqHeader> reqHeaders) {
        this.reqHeaders = reqHeaders;
    }



    public ReqBody getReqBodys() {
        return reqBodys;
    }

    public void setReqBodys(ReqBody reqBodys) {
        this.reqBodys = reqBodys;
    }

    public ReqVerif getReqVerifs() {
        return reqVerifs;
    }

    public void setReqVerifs(ReqVerif reqVerifs) {
        this.reqVerifs = reqVerifs;
    }

    public EnvMachine getEnvMachine() {
        return envMachine;
    }

    public void setEnvMachine(EnvMachine envMachine) {
        this.envMachine = envMachine;
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

    public int getZkId() {
        return zkId;
    }

    public void setZkId(int zkId) {
        this.zkId = zkId;
    }

    public String getZkApp() {
        return zkApp;
    }

    public void setZkApp(String zkApp) {
        this.zkApp = zkApp;
    }

    public String getZkInterface() {
        return zkInterface;
    }

    public void setZkInterface(String zkInterface) {
        this.zkInterface = zkInterface;
    }

    public String getZkMethod() {
        return zkMethod;
    }

    public void setZkMethod(String zkMethod) {
        this.zkMethod = zkMethod;
    }
}
