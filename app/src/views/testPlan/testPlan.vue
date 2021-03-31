<template>
    <el-row >
        <el-row class="pading-20 my-border my-bg">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>首页</el-breadcrumb-item>
                    <el-breadcrumb-item>测试计划管理</el-breadcrumb-item>
                    <el-breadcrumb-item>计划维护</el-breadcrumb-item>
                </el-breadcrumb>
        </el-row>

        <el-row >
            <div class="operate">
                <el-button  size="small" type="primary" @click="add" >新增</el-button>
                <el-input ref="queryProjectText" v-model="queryProjectText" size="small" placeholder="项目名称查询" style="width: 150px;margin-left: 20px;margin-right: 10px"></el-input>
                <el-button  size="small" type="primary" icon="el-icon-search"  @click="query">查询</el-button>
                <el-button  size="small" type="primary" icon="el-icon-delete"  @click="clear">清空</el-button>
            </div>
        </el-row>

        <el-row style="margin-top: 20px">
            <el-table :data="tableTestPlanData" style="width: 100%"  >
                <el-table-column  fixed="left" align="center" label="序号" width="100" >
                    <template  slot-scope="{$index}">
                        <span >{{(currentPage-1)*pageSize+$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column v-for="(item,key) in tableTestPlanTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width">
                    <template  slot-scope="{row,$index}">
                        <template v-if="filterList.indexOf(item.prop)==-1" >
                            <el-input v-if="editable[$index] && item.prop!='createTime' && item.prop!='operater'"   v-model="row[item.prop]"></el-input>
                            <span v-else>{{row[item.prop]}}</span>
                        </template>

                        <template  v-if="item.prop=='status'">
                            <el-select v-if="editable[$index]" v-model="row[item.prop]"  placeholder="请选择" >
                                <el-option label="启用" value="0"> </el-option>
                                <el-option label="关闭" value="1"> </el-option>
                            </el-select>
                            <el-tag v-if="!editable[$index] && row[item.prop]=='0'" type="success" >启用</el-tag>
                            <el-tag v-if="!editable[$index] && row[item.prop]=='1'" type="danger" >关闭</el-tag>
                        </template>


                        <template  v-if="item.prop=='serviceType'">
                            <el-select v-if="editable[$index]" v-model="row[item.prop]"  placeholder="请选择" >
                                <el-option label="普通类型" value="0"> </el-option>
                                <el-option label="dubbo类型" value="1"> </el-option>
                            </el-select>
                            <el-tag v-if="!editable[$index] && row[item.prop]=='0'" type="primary" >普通类型</el-tag>
                            <el-tag v-if="!editable[$index] && row[item.prop]=='1'" type="warning" >dubbo类型</el-tag>
                        </template>


                        <template  v-if="item.prop=='projectCode'">
                            <el-select v-if="editable[$index]" v-model="row[item.prop]"  placeholder="请选择"  @change="projectChangeEvent(row,row[item.prop])" >
                                <el-option v-for="obj in projectData" :key="obj.projectCode" :label="obj.projectName" :value="obj.projectCode" ></el-option>
                            </el-select>
                            <span v-else>{{filterProjectByCode(row[item.prop])}}</span>
                        </template>

                        <template  v-if="item.prop=='moduleCode'">
                            <el-select v-if="editable[$index]" v-model="row[item.prop]" multiple  placeholder="请选择">
                                <el-option v-for="item in moduleData" :key="item.moduleCode" :label="item.moduleName" :value="item.moduleCode"></el-option>
                            </el-select>
                            <span v-else>{{filterModuleByCode(row[item.prop])}}</span>
                        </template>


                        <template  v-if="item.prop=='envCode'">
                            <el-select v-if="editable[$index]" v-model="row[item.prop]"  placeholder="请选择" >
                                <el-option v-for="obj in envData" :key="obj.envCode" :label="obj.envName" :value="obj.envCode"></el-option>
                            </el-select>
                            <span v-else>{{filterEnvByCode(row,row[item.prop])}}</span>
                        </template>

                    </template>
                </el-table-column>

                <el-table-column  fixed="right" align="center" label="操作" width="300">
                    <template  slot-scope="{row,$index}">
                        <el-button v-per="'plan_edit'"  size="small"  type="primary" icon="el-icon-edit" @click="change(row,$index,true)" circle></el-button>
                        <el-button v-per="'plan_save'"   size="small" type="success" icon="el-icon-check" @click="save(row,$index)" circle></el-button>
                        <el-button   size="small" v-if="!editable[$index]" type="success" icon="el-icon-caret-right"  circle  @click="runPlan(row)"></el-button>
                        <el-button v-per="'plan_del'"   size="small" type="danger" icon="el-icon-delete" @click="remove(row)" circle></el-button>
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
    import {isNotEmpty} from "../../utils/utils.js";
    import {post} from "../../utils/httpUtils.js";
    import {queryBaseProject} from "../../utils/httpUtils.js";
    import {queryBaseModule} from "../../utils/httpUtils.js";
    import {queryBaseEnv} from "../../utils/httpUtils.js";
    import {filterProjectByCode} from "../../utils/utils.js";
    import {filterModuleByCode}  from  "../../utils/utils.js";
    import {filterEnvByCode} from "../../utils/utils.js";
    import {queryAuthName} from "../../utils/utils.js";



    export default {
        name: "testPlan",
        data(){
            return{
                tableTestPlanTitle:templateData.tableTestPlanTitle,
                tableTestPlanInitData:templateData.tableTestPlanInitData,
                tableTestPlanData:[],
                currentPage:1,
                pageSize: 5,
                totalSize: 100,
                pageCount:1,
                queryProjectText:'',
                editable:[],
                projectData:[],
                moduleData:[],
                moduleAllData:[],
                changeFlag:false,
                envData:[],
                envAllData:[],
                filterList:['status','projectCode','moduleCode','envCode','serviceType'],
            }
        },
        methods:{
            filterProjectByCode(param){
                return filterProjectByCode(param,this.projectData)
            },

            filterModuleByCode(param){
                let data='';
                let that=this;
                if(param instanceof Array){
                    param.forEach(function(item,index){
                        let name=filterModuleByCode(item,that.moduleAllData);
                        data+=','+name
                    });
                    return data.substring(1)
                }
            },
            filterEnvByCode(row,param){
                return filterEnvByCode(row,param,this.envAllData)
            },
            handleSizeChange(size){
                this.currentPage = 1;//每次改变每页多少条都会重置当前页码为1
                this.pageSize = size;
                this.getData(this.currentPage,this.pageSize,this.queryProjectText)
            },
            handleCurrentChange(size){
                this.currentPage = size;
                this.getData(this.currentPage,this.pageSize,this.queryProjectText)
            },
            projectChangeEvent(row,code,type){
                this.moduleData=this.moduleAllData;
                let data=[];
                this.moduleData.forEach(function(item,index){
                    if(code==item.projectCode){
                        data.push(item);
                    }
                });
                this.moduleData=data;
                row.moduleCode.forEach(function (item,index) {
                    if(JSON.stringify(data).indexOf(item)==-1)  row.moduleCode='';
                });

                this.envData=this.envAllData;
                let data2=[],envFlag=false;
                this.envData.forEach(function(item,index){
                    if(row.projectCode==item.projectCode ){   //环境配置筛选根据项目决定
                        data2.push(item);
                        if(row.envCode==item.envCode) envFlag=true;
                    }
                });
                this.envData=data2;
                if(!envFlag) row.envCode='';
            },
            runPlan(row){
                if(this.changeFlag){
                    this.$alert('存在修改中的数据，请确认', '提示', {confirmButtonText: '确定'});
                    return
                }
                this.$alert('测试计划执行中，请稍后', '提示', {confirmButtonText: '确定'});
                this.editable=[];
                this.changeFlag=false;
                row.moduleCode=JSON.stringify(row.moduleCode);
                post('api/plan/runTestPlan',JSON.stringify(row)).then((response)=> {
                    if(response.data.code=='200'){
                        this.getData(1,this.pageSize,this.queryProjectText);
                        this.$message({type: 'success', message: response.data.msg});
                    }else{
                        this.$message.error('操作异常，'+response.data.msg);
                    }
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                })
            },
            getData(offset,limit,project){
                this.editable=[];
                this.changeFlag=false;
                let var1={}, pageObject={};
                var1['projectName']=project;
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                post('api/plan/queryAllTestPlan',JSON.stringify(pageObject)).then((response)=> {
                    let obj=response.data.rows;
                    obj.forEach(function (item,index) {
                        if(item.moduleCode!='' && item.moduleCode!=null){
                            item.moduleCode= JSON.parse(item.moduleCode)
                        }else{
                            item.moduleCode=[]
                        }
                    });
                    this.tableTestPlanData=obj;
                    this.totalSize=response.data.total;
                    this.pageCount=Math.ceil(this.totalSize/this.pageSize);
                    this.change(null,0,false);
                    }).catch( (error)=> {
                        this.$message.error('操作异常，'+error);
                })
            },
            query(){
                this.getData(1,this.pageSize,this.queryProjectText);
            },
            clear(){
                this.queryProjectText='';
                this.getData(1,this.pageSize,'')
            },
            change(row,index,flag){
                if(this.changeFlag){
                    this.$alert('存在修改中的数据，请确认', '提示', {confirmButtonText: '确定'});
                    return
                }
                this.editable[index] = flag;
                this.$set(this.editable, index, flag);
                if(row!=null){
                    this.projectChangeEvent(row,row.projectCode);
                    this.changeFlag=true;
                }
            },
            save(row,index){
                if(!this.editable[index]) {
                    this.$alert('修改后才能进行保存', '提示', {confirmButtonText: '确定'});
                    return
                }
                if(!isNotEmpty(row.projectCode) || !isNotEmpty(row.moduleCode)  || !isNotEmpty(row.envCode)){
                    this.$message({type: 'warning', message: '数据不能为空!'});
                    return
                }
                this.changeFlag=false;
                let  pageObject={};
                row.operater=queryAuthName();
                row.moduleCode=JSON.stringify(row.moduleCode);
                pageObject["data"]=row;
                post('api/plan/editTestPlan',JSON.stringify(pageObject)).then((response)=> {
                    if(response.data.code=='200'){
                        this.getData(1,this.pageSize,this.queryProjectText);
                        this.$message({type: 'success', message: response.data.msg});
                    }else{
                        this.$message.error('操作异常，'+response.data.msg);
                    }
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                });
            },
            remove(row){
                if(!isNotEmpty(row.id)){
                    this.$message.error('新增未保存数据无法删除，刷新页面即可!');
                    return
                }
                this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
                    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
                }).then(() => {
                    post('api/plan/deleteTestPlan',row.id).then((response)=> {
                        if(response.data.code=='200'){
                            this.currentPage=1;
                            this.getData(1,this.pageSize,this.queryProjectText);
                            this.$message({type: 'success', message: response.data.msg});
                        }else{
                            this.$message.error('操作异常，'+response.data.msg);
                        }
                    }).catch( (error)=> {
                        this.$message.error('操作异常，'+error);
                    });
                });
            },
            add(){
                if(this.editable[0]){
                    this.$message({type: 'warning', message: '请保存新增数据后再进行操作!'});
                    return
                }
                this.tableTestPlanData.unshift(JSON.parse(JSON.stringify(this.tableTestPlanInitData)));
                this.change(null,0,true);
                this.changeFlag=true;
            },
            queryProject(offset,limit,projectCode){
                queryBaseProject(offset,limit,projectCode).then((response)=>{
                    this.projectData=response.data.rows
                })
            },
            queryModuLe(offset,limit,projectCode){
                queryBaseModule(offset,limit,projectCode).then((response)=>{
                    this.moduleData=response.data.rows;
                    this.moduleAllData=response.data.rows
                });
            },
            queryEnv(offset,limit,projectCode){    //查询全部环境配置数据
                queryBaseEnv(offset,limit,projectCode).then((response)=>{
                    this.envData=response.data.rows;
                    this.envAllData=response.data.rows;
                });
            }

        },
        mounted() {
            this.changeFlag=false;
            this.getData(1,this.pageSize,'');
            this.queryProject(1,this.totalSize,'');
            this.queryModuLe(1,this.totalSize,'');
            this.queryEnv(1,this.totalSize,'','');
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