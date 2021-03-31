package com.runner.server.controller;


import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.Interface;
import com.runner.server.service.serviceImpl.CaseService;
import com.runner.server.service.serviceImpl.InterfaceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/interface")
public class InterfaceController {

    @Resource
    private InterfaceService interfaceService;


    /**
     * @description  查询接口数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllInterface",produces = "text/html;charset=utf-8")
    public String queryAllInterface(@RequestBody PageObject pageObject) throws Exception {
        return interfaceService.query(pageObject);
    }


    /**
     * @description  编辑接口数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editInterface")
    public MsgResponseObject editInterface(@RequestBody PageObject pageObject) throws Exception {
        return interfaceService.edit(pageObject);
    }

    /**
     * @description  删除接口数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteInterface")
    public MsgResponseObject delInterface(@RequestBody Interface face) throws Exception {
        return interfaceService.delete(face);
    }





    /**
     * @description  查询接口数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryDataByCode")
    public List<Interface> queryDataByCode(@RequestBody Interface data) throws Exception {
        return interfaceService.queryDataByCode(data);
    }



//    @ResponseBody
//    @RequestMapping(value = "/interfaceTest")
//    public String interfaceTest(@RequestBody String data) throws Exception {
//        ReqInterfaceData reqInterfaceData=JSONObject.parseObject(data, ReqInterfaceData.class);
//        String resp=interfaceService.interfaceTest(reqInterfaceData);
//        reqInterfaceData.setPlanId("");
//        caseService.verifResponseData(reqInterfaceData,resp);
//        return resp;
//    }





}
