package com.runner.server.dao.entity.po;

public class ZkConfig {
    private Integer id;

    private String zkAlias;

    private String zkIp;

    private String zkPassword;

    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZkAlias() {
        return zkAlias;
    }

    public void setZkAlias(String zkAlias) {
        this.zkAlias = zkAlias;
    }

    public String getZkIp() {
        return zkIp;
    }

    public void setZkIp(String zkIp) {
        this.zkIp = zkIp;
    }

    public String getZkPassword() {
        return zkPassword;
    }

    public void setZkPassword(String zkPassword) {
        this.zkPassword = zkPassword;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}