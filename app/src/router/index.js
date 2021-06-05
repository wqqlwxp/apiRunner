
import Vue from 'vue'
import Router from 'vue-router'
import index from "@/components/layout/Index";
import Main from "@/components/Main";
import NotFound from "@/components/NotFound";
import project from "@/views/testProject/project";
import module from "@/views/testProject/module";
import interfaceSet from "@/views/testInterface/interfaceSet";
import envSet from "@/views/testSet/envSet";
import caseSet from "@/views/testCase/caseSet";
import caseDetail from "@/views/testCase/caseDetail";
import reportDetail from "@/views/testReport/reportDetail";
import reportSummary from "@/views/testReport/reportSummary";
import testPlan from "@/views/testPlan/testPlan";
import timerTask from "@/views/timingTask/timerTask";
import user from "@/views/auth/user";
import login from "@/views/auth/login";
import mailSet from "@/views/testSet/mailSet";
import cipherSet from "@/views/testSet/cipherSet";
import zkSet from "@/views/testSet/zkSet";
import dubboServiceDetail from "@/views/testInterface/dubboServiceDetail";
import caseDubboDetail from "@/views/testCase/caseDubboDetail";
import databaseSet from "@/views/testSet/databaseSet";


const routes=[

    {
        path: "/404",
        name: "notFound",
        component: NotFound
    }, {
        path: "*", // 此处需特别注意置于最底部
        redirect: "/404"
    }, {
        path: "/login", // 此处需特别注意置于最底部
        name: "login",
        component:login
    },
    {
        path: '/',
        title: '首页',
        component: index,
        redirect:'/main',
        children:[
            {
                path: "/main",
                title:'首页',
                component:reportSummary
            },
            {
                path: "/goProject",
                title:'项目维护',
                component:project
            },
            {
                path: "/goModule",
                title:'模块维护',
                component:module
            },
            {
                path: "/goInterface",
                title:'接口维护',
                component:interfaceSet
            },
            {
                path: "/goEnv",
                title:'环境维护',
                component:envSet
            },
            {
                path: "/goCase",
                title:'用例维护',
                component:caseSet
            },
            {
                path: "/goCaseDetail",
                title:'用例明细',
                component:caseDetail
            },
            {
                path: "/goReportDetail",
                title:'报告明细',
                component:reportDetail
            },
            {
                path: "/goTestPlan",
                title:'测试计划',
                component:testPlan
            }
            ,
            {
                path: "/goScheduleTask",
                title:'定时任务',
                component:timerTask
            },
            {
                path: "/goUser",
                title:'用户维护',
                component:user
            },
            {
                path: "/goMail",
                title:'邮件维护',
                component:mailSet
            },
            {
                path: "/goCipher",
                title:'密钥维护',
                component:cipherSet
            },
            {
                path: "/goZookeeper",
                title:'ZK维护',
                component:zkSet
            },
            {
                path: "/goDubboInterface",
                title:'dubbo接口查看',
                component:dubboServiceDetail
            },
            {
                path: "/goCaseDubboDetail",
                title:'dubbo用例明细',
                component:caseDubboDetail
            },
            {
                path: "/goDatabaseSet",
                title:'数据库配置',
                component:databaseSet
            }

        ]
    },


];


Vue.use(Router);

const router=new Router({
    routes
});

const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
};

export default  router;