package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.DatabaseConfig;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataBaseMapper extends TkBaseMapper<DatabaseConfig> {
    //查询数据总条数
    List<DatabaseConfig> queryAll();

    int queryAllCount();

    int queryCountByExample(@Param("dataBaseConfig") DatabaseConfig dataBaseConfig);

    //查询分页数据
    List<DatabaseConfig> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("dataBaseConfig") DatabaseConfig dataBaseConfig);
}
