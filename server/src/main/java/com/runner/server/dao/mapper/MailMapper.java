package com.runner.server.dao.mapper;

import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.dao.entity.po.Mail;
import com.runner.server.dao.tkBaseMapper.TkBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MailMapper extends TkBaseMapper<Mail> {
    //查询数据总条数
    List<Mail> queryAll();

    int queryAllCount();

    int queryCountByExample( @Param("mail") Mail mail);

    //查询分页数据
    List<Mail> queryPageData(@Param("offset") int offset, @Param("size") int size, @Param("mail") Mail mail);
}
