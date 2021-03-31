package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.Cipher;
import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CipherMapper extends TkBaseMapper<Cipher> {
    //查询数据总条数
    List<Cipher> queryAll();

    int queryAllCount();

    int queryCountByExample(@Param("cipher") Cipher cipher);

    //查询分页数据
    List<Cipher> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("cipher") Cipher cipher);


}
