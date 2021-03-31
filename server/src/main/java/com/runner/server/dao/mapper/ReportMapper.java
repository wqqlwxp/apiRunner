package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.bo.ReportSummary;
import com.runner.server.dao.entity.po.CaseLog;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * @author 星空梦语
 * @desc
 * @date 2020/4/26 16:00
 */
@Mapper
public interface ReportMapper extends TkBaseMapper<CaseLog> {

    List<ReportSummary> queryProjectSummary();

    List<ReportSummary> queryModuleSummary();


    int queryAllCount();

    int queryCountByExample(@Param("caseLog") CaseLog  caseLog);

    List<CaseLog> queryAll();

    //查询数据总条数
    List<CaseLog> queryPageData(@Param("offset") int offset, @Param("size") int size,@Param("caseLog") CaseLog  caseLog);

}
