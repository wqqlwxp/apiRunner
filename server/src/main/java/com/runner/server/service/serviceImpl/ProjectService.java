package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.entity.po.ProjectModule;
import com.runner.server.dao.mapper.*;
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
public class ProjectService   {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectModuleMapper projectModuleMapper;

    @Resource
    private InterfaceMapper interfaceMapper;

    @Resource
    private TestPlanMapper testPlanMapper;

    @Resource
    private CaseMapper caseMapper;

    @Resource
    private EnvMachineMapper envMachineMapper;


    private MsgResponseObject msgResponseObject=new MsgResponseObject();


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        Project project=new Project();
        BeanUtils.populate(project, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<Project> list=projectMapper.queryPageData(offset,pageObject.getLimit(),project);
        int total=projectMapper.queryCountByExample(project);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 查询模块数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String queryModule(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        ProjectModule module=new ProjectModule();
        BeanUtils.populate(module, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<ProjectModule> list=projectModuleMapper.queryModulePageData(offset,pageObject.getLimit(),module);
        int total=projectModuleMapper.queryCountByExample(module);
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
        Project project=new Project();
        BeanUtils.populate(project, pageObject.getData());
        msgResponseObject.setCode(200);
        if(StringUtils.isBlank(project.getProjectName()) ||StringUtils.isBlank(project.getProjectCode()) ){
            msgResponseObject.setMsg("名称或编码不能为空，请确认");
            msgResponseObject.setCode(201);
            return  msgResponseObject;
        }
        Project p=new Project();
        p.setProjectCode(project.getProjectCode());
        List<Project> plist= projectMapper.queryPageData(0,10,p);
        if(project.getId()!=null){
            for(Project data:plist){
                if(!data.getId().equals(project.getId())){
                    msgResponseObject.setCode(201);
                    msgResponseObject.setMsg("项目编码不能与已有项目编码重复");
                    return  msgResponseObject;
                }
            }
            Example example=new Example(Project.class);
            example.createCriteria().andEqualTo("id",project.getId());
            if(projectMapper.updateByExampleSelective(project,example)>0){
                updateProjectCache();
                msgResponseObject.setMsg("修改成功");
            }else{
                msgResponseObject.setMsg("修改失败");
                msgResponseObject.setCode(201);
            }
        }else{
            if(plist.size()>=1){
                msgResponseObject.setMsg("项目编码不能与已有项目编码重复");
                msgResponseObject.setCode(201);
                return  msgResponseObject;
            }
            project.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(projectMapper.insertSelective(project)>0){
                updateProjectCache();
                msgResponseObject.setMsg("新增数据成功");
            }else{
                msgResponseObject.setMsg("新增数据失败");
                msgResponseObject.setCode(201);
            }
        }
        return  msgResponseObject;
    }



    /**
     * @description 新增或修改项目模块数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject editModule(PageObject pageObject) throws Exception {
        ProjectModule projectModule=new ProjectModule();
        BeanUtils.populate(projectModule, pageObject.getData());
        msgResponseObject.setCode(200);
        if(StringUtils.isBlank(projectModule.getModuleCode()) ||StringUtils.isBlank(projectModule.getProjectCode()) ){
            msgResponseObject.setMsg("编码不能为空，请确认");
            msgResponseObject.setCode(201);
            return  msgResponseObject;
        }
        ProjectModule p=new ProjectModule();
        p.setModuleCode(projectModule.getModuleCode());
        p.setProjectCode(projectModule.getProjectCode());
        List<ProjectModule> plist= projectModuleMapper.queryModulePageData(0,10,p);
        if(projectModule.getId()!=null){
            for(ProjectModule data:plist){
                if(!data.getId().equals(projectModule.getId())){
                    msgResponseObject.setCode(201);
                    msgResponseObject.setMsg("模块编码不能与已有模块编码重复");
                    return  msgResponseObject;
                }
            }
            Example example=new Example(ProjectModule.class);
            example.createCriteria().andEqualTo("id",projectModule.getId());
            if(projectModuleMapper.updateByExampleSelective(projectModule,example)>0){
                updateProjectModuleCache();
                msgResponseObject.setMsg("修改成功");
            }else{
                msgResponseObject.setMsg("修改失败");
                msgResponseObject.setCode(201);
            }
        }else{
            if(plist.size()>=1){
                msgResponseObject.setMsg("模块编码不能与已有模块编码重复");
                msgResponseObject.setCode(201);
                return  msgResponseObject;
            }
            projectModule.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(projectModuleMapper.insertSelective(projectModule)>0){
                updateProjectModuleCache();
                msgResponseObject.setMsg("新增数据成功");
            }else{
                msgResponseObject.setMsg("新增数据失败");
                msgResponseObject.setCode(201);
            }
        }
        return  msgResponseObject;
    }


    /**
     * @description 删除项目数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject delete(Project project){
        msgResponseObject.setCode(201);
        int total=projectModuleMapper.queryByCode(project.getProjectCode(),null).size();
        if(total>0){
            msgResponseObject.setMsg("此项目下存在模块数据，无法删除！");
            return msgResponseObject;
        }
        Example example=new Example(Project.class);
        example.createCriteria().andEqualTo("id",project.getId());
        msgResponseObject.setMsg("删除项目数据失败");
        if(projectMapper.deleteByExample(example)>0){
            updateProjectCache();
            msgResponseObject.setMsg("删除项目数据成功");
            msgResponseObject.setCode(200);
        }
        return msgResponseObject;
    }



    /**
     * @description 删除数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject deleteModule(ProjectModule projectModule){
        msgResponseObject.setCode(201);
        int total=interfaceMapper.queryByCode(projectModule.getProjectCode(),projectModule.getModuleCode()).size();
        if(total>0){
            msgResponseObject.setMsg("此项目和模块下存在接口数据，无法删除！");
            return msgResponseObject;
        }
        total=testPlanMapper.queryByCode(projectModule.getProjectCode(),projectModule.getModuleCode()).size();
        if(total>0){
            msgResponseObject.setMsg("此项目和模块下存在测试计划数据，无法删除！");
            return msgResponseObject;
        }

        total=caseMapper.queryByCode(projectModule.getProjectCode(),projectModule.getModuleCode(),null);
        if(total>0){
            msgResponseObject.setMsg("此项目和模块下存在测试用例数据，无法删除！");
            return msgResponseObject;
        }

        Example example=new Example(ProjectModule.class);
        example.createCriteria().andEqualTo("id",projectModule.getId());
        msgResponseObject.setMsg("删除数据失败");
        if(projectModuleMapper.deleteByExample(example)>0){
            updateProjectModuleCache();
            msgResponseObject.setCode(200);
            msgResponseObject.setMsg("删除数据成功");
        }
        return msgResponseObject;
    }


    /**
     * @description 查询模块
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public List<ProjectModule> queryModuleByCode(ProjectModule module){
          return  projectModuleMapper.queryByCode(module.getProjectCode(),module.getModuleCode());
    }



    /**
     * @description  获取项目缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<Project> getProjectCache(){
        return (List<Project>) CacheUtil.getCacheObject("project");
    }


    /**
     * @description  获取项目模块缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<ProjectModule> getProjectModuleCache(){
        return (List<ProjectModule>) CacheUtil.getCacheObject("projectModule");
    }




    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateProjectCache(){
        LogUtil.info("开始更新项目配置缓存");
        CacheUtil.removeCacheObject("project");
        List<Project> projectList= projectMapper.queryAll();
        CacheUtil.setCacheObject("project",projectList,0L);
    }


    /**
     * @description  更新模块缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateProjectModuleCache(){
        LogUtil.info("开始更新项目模块配置缓存");
        CacheUtil.removeCacheObject("projectModule");
        List<ProjectModule> projectList= projectModuleMapper.queryAllModule();
        CacheUtil.setCacheObject("projectModule",projectList,0L);
    }


}
