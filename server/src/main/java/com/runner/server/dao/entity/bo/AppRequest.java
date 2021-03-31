package com.runner.server.dao.entity.bo;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/3/23 下午4:14
 */
public class AppRequest {
    private int zkId;
    private String zkAddress;
    private String appName;
    private String dependency;


    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public int getZkId() {
        return zkId;
    }

    public void setZkId(int zkId) {
        this.zkId = zkId;
    }
}
