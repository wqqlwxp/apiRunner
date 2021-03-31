package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.ScheduleTask;
import com.runner.server.dao.entity.po.ZkConfig;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ZkMapper extends TkBaseMapper<ZkConfig> {
    //查询数据总条数
    List<ZkConfig> queryAll();

    int queryAllCount();

    int queryCountByExample(@Param("zkConfig") ZkConfig zkConfig);


    //查询分页数据
    List<ZkConfig> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("zkConfig") ZkConfig zkConfig);
}
