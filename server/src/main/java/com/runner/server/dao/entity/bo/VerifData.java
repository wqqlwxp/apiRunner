package com.runner.server.dao.entity.bo;


public class VerifData {
    private String verifType; //大于，等于，小于，包含等基础校验

    private String  verifKey; //校验参数名称

    private String verifValue;  //校验预期值

    private String verifParentKey; //校验参数父节点

    private String actualValue; //校验参数实际值

    private boolean result;

    private String msg;

    private String verifWay;  //校验方式，接口校验 ，数据库校验

    private String verifSql; //数据库校验时执行SQL


    public String getVerifType() {
        return verifType;
    }

    public void setVerifType(String verifType) {
        this.verifType = verifType;
    }

    public String getVerifKey() {
        return verifKey;
    }

    public void setVerifKey(String verifKey) {
        this.verifKey = verifKey;
    }

    public String getVerifValue() {
        return verifValue;
    }

    public void setVerifValue(String verifValue) {
        this.verifValue = verifValue;
    }

    public String getVerifParentKey() {
        return verifParentKey;
    }

    public void setVerifParentKey(String verifParentKey) {
        this.verifParentKey = verifParentKey;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public String getVerifSql() {
        return verifSql;
    }

    public void setVerifSql(String verifSql) {
        this.verifSql = verifSql;
    }

    public String getVerifWay() {
        return verifWay;
    }

    public void setVerifWay(String verifWay) {
        this.verifWay = verifWay;
    }
}
