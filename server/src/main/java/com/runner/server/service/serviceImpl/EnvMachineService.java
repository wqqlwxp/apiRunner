package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.EnvMachine;
import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.mapper.CaseMapper;
import com.runner.server.dao.mapper.EnvMachineMapper;
import com.runner.server.service.utils.CacheUtil;
import com.runner.server.service.utils.DateUtil;
import com.runner.server.service.utils.LogUtil;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnvMachineService    {

    @Resource
    private EnvMachineMapper envMachineMapper;

    @Resource
    private CaseMapper caseMapper;

    private MsgResponseObject msgResponseObject=new MsgResponseObject() ;


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        EnvMachine envMachine=new EnvMachine();
        BeanUtils.populate(envMachine, pageObject.getData());
        int total=0;
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<EnvMachine> list=envMachineMapper.queryPageData(offset,pageObject.getLimit(),envMachine);
        total=envMachineMapper.queryCountByExample(envMachine);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 新增或修改项目数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        EnvMachine envMachine=new EnvMachine();
        BeanUtils.populate(envMachine, pageObject.getData());
        msgResponseObject.setCode(201);
        if(StringUtils.isBlank(envMachine.getEnvCode()) ||StringUtils.isBlank(envMachine.getProjectCode())){
            msgResponseObject.setMsg("编码或名称不能为空，请确认");
            return  msgResponseObject;
        }

        EnvMachine p=new EnvMachine();
        p.setEnvCode(envMachine.getEnvCode());
        p.setProjectCode(envMachine.getProjectCode());
        List<EnvMachine> plist= envMachineMapper.queryPageData(0,10,p);
        envMachine.setProjectName(null);
        if(envMachine.getId()!=null){
            for(EnvMachine data:plist){
                if(!data.getId().equals(envMachine.getId())){
                    msgResponseObject.setMsg("同项目下，环境编码重复");
                    return  msgResponseObject;
                }
            }
            Example example=new Example(Project.class);
            example.createCriteria().andEqualTo("id",envMachine.getId());
            msgResponseObject.setMsg("修改失败");
            if(envMachineMapper.updateByExampleSelective(envMachine,example)>0){
                updateEnvMachineCache();
                msgResponseObject.setMsg("修改成功");
                msgResponseObject.setCode(200);
            }
        }else{
            if(plist.size()>=1){
                msgResponseObject.setMsg("同项目下，环境编码重复");
                return  msgResponseObject;
            }
            envMachine.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(envMachineMapper.insertSelective(envMachine)>0){
                updateEnvMachineCache();
                msgResponseObject.setMsg("新增成功");
                msgResponseObject.setCode(200);
            }else{
                msgResponseObject.setMsg("新增失败");
            }
        }
        return  msgResponseObject;
    }


    /**
     * @description
     * @author 星空梦语
     * @date 2021/2/28 下午5:37
     */
    public MsgResponseObject delete(EnvMachine env) throws Exception {
        msgResponseObject.setCode(201);
        int total=caseMapper.queryByCode(env.getProjectCode(),null,env.getEnvCode());
        if(total>0){
            msgResponseObject.setMsg("此项目，项目和环境下存在测试用例数据，无法删除！");
            return msgResponseObject;
        }
        Example example=new Example(Project.class);
        example.createCriteria().andEqualTo("id",env.getId());
        msgResponseObject.setMsg("删除失败");
        if(envMachineMapper.deleteByExample(example)>0){
            updateEnvMachineCache();
            msgResponseObject.setMsg("删除成功");
            msgResponseObject.setCode(200);
        }
        return msgResponseObject;
    }



    /**
     * @description  获取项目缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<EnvMachine> getEnvMachineCache(){
        return (List<EnvMachine>) CacheUtil.getCacheObject("envMachine");
    }


    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateEnvMachineCache(){
        LogUtil.info("开始更新机器配置缓存");
        CacheUtil.removeCacheObject("envMachine");
        List<EnvMachine> envMachineList= envMachineMapper.queryAll();
        CacheUtil.setCacheObject("envMachine",envMachineList,0L);
    }


    public List<EnvMachine> queryData(String projectCode){
        return envMachineMapper.queryByCode(projectCode);
    }



}
