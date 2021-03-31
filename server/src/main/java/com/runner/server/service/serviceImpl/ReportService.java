package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.bo.ReportSummary;
import com.runner.server.dao.entity.po.CaseLog;
import com.runner.server.dao.entity.po.Project;
import com.runner.server.dao.entity.po.ProjectModule;
import com.runner.server.dao.entity.po.TestCase;
import com.runner.server.dao.mapper.ProjectMapper;
import com.runner.server.dao.mapper.ProjectModuleMapper;
import com.runner.server.dao.mapper.ReportMapper;
import com.runner.server.service.utils.CacheUtil;
import com.runner.server.service.utils.DateUtil;
import com.runner.server.service.utils.LogUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private ProjectService projectService;

    @Resource
    private CaseService caseService;

    @Resource
    private InterfaceService interfaceService;


    private MsgResponseObject msgResponseObject=new MsgResponseObject();


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        CaseLog caseLog=new CaseLog();
        BeanUtils.populate(caseLog, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<CaseLog> list=reportMapper.queryPageData(offset,pageObject.getLimit(),caseLog);
        int total=reportMapper.queryCountByExample(caseLog);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }



    /**
     * @description  查询汇总数据
     * @author 星空梦语
     * @date 2021/2/20 上午9:58
     */
    public String querySummary(){
        Map map=new HashMap();
        map.put("projectCount",projectService.getProjectCache().size());
        map.put("moduleCount",projectService.getProjectModuleCache().size());
        map.put("interfaceCount",interfaceService.getinterfaceCache().size());
        map.put("caseCount",caseService.getTestCaseCache());
        map.put("reportCount",getReportCache());
        List<String> projectList=new ArrayList();
        List<String> moduleList=new ArrayList();
        List<String> datetList=new ArrayList();
        List summary=new ArrayList();
        List module=new ArrayList();
        List<ReportSummary> reportSummaryList=reportMapper.queryProjectSummary();
        List<ReportSummary> moduleSummaryList=reportMapper.queryModuleSummary();

        for(ReportSummary reportSummary:moduleSummaryList){
            moduleList.add(reportSummary.getModuleName());
        }
        for(ReportSummary reportSummary:reportSummaryList){
            projectList.add(reportSummary.getProjectName());
            datetList.add(reportSummary.getCreateTime());
        }
        projectList = projectList.stream().distinct().collect(Collectors.toList());
        moduleList = moduleList.stream().distinct().collect(Collectors.toList());
        datetList = datetList.stream().distinct().collect(Collectors.toList());

        //遍历获取项目和时间维度执行成功率
        for(String name:projectList){
            List data=new ArrayList();
            Map summaryMap=new HashMap();
            for(String date:datetList){
                BigDecimal rate=new BigDecimal(0);
                for(ReportSummary report:reportSummaryList){
                    if(name.equals(report.getProjectName()) && date.equals(report.getCreateTime())){
                        rate=new BigDecimal(report.getSuccess()).divide(new BigDecimal(report.getSummary()),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        break;
                    }
                }
                data.add(rate);
            }
            summaryMap.put("key",name);
            summaryMap.put("value",data);
            summary.add(summaryMap);
        }


        //遍历获取模块和时间维度执行成功率
        for(String name:moduleList){
            List data=new ArrayList();
            Map summaryMap=new HashMap();
            for(String date:datetList){
                BigDecimal rate=new BigDecimal(0);
                for(ReportSummary report:moduleSummaryList){
                    if(name.equals(report.getModuleName()) && date.equals(report.getCreateTime())){
                        rate=new BigDecimal(report.getSuccess()).divide(new BigDecimal(report.getSummary()),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        break;
                    }
                }
                data.add(rate);
            }
            summaryMap.put("key",name);
            summaryMap.put("value",data);
            module.add(summaryMap);
            LogUtil.info("汇总："+JSON.toJSONString(module));
        }

        map.put("projectSummary",JSON.toJSONString(summary));
        map.put("moduleSummary",JSON.toJSONString(module));
        map.put("dateList",JSON.toJSONString(datetList));
        map.put("projectList",JSON.toJSONString(projectList));
        map.put("moduleList",JSON.toJSONString(moduleList));
        return  JSON.toJSONString(map);
    }






    /**
     * @description 获取缓存
     * @author 星空梦语
     * @date 2021/2/20 上午10:31
     */
    public int getReportCache(){
        return (int) CacheUtil.getCacheObject("report");
    }




    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateReportCache(){
        LogUtil.info("开始更新用例执行记录缓存");
        CacheUtil.removeCacheObject("report");
        int count= reportMapper.queryAllCount();
        CacheUtil.setCacheObject("report",count,0L);
    }




    /**
     * @description  获取汇总缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public String getsummaryCache(){
        if(!CacheUtil.checkCacheName("summary")){
            LogUtil.info("重新设置汇总缓存");
            updateSummaryCache();
        }
        return (String) CacheUtil.getCacheObject("summary");
    }


    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateSummaryCache(){
        LogUtil.info("开始更新汇总缓存");
        String summaryList= this.querySummary();
        CacheUtil.setCacheObject("summary",summaryList,CacheUtil.CACHE_HOLD_TIME_12H);
    }



}
