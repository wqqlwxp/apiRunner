
//导航栏数据
let navMenu=[
    {
        path: '/main',
        icon:'el-icon-s-data',
        title: '首页',
    },
    {
        path: '/jkgl',
        icon:'el-icon-s-help',
        title: '接口服务管理',
        children:[
            {
                path: "/goInterface",
                icon:'el-icon-s-help',
                title:'普通接口',

            }, {
                path: "/goDubboInterface",
                icon:'el-icon-s-help',
                title:'dubbo接口',

            }
        ]
    },
    {
        path: '/zdhcs',
        icon:'el-icon-document',
        title: '测试用例管理',
        children:[
            {
                path: "/goCase",
                icon:'el-icon-document',
                title:'用例维护',
            }
        ]
    },
    {
        path: '/dhgjx',
        icon:'el-icon-date',
        title: '测试计划管理',
        children:[
            {
                path: "/goTestPlan",
                icon:'el-icon-date',
                title:'计划维护',
            }
        ]
    },
    {
        path: '/dqgjx',
        icon:'el-icon-reading',
        title: '项目管理',
        children:[
            {
                path: "/goProject",
                icon:'el-icon-reading',
                title:'项目维护',
            },
            {
                path: "/goModule",
                icon:'el-icon-menu',
                title:' 模块维护',
            }
        ]
    },
    {
        path: '/pzgl',
        icon:'el-icon-coin',
        title: '配置管理',
        children:[
            {
                path: "/goEnv",
                icon:'el-icon-connection',
                title:'环境配置',
            },
            {
                path: "/goMail",
                icon:'el-icon-s-promotion',
                title:'邮件配置',
            },
            {
                path: "/goCipher",
                icon:'el-icon-key',
                title:'密钥配置',
            },
            {
                path: "/goZookeeper",
                icon:'el-icon-key',
                title:'ZK配置',
            }
        ]
    },
    {
        path: '/tygjx',
        icon:'el-icon-loading',
        title: '定时管理',
        children:[
            {
                path: "/goScheduleTask",
                icon:'el-icon-loading',
                title:'定时维护',
            }
        ]
    },
    {
        path: '/bggl',
        icon:'el-icon-view',
        title: '报告查看',
        children:[
            {
                path: "/goReportDetail",
                icon:'el-icon-view',
                title:'报告明细',
            }
        ]
    },
    {
        path: '/yhgl',
        icon:'el-icon-user',
        title: '权限管理',
        children:[
            {
                path: "/goUser",
                icon:'el-icon-user',
                title:'用户维护',
            }
        ]
    }
];



let tableProjectTitle=[{label:'项目名称',prop:'projectName',width:'150',},{label:'项目编码',prop:'projectCode',width:'100',},{label:'状态',prop:'status',width:'250',},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'200',}];
let tableProjectInitData={"projectName":"","projectCode":"","status":"0","operater":"","createTime":""};


let tableModuleTitle=[{label:'所属项目',prop:'projectCode',width:'150',},{label:'模块名称',prop:'moduleName',width:'150',},{label:'模块编码',prop:'moduleCode',width:'100',},{label:'状态',prop:'status',width:'250',},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'250',}];
let tableModuleInitData={"projectCode":"","moduleName":"","moduleCode":"","status":"0","operater":"","createTime":""};


let tableInterfaceTitle=[{label:'所属项目',prop:'projectCode',width:'150',},{label:'所属模块',prop:'moduleCode',width:'150',},{label:'接口地址',prop:'interfaceUrl',width:'450',},{label:'协议类型',prop:'interfaceType',width:'150',},{label:'请求类型',prop:'requestType',width:'150',},{label:'接口描述',prop:'interfaceDesc',width:'250',},{label:'状态',prop:'status',width:'150',},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'200',}];
let tableInterfaceInitData={"projectCode":"","moduleCode":"","interfaceUrl":"","interfaceType":"http","requestType":"post","interfaceDesc":"","status":"0","operater":"","createTime":""};


let tableEnvTitle=[{label:'所属项目',prop:'projectCode',width:'150',},{label:'环境名称',prop:'envName',width:'150',},{label:'环境编码',prop:'envCode',width:'150',},{label:'环境地址',prop:'ip',width:'300',},{label:'环境端口',prop:'port',width:'150'},{label:'状态',prop:'status',width:'150',},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'150',}];
let tableEnvInitData={"projectCode":"","envName":"","envCode":"","ip":"","port":"","status":"0","operater":"","createTime":""};

let tableCaseTitle=[{label:'用例id',prop:'id',width:'150',},{label:'所属项目',prop:'projectCode',width:'150',},{label:'所属模块',prop:'moduleCode',width:'150',},{label:'用例描述',prop:'caseDesc',width:'350',},{label:'用例类型',prop:'type',width:'150',},{label:'服务类型',prop:'serviceType',width:'150',},{label:'用例维度执行环境',prop:'envCode',width:'150',},{label:'用例状态',prop:'status',width:'150',},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'200',}];
let tableCaseInitData={"id":"","projectCode":"","moduleName":"","moduleCode":"","type":"1","serviceType":"0","envCode":"","caseDesc":"","status":"0","operater":"","createTime":""};


let tableHeaderTitle=[{label:'标签',prop:'key',width:'500',},{label:'内容',prop:'value',width:'500',}];
let tableHeaderInitData={"key":"","value":""};

let tableParamTitle=[{label:'参数名',prop:'key',width:'300',},{label:'参数值',prop:'value',width:'600',}];
let tableParamInitData={"key":"","value":""};


let tableVerifTitle=[{label:'json父节点',prop:'verifParentKey',width:'300',},{label:'校验json节点',prop:'verifKey',width:'300',},{label:'判断',prop:'verifType',width:'100',},{label:'预期结果',prop:'verifValue',width:'300',}];
let tableVerifInitData={"verifParentKey":"","verifKey":"","verifType":"=","verifValue":""};

//测试数据
let testData= [{"tabIndex":"header","interfaceData":{"id":8,"projectCode":"sso","moduleCode":"sso","interfaceUrl":"/test/comapny/queryCompanyNameBycode","interfaceType":"http","requestType":"post","interfaceDesc":"查询公司名称接口","status":"0"},"reqBody":{"jsonData":"","data":[{"key":"header","value":"1111"}],"type":"formData"},"reqHeaderList":[{"key":"header","value":"111"}],"verifData":{"jsonData":[{"verifParentKey":"","verifKey":"","verifValue":""}],"data":"json数据拉啊啊啊啊啊","type":"all"}},{"tabIndex":"header","interfaceData":{"id":9,"projectCode":"sso","moduleCode":"sso","interfaceUrl":"/test/comapny/queryCompanyNameBycode","interfaceType":"http","requestType":"post","interfaceDesc":"查询公司名称接口","status":"0"},"reqBody":{"jsonData":"ererer","data":[{"key":"","value":""}],"type":"json"},"reqHeaderList":[{"key":"header","value":"111"}],"verifData":{"jsonData":[{"verifParentKey":"header","verifKey":"111","verifValue":"111"}],"data":"","type":"json"}}];

//用例接口新增模板数据
let tableCaseInterfaceAddData={"tabIndex":"header","reqInterface":{"id":"","projectCode":"","moduleCode":"","interfaceUrl":"","interfaceType":"","requestType":"","interfaceDesc":"","status":""},"reqBodys":{"jsonData":"","data":[{"key":"","value":"","isEncrypt":true}],"type":"none"},"reqHeaders":[{"key":"","value":""}],"reqVerifs":{"jsonData":[{"verifParentKey":"","verifKey":"","verifType":"=","verifValue":""}],"data":"","type":"none"}};
let tableDubboCaseInterfaceAddData={"tabIndex":"param","zkId":"","zkApp":"","zkInterface":"","zkMethod":"","reqInterface":{"id":"","projectCode":"","moduleCode":"","interfaceUrl":"","interfaceType":"","requestType":"","interfaceDesc":"","status":""},"reqBodys":{"jsonData":"","type":"json"},"reqVerifs":{"jsonData":[{"verifParentKey":"","verifKey":"","verifType":"=","verifValue":""}],"data":"","type":"none"}};

//报告明细表头
let tableReportDetailTitle=[{label:'执行项目',prop:'projectCode',width:'150',},{label:'执行模块',prop:'moduleCode',width:'150'},{label:'用例id',prop:'caseId',width:'150'},{label:'用例类型',prop:'type',width:'150'},{label:'调用类型',prop:'serviceType',width:'150'},{label:'测试计划id',prop:'planId',width:'150'},{label:'执行环境',prop:'testEnv',width:'150'},{label:'执行结果',prop:'result',width:'150'},{label:'用例执行接口',prop:'interfaceDesc',width:'150'},{label:'用例接口相应数据',prop:'responseData',width:'200'},{label:'用例校验数据',prop:'verifData',width:'200'},{label:'异常消息',prop:'msg',width:'200'},{label:'执行时间',prop:'createTime',width:'200'}];

let tableVerifViewTitle=[{label:'校验结果',prop:'result',width:'150'},{label:'校验父节点',prop:'verifParentKey',width:'150',},{label:'校验节点',prop:'verifKey',width:'150'},{label:'判断符',prop:'verifType',width:'150'},{label:'预期结果',prop:'verifValue',width:'150'},{label:'实际结果',prop:'actualValue',width:'150'}];



let tableTestPlanTitle=[{label:'所属项目',prop:'projectCode',width:'150',},{label:'所属模块',prop:'moduleCode',width:'150',},{label:'调用类型',prop:'serviceType',width:'150',},{label:'计划执行环境',prop:'envCode',width:'150',},{label:'测试计划描述',prop:'planDesc',width:'200'},{label:'状态',prop:'status',width:'100'},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'150'}];
let tableTestPlanInitData={"projectCode":"","moduleCode":"","serviceType":"","envCode":"","planDesc":"","status":"0","operater":"","createTime":""};


let tableScheduleTaskTitle=[{label:'测试计划项目',prop:'projectCode',width:'200',},{label:'cron表达式',prop:'cron',width:'250',},{label:'状态',prop:'status',width:'150',},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'150'}];
let tableScheduleTaskInitData={"projectCode":"","cron":"","testEnv":"","status":"0","operater":"","createTime":""};


let tableUsersTitle=[{label:'用户账号',prop:'userAccount',width:'200',},{label:'用户密码',prop:'userPassword',width:'300',},{label:'用户昵称',prop:'nickName',width:'200',},{label:'权限',prop:'permission',width:'150'},{label:'状态',prop:'status',width:'150'},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'150'}];
let tableUsersInitData={"userAccount":"","userPassword":"","nickName":"","permission":"","status":"0","operater":"","createTime":""};

let allPermission= [{label:'接口管理-接口编辑',key:'interface_edit'},{label:'接口管理-接口保存',key:'interface_save'},{label:'接口管理-接口删除',key:'interface_del'},{label:'用例管理-用例编辑',key:'case_edit'},{label:'用例管理-用例保存',key:'case_save'},{label:'用例管理-用例删除',key:'case_del'},{label:'用例管理-明细执行',key:'case_detail_run'},{label:'用例管理-明细保存',key:'case_detail_save'},{label:'计划管理-计划编辑',key:'plan_edit'},{label:'计划管理-计划保存',key:'plan_save'},{label:'计划管理-计划删除',key:'plan_del'},{label:'项目管理-项目编辑',key:'project_edit'},{label:'项目管理-项目保存',key:'project_save',},{label:'项目管理-项目删除',key:'project_del'},{label:'项目管理-模块编辑',key:'module_edit'},{label:'项目管理-模块保存',key:'module_save'},{label:'项目管理-模块删除',key:'module_del'},{label:'配置管理-环境编辑',key:'env_edit'},{label:'配置管理-环境保存',key:'env_save'},{label:'配置管理-环境删除',key:'env_del'},{label:'定时管理-定时编辑',key:'timer_edit'},{label:'定时管理-定时保存',key:'timer_save'},{label:'定时管理-定时删除',key:'timer_del'},{label:'权限管理-用户编辑',key:'user_edit'},{label:'权限管理-用户保存',key:'user_save'},{label:'权限管理-用户删除',key:'user_del'},{label:'配置管理-zk保存',key:'zk_save'},{label:'配置管理-zk删除',key:'zk_del'},{label:'配置管理-zk刷新',key:'zk_refresh'},{label:'配置管理-zk服务创建',key:'zk_createApp'}];


let tableMailTitle=[{label:'邮件项目维度',prop:'projectCode',width:'200',},{label:'邮件标题',prop:'mailTitle',width:'200',},{label:'收件人(,分割)',prop:'mailReceive',width:'200',},{label:'状态',prop:'status',width:'150'},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'150'}];
let tableMailInitData={"projectCode":"","mailTitle":"","mailReceive":"","status":"0","operater":"","createTime":""};


let tableCipherTitle=[{label:'所属项目',prop:'projectCode',width:'150',},{label:'所属模块',prop:'moduleCode',width:'150',},{label:'加解密类型',prop:'type',width:'150',},{label:'同模块执行顺序',prop:'executeOrder',width:'150'},{label:'密文',prop:'keyData',width:'200'},{label:'偏移量',prop:'iv',width:'200'},{label:'密文解析表达式',prop:'cipherPath',width:'200'},{label:'请求是否加密',prop:'requestEncrypt',width:'150'},{label:'响应是否解密',prop:'responseDecrypt',width:'150'},{label:'状态',prop:'status',width:'150'},{label:'最后操作人',prop:'operater',width:'150'},{label:'创建时间',prop:'createTime',width:'200'}];
let tableCipherInitData={"projectCode":"","moduleCode":"","type":"","executeOrder":"1","keyData":"","iv":"","cipherPath":"","requestEncrypt":"","responseDecrypt":"","status":"0","operater":"","createTime":""};


let tableZKTitle=[{label:'zk别名',prop:'zkAlias',width:'150',},{label:'zk地址',prop:'zkIp',width:'250',},{label:'zk密码',prop:'zkPassword',width:'150',},{label:'创建时间',prop:'createTime',width:'200'}];
let tableZKInitData={"zkAlias":"","zkIp":"","zkPassword":"","createTime":""};


let tableZKDataTitle=[{label:'zk环境',prop:'zkAlias',width:'150',},{label:'应用',prop:'appName',width:'200',},{label:'接口',prop:'interfaceName',width:'500',},{label:'方法',prop:'methodName',width:'350'},{label:'版本',prop:'version',width:'150'}];


export default{
    navMenu,tableProjectTitle,tableProjectInitData,tableModuleTitle,tableModuleInitData,tableInterfaceTitle,tableInterfaceInitData,
    tableEnvTitle,tableEnvInitData,tableCaseTitle,tableCaseInitData,tableHeaderTitle,tableHeaderInitData,tableParamTitle,tableParamInitData,
    tableVerifTitle,tableVerifInitData,testData,tableCaseInterfaceAddData,tableReportDetailTitle,tableVerifViewTitle,tableTestPlanTitle,tableTestPlanInitData,
    tableScheduleTaskTitle,tableScheduleTaskInitData,tableUsersTitle,tableUsersInitData,allPermission,tableMailTitle,tableMailInitData,
    tableCipherTitle,tableCipherInitData,tableZKTitle,tableZKInitData,tableZKDataTitle,tableDubboCaseInterfaceAddData
}