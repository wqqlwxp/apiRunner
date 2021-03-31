package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.dao.entity.po.ScheduleTask;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleTaskMapper extends TkBaseMapper<ScheduleTask> {
    //查询数据总条数
    List<ScheduleTask> queryAll();

    int queryAllCount();

    int queryCountByExample(@Param("scheduleTask") ScheduleTask scheduleTask);


    //查询分页数据
    List<ScheduleTask> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("scheduleTask") ScheduleTask scheduleTask);
}
