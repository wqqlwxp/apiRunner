package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.ProjectModule;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface ProjectModuleMapper extends TkBaseMapper<ProjectModule> {
    //查询数据总条数
    List<ProjectModule> queryAllModule();

    int queryAllCount();

    //根据code数据
    List<ProjectModule> queryByCode(@Param("projectCode") String projectCode, @Param("moduleCode") String moduleCode);


    int queryCountByExample(@Param("projectModule") ProjectModule projectModule);

    //查询分页数据
    List<ProjectModule> queryModulePageData(@Param("offset") int offset, @Param("size") int size, @Param("projectModule") ProjectModule projectModule);

}
