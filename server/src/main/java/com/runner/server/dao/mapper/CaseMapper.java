package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.TestCase;
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
public interface CaseMapper extends TkBaseMapper<TestCase> {

    //查询数据总条数
    List<TestCase> queryAll();

    int queryAllCount();

    TestCase  querySingleCase(@Param("id") String id);

    int queryCountByExample(@Param("testCase") TestCase testCase);

    //根据code数据
    int queryByCode(@Param("projectCode") String projectCode, @Param("moduleCode") String moduleCode,@Param("env") String env);


    //查询分页数据
    List<TestCase> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("testCase") TestCase testCase, @Param("modules") List<String> modules);

}
