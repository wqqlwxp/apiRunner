package com.runner.server.controller;

import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.entity.po.ProjectModule;
import com.runner.server.service.serviceImpl.ProjectService;
import com.runner.server.service.serviceImpl.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;


    /**
     * @description  查询项目数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllReportDetail",produces = "application/json;charset=utf-8")
    public String queryAllProject(@RequestBody PageObject pageObject) throws Exception {
        return reportService.query(pageObject);
    }


    /**
     * 查询汇总
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryReportSummary",produces = "application/json;charset=utf-8")
    public String queryAllProject() throws Exception {
        return reportService.getsummaryCache();
    }
















}
