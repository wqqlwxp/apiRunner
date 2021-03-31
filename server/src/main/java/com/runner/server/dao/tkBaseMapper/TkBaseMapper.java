package com.runner.server.dao.tkBaseMapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.condition.UpdateByConditionSelectiveMapper;


public interface TkBaseMapper<T> extends Mapper<T> ,UpdateByConditionSelectiveMapper<T> {
}
