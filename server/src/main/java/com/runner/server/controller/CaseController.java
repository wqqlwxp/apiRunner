package com.runner.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.bo.ReqRequest;
import com.runner.server.dao.entity.po.TestCase;
import com.runner.server.service.serviceImpl.CaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/case")
public class CaseController {


    @Resource
    private CaseService caseService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllCase",produces = "text/html;charset=utf-8")
    public String queryAllCase(@RequestBody PageObject pageObject) throws Exception {
        return caseService.query(pageObject);
    }




    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryDetail",produces = "text/html;charset=utf-8")
    public String queryDetail(@RequestBody String  id) throws Exception {
        return caseService.queryDetail(id);
    }


    /**
     * @description  保存用例
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editCase")
    public MsgResponseObject editCase(@RequestBody PageObject pageObject) throws Exception {
         return caseService.editCase(pageObject);
    }




    /**
     * @description  导入
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/importCase")
    public MsgResponseObject importCase(@RequestBody PageObject pageObject) throws Exception {
        return caseService.importCase(pageObject);
    }



    /**
     * @description  查询
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryByCode")
    public List<TestCase> queryByCode(@RequestBody String projectCode, String moduleCode){
        return caseService.queryByCode(projectCode,moduleCode);
    }


    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCase")
    public MsgResponseObject deleteCase(@RequestBody int id) throws Exception {
        return caseService.delete(id);
    }



    /**
     * @description  执行普通用例
     * @author 星空梦语
     * @date 2021/3/29 上午9:41
     */
    @ResponseBody
    @RequestMapping(value = "/runCase")
    public void runCase(@RequestBody String data) throws Exception {
        ReqRequest reqRequest= JSONObject.parseObject(data, ReqRequest.class);
        caseService.runCase(reqRequest);
    }


    /**
     * @description 执行dubbo用例
     * @author 星空梦语
     * @date 2021/3/29 上午9:42
     */
    @ResponseBody
    @RequestMapping(value = "/runDubboCase")
    public String runDubboCase(@RequestBody String data) throws Exception {
        ReqRequest reqRequest= JSONObject.parseObject(data, ReqRequest.class);
        return JSON.toJSONString(caseService.invokeMethod(reqRequest).get("result"));
    }

}
