package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.TestPlan;
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
public interface TestPlanMapper extends TkBaseMapper<TestPlan> {

    int queryAllCount();

    //查询数据总条数
    List<TestPlan> queryAll();

    int queryCountByExample(@Param("testPlan") TestPlan testPlan);

    //根据code数据
    List<TestPlan> queryByCode(@Param("projectCode") String projectCode, @Param("moduleCode") String moduleCode);


    //查询分页数据
    List<TestPlan> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("testPlan") TestPlan testPlan);

}
