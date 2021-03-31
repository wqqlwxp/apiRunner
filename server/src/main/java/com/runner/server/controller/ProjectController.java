package com.runner.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.entity.po.ProjectModule;
import com.runner.server.service.serviceImpl.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;


    /**
     * @description  查询项目数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllProject",produces = "application/json;charset=utf-8")
    public String queryAllProject(@RequestBody PageObject pageObject) throws Exception {
        return projectService.query(pageObject);
    }


    /**
     * @description  查询项目模块
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllProjectModule",produces = "application/json;charset=utf-8")
    public String queryAllProjectModule(@RequestBody PageObject pageObject) throws Exception {
        return projectService.queryModule(pageObject);
    }




    /**
     * @description  编辑项目数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editProject")
        public MsgResponseObject editProject(@RequestBody PageObject pageObject) throws Exception {
        return projectService.edit(pageObject);
    }


    /**
     * @description  编辑模块数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editProjectModule")
    public MsgResponseObject editProjectModule(@RequestBody PageObject pageObject) throws Exception {
        return projectService.editModule(pageObject);
    }



    /**
     * @description  删除项目数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteProject")
    public MsgResponseObject delProject(@RequestBody Project project) throws Exception {
        return projectService.delete(project);
    }


    /**
     * @description  删除模块数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteProjectModule")
    public MsgResponseObject deleteProjectModule(@RequestBody ProjectModule projectModule) throws Exception {
        return projectService.deleteModule(projectModule);
    }


    /**
     * @description 查询模块数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryModuleByCode")
    public List<ProjectModule> queryModuleByCode(ProjectModule module){
        return projectService.queryModuleByCode(module);
    }











}
