package com.runner.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.TestPlan;
import com.runner.server.service.serviceImpl.TestPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;


@Controller
@RequestMapping("/plan")
public class TestPlanController {

    @Resource
    private TestPlanService testPlanService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllTestPlan",produces = "application/json;charset=utf-8")
    public String queryAllProject(@RequestBody PageObject pageObject) throws Exception {
        return testPlanService.query(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editTestPlan")
    public MsgResponseObject editProject(@RequestBody PageObject pageObject) throws Exception {
        return testPlanService.edit(pageObject);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteTestPlan")
    public MsgResponseObject delProject(@RequestBody int id) throws Exception {
        return testPlanService.delete(id);
    }




    /**
     * @description  执行计划
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/runTestPlan")
    public MsgResponseObject runPlan(@RequestBody TestPlan testPlan) throws Exception {
        return testPlanService.runTestPlan(testPlan,null);
    }




}
