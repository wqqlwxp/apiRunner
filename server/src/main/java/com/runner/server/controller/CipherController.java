package com.runner.server.controller;

import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.TestPlan;
import com.runner.server.service.serviceImpl.CipherService;
import com.runner.server.service.serviceImpl.TestPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/cipher")
public class CipherController {

    @Resource
    private CipherService cipherService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllCipher",produces = "application/json;charset=utf-8")
    public String queryAllCipher(@RequestBody PageObject pageObject) throws Exception {
        return cipherService.query(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editCipher")
    public MsgResponseObject editCipher(@RequestBody PageObject pageObject) throws Exception {
        return cipherService.edit(pageObject);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCipher")
    public MsgResponseObject deleteCipher(@RequestBody int id) throws Exception {
        return cipherService.delete(id);
    }


}
