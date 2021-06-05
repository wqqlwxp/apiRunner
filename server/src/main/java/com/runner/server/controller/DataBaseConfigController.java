package com.runner.server.controller;

import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.service.serviceImpl.CipherService;
import com.runner.server.service.serviceImpl.DataBaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/databaseConfig")
public class DataBaseConfigController {

    @Resource
    private DataBaseService dataBaseService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllDatabaseConfig",produces = "application/json;charset=utf-8")
    public String queryDatabaseConfig(@RequestBody PageObject pageObject) throws Exception {
        return dataBaseService.query(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editDatabaseConfig")
    public MsgResponseObject editCipher(@RequestBody PageObject pageObject) throws Exception {
        return dataBaseService.edit(pageObject);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteDatabaseConfig")
    public MsgResponseObject deleteCipher(@RequestBody int id) throws Exception {
        return dataBaseService.delete(id);
    }


}
