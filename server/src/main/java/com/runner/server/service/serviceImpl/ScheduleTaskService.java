package com.runner.server.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.ScheduleTask;
import com.runner.server.dao.entity.po.TestPlan;
import com.runner.server.dao.mapper.ScheduleTaskMapper;
import com.runner.server.dao.mapper.TestPlanMapper;
import com.runner.server.service.utils.DateUtil;
import com.runner.server.service.utils.LogUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduleTaskService{

    @Resource
    private ScheduleTaskMapper scheduleTaskMapper;

    @Resource
    private TestPlanService testPlanService;

    @Resource
    private TestPlanMapper testPlanMapper;

    private MsgResponseObject msgReponseObject=new MsgResponseObject() ;

    private static ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    private static Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    static {
        threadPoolTaskScheduler.initialize();
        System.out.println("定时任务线程池启动");
    }




    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        ScheduleTask scheduleTask=new ScheduleTask();
        BeanUtils.populate(scheduleTask, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<ScheduleTask> list=scheduleTaskMapper.queryPageData(offset,pageObject.getLimit(),scheduleTask);
        int total=scheduleTaskMapper.queryCountByExample(scheduleTask);
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
    public MsgResponseObject edit(ScheduleTask scheduleTask) throws Exception {
        msgReponseObject.setCode(201);
        if(StringUtils.isBlank(scheduleTask.getProjectCode()) ||  StringUtils.isBlank(scheduleTask.getCron())){
            msgReponseObject.setMsg("数据不能为空，请确认");
            return  msgReponseObject;
        }

        String name=scheduleTask.getProjectName();
        if(scheduleTask.getId()!=null){
            scheduleTask.setProjectName(null);
            Example example=new Example(ScheduleTask.class);
            example.createCriteria().andEqualTo("id",scheduleTask.getId());
            msgReponseObject.setMsg("修改失败");
            if(scheduleTaskMapper.updateByExampleSelective(scheduleTask,example)>0){
                msgReponseObject.setMsg("修改成功");
                msgReponseObject.setCode(200);
                scheduleTask.setProjectName(name);
                reset(scheduleTask);
            }
        }else{
            scheduleTask.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(scheduleTaskMapper.insertSelective(scheduleTask)>0){
                msgReponseObject.setMsg("新增成功");
                msgReponseObject.setCode(200);
                reset(scheduleTask);
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
    public MsgResponseObject delete(ScheduleTask scheduleTask) throws Exception {
        Example example=new Example(TestPlan.class);
        example.createCriteria().andEqualTo("id",scheduleTask.getId());
        msgReponseObject.setMsg("删除失败");
        if(scheduleTaskMapper.deleteByExample(example)>0){
            msgReponseObject.setMsg("删除成功");
            msgReponseObject.setCode(200);
            cancel(scheduleTask);
        }
        return msgReponseObject;
    }


    /**
     * @description  定时任务执行
     * @author 星空梦语
     * @date 2021/2/23 下午4:30
     */
    public void initTask(){
        LogUtil.info("项目启动初始化定时任务...");
        List<ScheduleTask> scheduleTaskList=scheduleTaskMapper.queryAll();
        scheduleTaskList.forEach(scheduleTask -> {
            start(scheduleTask);
        });
    }




    /**
     * @description  启动定时任务
     * @author 星空梦语
     * @date 2021/2/23 下午4:27
     */
    public  void start(ScheduleTask scheduleTask) {
        System.out.println("启动定时任务线程项目名称:" + scheduleTask.getProjectName());
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(getRunnable(scheduleTask), new CronTrigger(scheduleTask.getCron()));
        scheduledFutureMap.put(scheduleTask.getId(), scheduledFuture);
    }


    /**
     * @description  关闭定时任务
     * @author 星空梦语
     * @date 2021/2/23 下午4:27
     */
    public  void cancel(ScheduleTask scheduleTask) {
        System.out.println("关闭定时任务线程项目名称:" + scheduleTask.getProjectName());
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(scheduleTask.getId());
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
        scheduledFutureMap.remove(scheduleTask.getId());
    }


    /**
     * 重置定时任务
     * @param scheduleTask
     * @return
     */
    public  void reset(ScheduleTask scheduleTask) {
        if(scheduleTask.getStatus().equals("0")){
            cancel(scheduleTask);
            start(scheduleTask);
        }else{
            cancel(scheduleTask);
        }
    }

    private  Runnable getRunnable(ScheduleTask scheduleTask){
        return new Runnable() {
            @Override
            public void run() {
                LogUtil.info("定时任务，测试计划开始执行------------------>>>>>>>>>>>>>>>>>>");
                TestPlan testPlan = new TestPlan();
                testPlan.setProjectCode(scheduleTask.getProjectCode());
                testPlan.setStatus("0");
                List<TestPlan> testPlanList = testPlanMapper.queryPageData(0, 1024, testPlan);
                testPlanService.timerRun(testPlanList,scheduleTask.getProjectCode());
            }
        };
    }


}
