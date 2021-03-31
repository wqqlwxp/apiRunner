package com.runner.server.controller;

import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.ScheduleTask;
import com.runner.server.service.serviceImpl.ScheduleTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/task")
public class ScheduleTaskController {

    @Resource
    private ScheduleTaskService scheduleTaskService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllTask",produces = "application/json;charset=utf-8")
    public String queryAllTask(@RequestBody PageObject pageObject) throws Exception {
        return scheduleTaskService.query(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editTask")
    public MsgResponseObject editTask(@RequestBody ScheduleTask scheduleTask) throws Exception {
        return scheduleTaskService.edit(scheduleTask);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteTask")
    public MsgResponseObject delProject(@RequestBody ScheduleTask scheduleTask) throws Exception {
        return scheduleTaskService.delete(scheduleTask);
    }


}
