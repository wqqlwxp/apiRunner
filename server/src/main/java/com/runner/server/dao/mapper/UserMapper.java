package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.TestPlan;
import com.runner.server.dao.entity.po.User;
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
public interface UserMapper extends TkBaseMapper<User> {

    int queryAllCount();

    //查询数据总条数
    List<User> queryAll();

    //查询分页数据
    List<User> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("user") User user);

}
