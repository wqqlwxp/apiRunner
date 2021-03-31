<template>
    <el-row >
        <el-row class="pading-20 my-border my-bg">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>首页</el-breadcrumb-item>
                    <el-breadcrumb-item>接口服务管理</el-breadcrumb-item>
                    <el-breadcrumb-item>dubbo接口</el-breadcrumb-item>
                </el-breadcrumb>
        </el-row>

        <el-row >
            <div class="operate">
                <el-input ref="queryAppText" v-model="queryAppText" size="small" placeholder="应用名称查询" style="width: 150px;margin-left: 10px;margin-right: 10px"></el-input>
                <el-input ref="queryInterfaceText" v-model="queryInterfaceText" size="small" placeholder="接口名称查询" style="width: 300px;margin-left: 10px;margin-right: 10px"></el-input>

                <el-button  size="small" type="primary" icon="el-icon-search"  @click="query">查询</el-button>
                <el-button  size="small" type="primary" icon="el-icon-delete"  @click="clear">清空</el-button>
            </div>

        </el-row>

        <el-row style="margin-top: 20px">
            <el-table :data="tableDetailData" style="width: 100%"  >
                <el-table-column  fixed="left" align="center" label="序号" width="100" >
                    <template  slot-scope="{$index}">
                        <span >{{(currentPage-1)*pageSize+$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column v-for="(item,key) in tableZKDataTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width">
                    <template  slot-scope="{row}">
                        <template >
                            <span>{{row[item.prop]}}</span>
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
                tableZKDataTitle:templateData.tableZKDataTitle,
                tableDetailData:[],
                currentPage:1,
                pageSize: 5,
                totalSize: 100,
                pageCount:1,
                queryAppText:'',
                queryInterfaceText:''
            }
        },
        methods:{
            handleSizeChange(size){
                this.currentPage = 1;//每次改变每页多少条都会重置当前页码为1
                this.pageSize = size;
                this.getData(this.currentPage,this.pageSize,this.queryAppText,this.queryInterfaceText)
            },
            handleCurrentChange(size){
                this.currentPage = size;
                this.getData(this.currentPage,this.pageSize,this.queryAppText,this.queryInterfaceText)
            },
            getData(offset,limit,appName,interfaceText){
                this.editable=[];
                this.changeFlag=false;
                let var1={}, pageObject={};
                var1['appName']=appName;
                var1['interfaceName']=interfaceText;
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                post('api/zk/queryAllZkData',JSON.stringify(pageObject)).then((response)=> {
                            this.tableDetailData=response.data.rows;
                            this.totalSize=response.data.total;
                            this.pageCount=Math.ceil(this.totalSize/this.pageSize);
                    }).catch( (error)=> {
                        // this.$message.error('操作异常，'+error);
                })
            },
            query(){
                this.getData(1,this.pageSize,this.queryAppText,this.queryInterfaceText);
            },
            clear(){
                this.queryAppText='';
                this.queryInterfaceText='';
                this.getData(1,this.pageSize,this.queryAppText,this.queryInterfaceText)
            }

        },
        mounted() {
            this.getData(1,this.pageSize,this.queryAppText,this.queryInterfaceText);
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