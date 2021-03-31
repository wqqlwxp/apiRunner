package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.runner.server.dao.entity.bo.*;
import com.runner.server.dao.entity.po.*;
import com.runner.server.dao.mapper.CaseMapper;
import com.runner.server.dao.mapper.TestPlanMapper;
import com.runner.server.service.utils.CacheUtil;
import com.runner.server.service.utils.DateUtil;
import com.runner.server.service.utils.LogUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


@Service
public class TestPlanService  {

    @Resource
    public TestPlanMapper testPlanMapper;

    @Resource
    public CaseMapper caseMapper;

    @Resource
    private CaseService caseService;


    @Resource
    public ReportService reportService;

    @Resource
    private MailService mailService;




    private MsgResponseObject msgReponseObject=new MsgResponseObject() ;


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        TestPlan testPlan=new TestPlan();
        BeanUtils.populate(testPlan, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<TestPlan> list=testPlanMapper.queryPageData(offset,pageObject.getLimit(),testPlan);
        int total=testPlanMapper.queryCountByExample(testPlan);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 新增或修改计划数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        TestPlan testPlan=new TestPlan();
        BeanUtils.populate(testPlan, pageObject.getData());
        msgReponseObject.setCode(201);
        if(StringUtils.isBlank(testPlan.getModuleCode()) ||StringUtils.isBlank(testPlan.getProjectCode())  ){
            msgReponseObject.setMsg("编码或名称不能为空，请确认");
            return  msgReponseObject;
        }
        if(StringUtils.isBlank(testPlan.getEnvCode()) ){
            msgReponseObject.setMsg("环境不能为空，请确认");
            return  msgReponseObject;
        }

        if(testPlan.getId()!=null){
            testPlan.setModuleName(null);
            testPlan.setProjectName(null);
            Example example=new Example(TestPlan.class);
            example.createCriteria().andEqualTo("id",testPlan.getId());
            msgReponseObject.setMsg("修改失败");
            if(testPlanMapper.updateByExampleSelective(testPlan,example)>0){
                msgReponseObject.setMsg("修改成功");
                msgReponseObject.setCode(200);
                updateTestPlanCache();
            }
        }else{
            testPlan.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(testPlanMapper.insertSelective(testPlan)>0){
                msgReponseObject.setMsg("新增成功");
                msgReponseObject.setCode(200);
                updateTestPlanCache();
            }else{
                msgReponseObject.setMsg("新增失败");
            }
        }
        return  msgReponseObject;
    }

    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/2/23 上午10:10
     */
    public MsgResponseObject delete(int id) throws Exception {
        Example example=new Example(TestPlan.class);
        example.createCriteria().andEqualTo("id",id);
        msgReponseObject.setMsg("删除失败");
        if(testPlanMapper.deleteByExample(example)>0){
            msgReponseObject.setMsg("删除成功");
            msgReponseObject.setCode(200);
            updateTestPlanCache();
        }
        return msgReponseObject;
    }



    /**
     * @description  获取缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<TestPlan> getTestPlanCache(){
        return (List<TestPlan>) CacheUtil.getCacheObject("testPlan");
    }


    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateTestPlanCache(){
        LogUtil.info("开始更新测试计划缓存");
        List<TestPlan> testPlanList= testPlanMapper.queryAll();
        CacheUtil.setCacheObject("testPlan",testPlanList,0L);
    }



    /**
     * @description 测试计划执行
     * @author 星空梦语
     * @date 2020/9/6 15:52
     */
    public MsgResponseObject runTestPlan(TestPlan testPlan,List<TestPlan> testPlanList) throws Exception {
        MailReportSummary mailReportSummary=new MailReportSummary();
        mailReportSummary.setSys(testPlan.getProjectName());
        if(testPlanList!=null){
            mailReportSummary.setSys(testPlanList.get(0).getProjectName());
        }
        try{
            TestCase tcase=new TestCase();
            tcase.setStatus("0");
            tcase.setType("1");  //查询正常用例，前置用例只能单独执行
            List<CaseLog> caseLogList=new ArrayList<>();
            if(testPlanList==null){
                testPlanList=new ArrayList<>();
                testPlanList.add(testPlan);
            }
            int total=0;
            List<TestCase>  testCaseList=null;
            mailReportSummary.setStartDate(DateUtil.formatToDayByDate3(new Date()));
            ExecutorService threadPool = Executors.newFixedThreadPool(5);   //初始化线程池
            List<Future<List<CaseLog>>> futures=new ArrayList<Future<List<CaseLog>>>();
            for(TestPlan testPlan1:testPlanList){  //遍历测试计划集合，每个测试计划下执行的环境可能不一样
                tcase.setProjectCode(testPlan1.getProjectCode());
                tcase.setServiceType(testPlan1.getServiceType());
                List<String>  modules =  JSONObject.parseObject(testPlan1.getModuleCode(), List.class);
                testCaseList= caseMapper.queryPageData(0,1024,tcase,modules);
                LogUtil.info("测试计划开始执行，当前执行计划："+testPlan1.getPlanDesc());
                for(TestCase testCase1:testCaseList){
                    LogUtil.info("执行测试计划下用例，用例id="+testCase1.getId());
                    TestCase testCase=caseMapper.querySingleCase(String.valueOf(testCase1.getId()));
                    ReqRequest reqRequest=new ReqRequest();
                    reqRequest.setProjectCode(testCase1.getProjectCode());
                    reqRequest.setModuleCode(testCase1.getModuleCode());
                    reqRequest.setEnv(testPlan1.getEnvCode());   //执行环境
                    reqRequest.setId(String.valueOf(testCase.getId()));
                    TestCase reqPre= JSONObject.parseObject(testCase.getPreData(),TestCase.class);
                    List<ReqCaseData> reqInterfaceDatas= JSONArray.parseArray(testCase.getCaseData(), ReqCaseData.class);
                    reqRequest.setReqPre(reqPre);
                    reqRequest.setReqCaseDatas(reqInterfaceDatas);
                    reqRequest.setPlanID(String.valueOf(testPlan1.getId()));
                    Future<List<CaseLog>> result = threadPool.submit(new Callable<List<CaseLog>>() {
                        @Override
                        public List<CaseLog> call() throws Exception {
                            if(testCase1.getServiceType().equals("0")){
                                return caseService.runCase(reqRequest);
                            }else{
                                List<CaseLog> caseLogList1= (List<CaseLog>) caseService.invokeMethod(reqRequest).get("logList");
                                return  caseLogList1;
                            }
                        }
                    });
                    futures.add(result);
                }
            }

            for(Future future:futures){
                caseLogList.addAll((Collection<? extends CaseLog>) future.get());
            }
            threadPool.shutdown();
            if(caseLogList.size()>0){
                int success=caseLogList.stream().filter(item-> item.getResult().equals("true")).collect(Collectors.toList()).size();
                int fail=caseLogList.stream().filter(item-> item.getResult().equals("false")).collect(Collectors.toList()).size();
                mailReportSummary.setTotal(caseLogList.size());
                mailReportSummary.setSuccess(success);
                mailReportSummary.setFailed(fail);
                mailReportSummary.setEndDate(DateUtil.formatToDayByDate3(new Date()));
                mailReportSummary.setData(caseLogList);
                LogUtil.info("测试计划执行结束，当前执行计划："+testPlan.getPlanDesc());
                msgReponseObject.setMsg("测试计划执行成功");
                reportService.updateReportCache(); //更新执行记录缓存
                reportService.updateSummaryCache();   //更新首页汇总缓存
                msgReponseObject.setCode(200);
                MsgResponseObject msgResponseObject=mailService.sendMailReport(mailReportSummary);
                if(msgResponseObject.getCode()==201){
                    return msgResponseObject;
                }
            }else{
                msgReponseObject.setMsg("测试计划执行异常，无可用测试用例执行");
                msgReponseObject.setCode(201);
            }

        }catch(Exception e){
            msgReponseObject.setMsg("测试计划执行异常，异常原因："+e.getMessage());
            msgReponseObject.setCode(201);
        }
        return  msgReponseObject;
    }



    /**
     * @description 定时执行
     * @author 星空梦语
     * @date 2021/2/27 下午12:04
     */
    public void timerRun(List<TestPlan> testPlanList,String projectCode) {
        TestPlan  testPlan=new TestPlan();
        testPlan.setProjectCode(projectCode);
        try{
            runTestPlan(testPlan,testPlanList);
        }catch (Exception e){
            LogUtil.err("定时任务执行异常，异常原因："+e.getMessage());
            e.printStackTrace();
        }

    }





}
