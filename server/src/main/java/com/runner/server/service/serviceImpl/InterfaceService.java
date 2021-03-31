package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.Interface;
import com.runner.server.dao.entity.po.TestCase;
import com.runner.server.dao.mapper.CaseMapper;
import com.runner.server.dao.mapper.InterfaceMapper;
import com.runner.server.service.utils.CacheUtil;
import com.runner.server.service.utils.DateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class InterfaceService  {

    @Resource
    private InterfaceMapper interfaceMapper;

    @Resource
    public CaseMapper  caseMapper;


    private MsgResponseObject msgResponseObject=new MsgResponseObject();


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        Interface interfaceData=new Interface();
        BeanUtils.populate(interfaceData, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<Interface> list=interfaceMapper.queryPageData(offset,pageObject.getLimit(),interfaceData);
        int total=interfaceMapper.queryCountByExample(interfaceData);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }




    /**
     * @description 新增或修改数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        Interface interfaceData=new Interface();
        msgResponseObject.setCode(201);
        BeanUtils.populate(interfaceData, pageObject.getData());
        if(StringUtils.isBlank(interfaceData.getModuleCode()) ||StringUtils.isBlank(interfaceData.getProjectCode()) ){
            msgResponseObject.setMsg("编码或名称不能为空，请确认");
            return  msgResponseObject;
        }
        if(StringUtils.isBlank(interfaceData.getInterfaceUrl())){
            msgResponseObject.setMsg("接口地址不能为空，请确认");
            return  msgResponseObject;
        }

        Interface p=new Interface();
        p.setInterfaceUrl(interfaceData.getInterfaceUrl());
        p.setModuleCode(interfaceData.getModuleCode());
        List<Interface> plist= interfaceMapper.queryPageData(0,10,p);
        if(interfaceData.getId()!=null){
            interfaceData.setModuleName(null);
            interfaceData.setProjectName(null);
            for(Interface data:plist){
                if(!data.getId().equals(interfaceData.getId())){
                    msgResponseObject.setMsg("同模块下接口地址重复");
                    return  msgResponseObject;
                }
            }
            Example example=new Example(Interface.class);
            example.createCriteria().andEqualTo("id",interfaceData.getId());
            if(interfaceMapper.updateByExampleSelective(interfaceData,example)>0){
                msgResponseObject.setMsg("修改成功");
                msgResponseObject.setCode(200);
                updateProjectCache();
            }else{
                msgResponseObject.setMsg("修改失败");
            }
        }else{
            interfaceData.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(plist.size()>=1){
                msgResponseObject.setMsg("同模块下接口地址重复");
                return  msgResponseObject;
            }
            if(interfaceMapper.insertSelective(interfaceData)>0){
                msgResponseObject.setMsg("新增成功");
                msgResponseObject.setCode(200);
                updateProjectCache();
            }else{
                msgResponseObject.setMsg("新增失败");
            }
        }
        return msgResponseObject;
    }


    /**
     * @description 删除数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject delete( Interface face){
        msgResponseObject.setCode(201);
        TestCase testCase=new TestCase();
        testCase.setProjectCode(face.getProjectCode());
        testCase.setModuleCode(face.getModuleCode());
        List<TestCase>testCaseList= caseMapper.queryPageData(0,1024,testCase,null);
        boolean flag=false;
        for (TestCase aCase : testCaseList) {
            List<String> ids= (List<String>) JSON.parse(aCase.getInterfaceIds());
            if(ids!=null){
                for(String id:ids){
                    if(id.equals(String.valueOf(face.getId()))){
                        flag=true;
                        break;
                    }
                }
            }
        }
        if(flag){
            msgResponseObject.setMsg("此接口存在测试用例数据，无法删除！");
            return msgResponseObject;
        }
        Example example=new Example(Interface.class);
        example.createCriteria().andEqualTo("id",face.getId());
        msgResponseObject.setMsg("删除失败");
        if(interfaceMapper.deleteByExample(example)>0){
            msgResponseObject.setMsg("删除成功");
            msgResponseObject.setCode(200);
            updateProjectCache();
        }
        return msgResponseObject;
    }





    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateProjectCache(){
        CacheUtil.removeCacheObject("interface");
        List<Interface> interfaceList= interfaceMapper.queryAll();
        CacheUtil.setCacheObject("interface",interfaceList,0L);
    }


    /**
     * @description  获取缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<Interface> getinterfaceCache(){
        return (List<Interface>) CacheUtil.getCacheObject("interface");
    }




    /**
     * @description  查询数据
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public List<Interface> queryDataByCode(Interface data){
        return interfaceMapper.queryByCode(data.getProjectCode(),data.getModuleCode());
    }


}
