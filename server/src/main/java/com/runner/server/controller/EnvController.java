package com.runner.server.controller;

import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.service.serviceImpl.EnvMachineService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/env")
public class EnvController  {

    @Resource
    private EnvMachineService envMachineService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllEnvMachine",produces = "application/json;charset=utf-8")
    public String queryAllProject(@RequestBody PageObject pageObject) throws Exception {
        return envMachineService.query(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editEnvMachine")
    public MsgResponseObject editProject(@RequestBody PageObject pageObject) throws Exception {
        return envMachineService.edit(pageObject);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteEnvMachine")
    public MsgResponseObject delProject(@RequestBody EnvMachine envMachine) throws Exception {
        return envMachineService.delete(envMachine);
    }





    /**
     * @description  根据项目编码查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryEnvData")
    public List<EnvMachine> querySingleEnv(@RequestBody EnvMachine envMachine){
        return envMachineService.queryData(envMachine.getProjectCode());
    }


}
