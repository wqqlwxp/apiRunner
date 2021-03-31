package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface ProjectMapper extends TkBaseMapper<Project> {
    //查询数据总条数
    List<Project> queryAll();

    int queryAllCount();

    //根据code查询数据是否存在
    int queryExistByCode(@Param("projectCode") String projectCode);

    int queryCountByExample(@Param("project") Project project);

    //查询分页数据
    List<Project> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("project") Project project);


}
