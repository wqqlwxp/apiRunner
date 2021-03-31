package com.runner.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.AppRequest;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.bo.ReqRequest;
import com.runner.server.dao.entity.po.ZkData;
import com.runner.server.service.serviceImpl.ZkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/zk")
public class ZkController {

    @Resource
    private ZkService zkService;


    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllZk",produces = "application/json;charset=utf-8")
    public String queryAllZk(@RequestBody PageObject pageObject) throws Exception {
        return zkService.query(pageObject);
    }

    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllZkData",produces = "application/json;charset=utf-8")
    public String queryAllZkData(@RequestBody PageObject pageObject) throws Exception {
        return zkService.queryData(pageObject);
    }


    /**
     * @description  编辑数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/editZk")
    public MsgResponseObject editZk(@RequestBody  PageObject pageObject) throws Exception {
        return zkService.edit(pageObject);
    }

    /**
     * @description  删除数据
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/deleteZk")
    public MsgResponseObject deleteZk(@RequestBody int id) throws Exception {
        return zkService.delete(id);
    }


    /**
     * @description 获取zk下应用名集合
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/getAppNamesByZk")
    public String getAppNamesByZk(@RequestBody String zkAddress) throws Exception {
        return zkService.getAppsByZk(zkAddress);
    }


    /**
     * @description 创建服务
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/createApp")
    public MsgResponseObject createApp(@RequestBody AppRequest appRequest)  {
        return zkService.createApp(appRequest);
    }


    /**
     * @description 创建服务
     * @author 星空梦语
     * @date 2020/4/11 11:32
     */
    @ResponseBody
    @RequestMapping(value = "/refreshZkService")
    public MsgResponseObject refresh(@RequestBody AppRequest appRequest) throws Exception {
        return zkService.refresh(appRequest);
    }


    /**
     * @description  根据zk查询应用
     * @author 星空梦语
     * @date 2021/3/29 上午9:42
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllAppByZkAddress")
    public String queryAllAppByZkAddress(@RequestBody int zkId){
        return zkService.queryAllAppByZkAddress(zkId);
    }




    /**
     * @description  根据应用查询接口
     * @author 星空梦语
     * @date 2021/3/29 上午9:42
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllInterfaceByZkApp")
    public String queryAllInterfaceByZkApp(@RequestBody ZkData zkData){
        return zkService.queryAllInterfaceByZkApp(zkData);
    }


    /**
     * @description  根据接口查询方法
     * @author 星空梦语
     * @date 2021/3/29 上午9:42
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllMethodByInterface")
    public String queryAllMethodByInterface(@RequestBody ZkData zkData){
        return zkService.queryAllMethodByInterface(zkData);
    }


    /**
     * @description 根据方法查询明细
     * @author 星空梦语
     * @date 2021/3/29 上午9:42
     */
    @ResponseBody
    @RequestMapping(value = "/queryMethodDetail")
    public String queryMethodDetail(@RequestBody ZkData zkData){
        return zkService.queryMethodDetail(zkData);
    }










}
