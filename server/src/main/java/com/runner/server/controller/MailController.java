package com.runner.server.controller;

import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.service.serviceImpl.EnvMachineService;
import com.runner.server.service.serviceImpl.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Resource
    private MailService mailService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllMail",produces = "application/json;charset=utf-8")
    public String queryAllMail(@RequestBody PageObject pageObject) throws Exception {
        return mailService.query(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editMail")
    public MsgResponseObject editProject(@RequestBody PageObject pageObject) throws Exception {
        return  mailService.edit(pageObject);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteMail")
    public MsgResponseObject delProject(@RequestBody int id) throws Exception {
        return mailService.delete(id);
    }




}
