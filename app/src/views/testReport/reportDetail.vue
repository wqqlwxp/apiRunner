<template>
    <el-row >
        <el-row class="pading-20 my-border my-bg">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>首页</el-breadcrumb-item>
                    <el-breadcrumb-item>报告查看</el-breadcrumb-item>
                    <el-breadcrumb-item>报告明细</el-breadcrumb-item>
                </el-breadcrumb>
        </el-row>

        <el-row >
            <div class="operate">
                <el-input ref="queryProjectText" v-model="queryProjectText" size="small" placeholder="项目名称查询" style="width: 150px;margin-left: 10px;margin-right: 10px"></el-input>
                <el-input ref="queryModuleText" v-model="queryModuleText" size="small" placeholder="模块名称查询" style="width: 150px;margin-left: 10px;margin-right: 10px"></el-input>
                <el-input ref="queryModuleText" v-model="queryCaseIdText" size="small" placeholder="用例id查询" style="width: 150px;margin-left: 10px;margin-right: 10px"></el-input>
                <el-button  size="small" type="primary" icon="el-icon-search"  @click="query">查询</el-button>
                <el-button  size="small" type="primary" icon="el-icon-delete"  @click="clear">清空</el-button>
            </div>

        </el-row>

        <el-row style="margin-top: 20px">
            <el-table :data="tableReportDetailData" style="width: 100%"  >
                <el-table-column  fixed="left" align="center" label="序号" width="100" >
                    <template  slot-scope="{$index}">
                        <span >{{(currentPage-1)*pageSize+$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column v-for="(item,key) in tableReportDetailTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width">
                    <template  slot-scope="{row}">
                        <template  v-if="item.prop=='caseId' || item.prop=='createTime' || item.prop=='planId' ">
                            <span>{{row[item.prop]}}</span>
                        </template>

                        <template  v-if="item.prop=='responseData'">
                            <el-popover placement="top" title="用例接口原始响应数据" width="400" trigger="click" :content="row[item.prop]">
                                <el-button type="text" slot="reference">查看接口原始响应数据</el-button>
                            </el-popover>
                        </template>



                        <template  v-if="item.prop=='verifData'">
                            <el-popover  placement="top" title="用例接口校验数据" width="100%" trigger="click">
                                <template v-if="row[item.prop]!=null && row[item.prop]!=''" >
                                    <el-table  :data="JSON.parse(row[item.prop])" style="width: 100%"  >
                                        <el-table-column v-for="(item,key) in tableVerifViewTitle" :key="key" :prop="item.prop" align="center" :label="item.label"  :width="150">
                                            <template   slot-scope="{row}">
                                                <template  v-if="item.prop=='result'">
                                                    <el-tag  v-if="row[item.prop]==true" type="success">成功</el-tag>
                                                    <el-tag  v-else type="danger">失败</el-tag>
                                                </template>
                                                <template v-else>
                                                    <span >{{row[item.prop]}}</span>
                                                </template>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </template>
                                <span v-else>用例接口未配置校验参数</span>
                                <el-button type="text" slot="reference">查看用例接口校验数据</el-button>
                            </el-popover>
                        </template>


                        <template  v-if="item.prop=='projectCode'">
                            <span>{{filterProjectByCode(row[item.prop])}}</span>
                        </template>

                        <template  v-if="item.prop=='moduleCode'">
                            <span>{{filterModuleByCode(row[item.prop])}}</span>
                        </template>

                        <template  v-if="item.prop=='type'">
                            <el-tag v-if="row[item.prop]=='1'"  effect="plain">正常用例</el-tag>
                            <el-tag v-if="row[item.prop]=='0'" type="warning"  effect="plain">前置用例</el-tag>
                        </template>

                        <template  v-if="item.prop=='testEnv'">
                            <span>{{filterEnvByCode(row,row[item.prop])}}</span>
                        </template>

                        <template  v-if="item.prop=='result'">
                            <el-tag  v-if="row[item.prop]=='true'" type="success">成功</el-tag>
                            <el-tag  v-if="row[item.prop]=='false'" type="danger">失败</el-tag>
                        </template>


                    </template>
                </el-table-column>

            </el-table>
            <el-pagination  style="margin-top:30px;float: left"
                            background
                            layout="prev, pager, next"
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="currentPage"
                            :page-size="pageSize"
                            :page-count="pageCount">
            </el-pagination>
        </el-row>


    </el-row>
</template>

<script>
    import templateData from "../../../static/templateData";
    import {post} from "../../utils/httpUtils.js";
    import {queryBaseProject} from "../../utils/httpUtils.js";
    import {queryBaseModule} from "../../utils/httpUtils.js";
    import {queryBaseEnv} from "../../utils/httpUtils.js";
    import {filterProjectByCode} from "../../utils/utils.js";
    import {filterModuleByCode}  from  "../../utils/utils.js";
    import {filterEnvByCode} from "../../utils/utils.js";



    export default {
        name: "reportDetail",
        data(){
            return{
                tableReportDetailTitle:templateData.tableReportDetailTitle,
                tableReportDetailInitData:templateData.tableReportDetailInitData,
                tableVerifViewTitle:templateData.tableVerifViewTitle,
                tableReportDetailData:[],
                currentPage:1,
                pageSize: 5,
                totalSize: 100,
                pageCount:1,
                queryProjectText:'',
                queryModuleText:'',
                queryCaseIdText:'',
                projectData:[],
                moduleAllData:[],
                envAllData:[],
                filterList:['status','projectCode','moduleCode','interfaceType','requestType']
            }
        },
        methods:{
            filterProjectByCode(param){
                return filterProjectByCode(param,this.projectData)
            },

            filterModuleByCode(param){
                return filterModuleByCode(param,this.moduleAllData);
            },

            filterEnvByCode(row,param){
                return filterEnvByCode(row,param,this.envAllData)
            },
            handleSizeChange(size){
                this.currentPage = 1;//每次改变每页多少条都会重置当前页码为1
                this.pageSize = size;
                this.getData(this.currentPage,this.pageSize,this.queryProjectText,this.queryModuleText,this.queryCaseIdText)
            },
            handleCurrentChange(size){
                this.currentPage = size;
                this.getData(this.currentPage,this.pageSize,this.queryProjectText,this.queryModuleText,this.queryCaseIdText)
            },
            getData(offset,limit,project,module,caseId){
                this.editable=[];
                this.changeFlag=false;
                let var1={}, pageObject={};
                var1['projectName']=project;
                var1['moduleName']=module;
                var1['caseId']=caseId;
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                post('api/report/queryAllReportDetail',JSON.stringify(pageObject)).then((response)=> {
                            this.tableReportDetailData=response.data.rows;
                            this.totalSize=response.data.total;
                            this.pageCount=Math.ceil(this.totalSize/this.pageSize);
                    }).catch( (error)=> {
                        // this.$message.error('操作异常，'+error);
                })
            },
            query(){
                this.getData(1,this.pageSize,this.queryProjectText,this.queryModuleText,this.queryCaseIdText);
            },
            clear(){
                this.queryProjectText='';
                this.queryModuleText='';
                this.queryCaseIdText='';
                this.getData(1,this.pageSize,'','','')
            },

            queryProject(offset,limit,projectCode){
                queryBaseProject(offset,limit,projectCode).then((response)=>{
                    this.projectData=response.data.rows
                })
            },
            queryModuLe(offset,limit,projectCode){
                queryBaseModule(offset,limit,projectCode).then((response)=>{
                    this.moduleAllData=response.data.rows
                });
            },
            queryEnv(offset,limit,projectCode,moduleCode){    //查询全部环境配置数据
                queryBaseEnv(offset,limit,projectCode,moduleCode).then((response)=>{
                    this.envAllData=response.data.rows;
                });
            }

        },
        mounted() {
            this.getData(1,this.pageSize,'','','');
            this.queryProject(1,this.totalSize,'');
            this.queryModuLe(1,this.totalSize,'');
            this.queryEnv(1,this.totalSize,'','')
        }
    }

</script>

<style scoped>
    .my-border{
        border-radius:3px;
        border: 1px solid #ebebeb;
    }


    html,body{
        background-color: #fafafa;
    }
    .my-bg{
        background-color:#fff
    }

    .pading-20{
        padding: 20px;
    }
    .operate{
        float: left;
        margin-left: 20px;
        margin-top: 30px;
    }

</style>