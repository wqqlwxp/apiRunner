package com.runner.server.dao.entity.po;

public class ZkData {
    private Integer id;

    private Integer zkId;

    private String zkAlias;

    private String appName;

    private String interfaceName;

    private String methodName;

    private String version;

    private String group;

    private String address;

    private String paramTypes;

    private String paramDatas;

    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZkId() {
        return zkId;
    }

    public void setZkId(Integer zkId) {
        this.zkId = zkId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(String paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String getParamDatas() {
        return paramDatas;
    }

    public void setParamDatas(String paramDatas) {
        this.paramDatas = paramDatas;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getZkAlias() {
        return zkAlias;
    }

    public void setZkAlias(String zkAlias) {
        this.zkAlias = zkAlias;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}