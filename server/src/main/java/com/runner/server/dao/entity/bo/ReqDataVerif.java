package com.runner.server.dao.entity.bo;

import java.util.List;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/6/5 下午5:51
 */
public class ReqDataVerif {
    private List<VerifData> sqlData;  //验证sql数据

    private String type;  //验证类型

    public List<VerifData> getSqlData() {
        return sqlData;
    }

    public void setSqlData(List<VerifData> sqlData) {
        this.sqlData = sqlData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
