package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.ZkConfig;
import com.runner.server.dao.entity.po.ZkData;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ZkDataMapper extends TkBaseMapper<ZkData> {
    //查询数据总条数
    List<ZkData> queryAll();

    int queryAllCount();

    int queryCountByExample(@Param("zkData") ZkData zkData);

    int insertBatchZkData(@Param("list") List<ZkData> list);

    //查询分页数据
    List<ZkData> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("zkData") ZkData zkData);
}
