package com.runner.server.service.serviceImpl;

import com.runner.server.service.auth.AuthService;
import com.runner.server.service.utils.LogUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InitService implements CommandLineRunner {

    @Resource
    private ProjectService projectService;

    @Resource
    private EnvMachineService envMachineService;

    @Resource
    private CaseService caseService;

    @Resource
    private ReportService reportService;

    @Resource
    private InterfaceService interfaceService;

    @Resource
    private TestPlanService testPlanService;

    @Resource
    private ScheduleTaskService scheduleTaskService;

    @Resource
    private AuthService authService;

    @Resource
    private CipherService cipherService;

    @Resource
    private ZkService zkService;


    @Resource
    private DataBaseService dataBaseService;

    /**
     * @description 初始化数据
     * @author 星空梦语
     * @date 2020/4/19 15:11
     */
    public void initData() throws Exception {
        LogUtil.info("初始化加载配置缓存");
        projectService.updateProjectCache();
        projectService.updateProjectModuleCache();
        envMachineService.updateEnvMachineCache();
        interfaceService.updateProjectCache();
        caseService.updateTestCaseCache();
        reportService.updateReportCache();
        reportService.updateSummaryCache();
        testPlanService.updateTestPlanCache();
        scheduleTaskService.initTask();
        authService.updateUserCache();
        cipherService.updateCipherCache();
        zkService.initCheck();
    }

    @Override
    public void run(String... args) throws Exception {
        this.initData();
    }
}
