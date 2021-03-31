package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface EnvMachineMapper extends TkBaseMapper<EnvMachine> {
    //查询数据总条数
    List<EnvMachine> queryAll();

    int queryAllCount();

    int queryCountByExample(@Param("envMachine") EnvMachine envMachine);

    //根据code数据
    List<EnvMachine> queryByCode(@Param("projectCode") String projectCode);


    //查询分页数据
    List<EnvMachine> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("envMachine") EnvMachine envMachine);
}
