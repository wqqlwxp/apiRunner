package com.runner.server.dao.entity.po;

import java.util.Date;

public class Cipher {
    private Integer id;

    private String projectCode;

    private String projectName;

    private String moduleName;

    private String moduleCode;

    private String type;

    private String keyData;

    private String iv;

    private String cipherPath;

    private  String requestEncrypt;

    private String responseDecrypt;

    private String executeOrder;

    private String status;

    private String operater;

    private String createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getKeyData() {
        return keyData;
    }

    public void setKeyData(String keyData) {
        this.keyData = keyData;
    }

    public String getExecuteOrder() {
        return executeOrder;
    }

    public void setExecuteOrder(String executeOrder) {
        this.executeOrder = executeOrder;
    }

    public String getCipherPath() {
        return cipherPath;
    }

    public void setCipherPath(String cipherPath) {
        this.cipherPath = cipherPath;
    }

    public String getRequestEncrypt() {
        return requestEncrypt;
    }

    public void setRequestEncrypt(String requestEncrypt) {
        this.requestEncrypt = requestEncrypt;
    }

    public String getResponseDecrypt() {
        return responseDecrypt;
    }

    public void setResponseDecrypt(String responseDecrypt) {
        this.responseDecrypt = responseDecrypt;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}