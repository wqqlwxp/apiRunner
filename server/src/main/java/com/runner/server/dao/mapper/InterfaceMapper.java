package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.Interface;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * @author 星空梦语
 * @desc
 * @date 2020/4/19 17:24
 */

@Mapper
public interface InterfaceMapper extends TkBaseMapper<Interface> {
    //查询数据总条数
    List<Interface> queryAll();

    int queryAllCount();

    //查询数据是否存在
    int queryExistByCode(@Param("projectCode") String projectCode, @Param("interfaceUrl") String interfaceUrl);

    int queryCountByExample(@Param("interfaceData") Interface interfaceData);

    //查询数据是否存在
    List<Interface> queryByCode(@Param("projectCode") String projectCode, @Param("moduleCode") String moduleCode);


    //查询分页数据
    List<Interface> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("interfaceData") Interface interfaceData);
}
