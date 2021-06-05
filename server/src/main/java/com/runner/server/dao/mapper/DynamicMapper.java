package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.DatabaseConfig;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/6/4 上午11:07
 */
public interface DynamicMapper {
    List<HashMap<String, Object>> query(@Param("sqlStr") String sqlStr);
}
