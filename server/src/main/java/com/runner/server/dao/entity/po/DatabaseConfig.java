package com.runner.server.dao.entity.po;

public class DatabaseConfig {
    private Integer id;

    private String projectCode;

    private String moduleCode;

    private String projectName;

    private String moduleName;

    private String connectionAddress;

    private String connectionUser;

    private String connectionPwd;

    private String status;

    private String operater;

    private String createTime;

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

    public String getConnectionAddress() {
        return connectionAddress;
    }

    public void setConnectionAddress(String connectionAddress) {
        this.connectionAddress = connectionAddress;
    }

    public String getConnectionUser() {
        return connectionUser;
    }

    public void setConnectionUser(String connectionUser) {
        this.connectionUser = connectionUser;
    }

    public String getConnectionPwd() {
        return connectionPwd;
    }

    public void setConnectionPwd(String connectionPwd) {
        this.connectionPwd = connectionPwd;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }
}