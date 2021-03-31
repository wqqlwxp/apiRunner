<template>
    <el-row >
        <el-row class="pading-20 my-border my-bg">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>首页</el-breadcrumb-item>
                    <el-breadcrumb-item>配置管理</el-breadcrumb-item>
                    <el-breadcrumb-item>ZK维护</el-breadcrumb-item>
                </el-breadcrumb>
        </el-row>

        <el-row >
            <div class="operate">
                <el-button  size="small" type="primary" @click="add" >新增</el-button>
                <el-input ref="queryText" v-model="queryText" size="small" placeholder="别名查询" style="width: 150px;margin-left: 20px;margin-right: 10px"></el-input>
                <el-button  size="small" type="primary" icon="el-icon-search"  @click="query">查询</el-button>
                <el-button  size="small" type="primary" icon="el-icon-delete"  @click="clear">清空</el-button>
            </div>
        </el-row>


        <el-popover class="tips" placement="top-start" title="zk服务初始化..." width="130" v-model="visible" style="text-align: center;z-index:9999">
            <el-progress type="circle" :percentage="progressNum"></el-progress>
        </el-popover>

        <el-row style="margin-top: 20px">
            <el-table :data="tableZkData" style="width: 100%"  >
                <el-table-column  fixed="left" align="center" label="序号" width="100" >
                    <template  slot-scope="{$index}">
                        <span >{{(currentPage-1)*pageSize+$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column v-for="(item,key) in tableZKTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width">
                    <template slot-scope="{row,$index}">
                        <el-input v-if="editable[$index] && item.prop!='createTime'"   v-model="row[item.prop]"></el-input>
                        <span v-else>{{row[item.prop]}}</span>
                    </template>
                </el-table-column>
                <el-table-column  fixed="right" align="center" label="操作"  style="width:400px" >
                    <template  slot-scope="{row,$index}">
                        <el-button  v-per="'zk_save'"   slot="reference" size="small" type="success" icon="el-icon-check" @click="save(row,$index)" circle></el-button>
                        <el-popover placement="left" width="400" v-model="appvisible[$index]" trigger="manual">
                            <el-form ref="form" :model="form" label-width="100px">
                                <el-form-item label="应用名称">
                                        <el-select v-model="form.appName[$index]" placeholder="请选择应用">
                                            <el-option v-for="item in form.apps" :key="item" :label="item" :value="item"></el-option>
                                        </el-select>
                                </el-form-item>
                                <el-form-item label="maven依赖">
                                        <textarea v-model="form.mavenDependency[$index]" style="width: 300px;height: 150px" :placeholder="form.tips" class="mavenDependency"></textarea>
                                </el-form-item>
                                <el-form-item>
                                    <el-button size="small" @click="cancel($index)">取消</el-button>
                                    <el-button type="success" size="small" @click="createApp(row.zkIp,row.id,$index)"  >立即创建</el-button>
                                </el-form-item>
                            </el-form>
                            <el-button  v-per="'zk_createApp'"   size="small" type="primary" slot="reference"  @click="openApp(row.zkIp,$index)" round>创建服务</el-button>
                        </el-popover>
                        <el-button v-per="'zk_refresh'"  size="small" type="warning"  @click="refresh(row)" round>刷新ZK</el-button>
                        <el-button v-per="'zk_del'"   size="small" type="danger" icon="el-icon-delete" @click="remove(row)" circle></el-button>
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
    import {filterProjectByCode} from "../../utils/utils.js";
    import {filterModuleByCode}  from  "../../utils/utils.js";
    import {queryAuthName} from "../../utils/utils.js";



    export default {
        name: "zkSet",
        data(){
            return{
                tableZKTitle:templateData.tableZKTitle,
                tableZKInitData:templateData.tableZKInitData,
                tableZkData:[],
                currentPage:1,
                pageSize: 5,
                totalSize: 100,
                pageCount:1,
                queryText:'',
                editable:[],
                changeFlag:false,
                visible: false,
                appvisible:[],
                progressNum:0,
                form:{
                    appName:[],
                    mavenDependency:[],
                    apps:[],
                    tips:'maven坐标示例：\n' +
                        '<dependency>\n' +
                        '\t<groupId>com.xxx.xxx</groupId>\n' +
                        '\t<artifactId>java-service</artifactId>\n' +
                        '\t<version>1.0-SNAPSHOT</version>\n' +
                        '</dependency>'
                },


            }
        },
        methods:{
            handleSizeChange(size){
                this.currentPage = 1;//每次改变每页多少条都会重置当前页码为1
                this.pageSize = size;
                this.getData(this.currentPage,this.pageSize,this.queryText)
            },
            handleCurrentChange(size){
                this.currentPage = size;
                this.getData(this.currentPage,this.pageSize,this.queryText)
            },

            createApp(zkAddress,zkId,index){
                let appName=this.form.appName[index];
                let mavenDependency=this.form.mavenDependency[index];
                if(appName==null  || mavenDependency==null){
                    this.$message({type: 'warning', message: '数据不能为空!'});
                    return
                }
                this.interval();
                post('api/zk/createApp',{
                    zkAddress:zkAddress,
                    dependency:mavenDependency,
                    zkId:zkId,
                    appName:appName}
                ).then((response)=> {
                    if(response.data.code==200){
                        this.$message({type: 'success', message: response.data.msg});
                    }else{
                        this.$message.error(response.data.msg);
                    }
                    this.progressNum=100;
                    this.visible=false;
                    clearInterval(window.interval);
                }).catch( (error)=> {
                    // this.$message.error('操作异常，'+error);
                });

            },

            openApp(zkAddress,index){
                this.$set(this.appvisible, index, true);
                post('api/zk/getAppNamesByZk',zkAddress).then((response)=> {
                    this.form.apps=response.data;
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                })
            },
            cancel(index){
                this.$set(this.appvisible, index, false);
            },
            getData(offset,limit,text){
                this.editable=[];
                this.changeFlag=false;
                let var1={}, pageObject={};
                var1['zkAlias']=text;
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                post('api/zk/queryAllZk',JSON.stringify(pageObject)).then((response)=> {
                            this.tableZkData=response.data.rows;
                            this.totalSize=response.data.total;
                            this.pageCount=Math.ceil(this.totalSize/this.pageSize);
                            this.change(null,0,false);
                    }).catch( (error)=> {
                        // this.$message.error('操作异常，'+error);
                })
            },
            query(){
                this.getData(1,this.pageSize,this.queryText);
            },
            clear(){
                this.queryText='';
                this.getData(1,this.pageSize,'','')
            },
            change(row,index,flag){
                if(this.changeFlag){
                    this.$alert('存在修改中的数据，请确认', '提示', {confirmButtonText: '确定'});
                    return
                }
                this.editable[index] = flag;
                this.$set(this.editable, index, flag);
                if(row!=null){
                    this.changeFlag=true;
                }
            },


            refresh(row){
                this.interval();
                let  pageObject={};
                pageObject["data"]=row;
                post('api/zk/refreshZkService',{
                    zkAddress:row.zkIp,
                    zkId:row.id,
                }).then((response)=> {
                    if(response.data.code=='200'){
                        this.$message({type: 'success', message: response.data.msg});
                    }else{
                        this.$message.error('操作异常，'+response.data.msg);
                    }
                    this.progressNum=100;
                    this.visible=false;
                    clearInterval(window.interval);

                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                });

            },

            interval(){
                this.progressNum=0;
                this.visible=true;
                let n=1;
                let that=this;
                window.interval= setInterval(function(){
                    console.log("进度条值："+that.progressNum);
                    that.progressNum=n;
                    if (that.progressNum > 98) {
                        clearInterval(window.interval);
                    }
                    n++;
                },1000);
            },


            save(row,index){
                if(!this.editable[index]) {
                    this.$alert('请勿重复保存', '提示', {confirmButtonText: '确定'});
                    return
                }

                if(!isNotEmpty(row.zkAlias) || !isNotEmpty(row.zkIp)){
                    this.$message({type: 'warning', message: '数据不能为空!'});
                    return
                }

                if(!row.zkIp.endsWith("2181")){
                    this.$message({type: 'warning', message: 'zk地址格式不正确!'});
                    return
                }
                this.interval();
                this.changeFlag=false;
                let  pageObject={};
                pageObject["data"]=row;
                post('api/zk/editZk',JSON.stringify(pageObject)).then((response)=> {
                    if(response.data.code=='200'){
                        this.getData(1,this.pageSize,this.queryText);
                        this.$message({type: 'success', message: response.data.msg});
                    }else{
                        this.$message.error('操作异常，'+response.data.msg);
                    }
                    this.progressNum=100;
                    this.visible=false;
                    clearInterval(window.interval);
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                });
            },
            remove(row){
                this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
                    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
                }).then(() => {
                    post('api/zk/deleteZk',row.id).then((response)=> {
                        if(response.data.code=='200'){
                            this.currentPage=1;
                            this.getData(1,this.pageSize,this.queryText);
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
                this.tableZkData.unshift(JSON.parse(JSON.stringify(this.tableZKInitData)));
                this.change(null,0,true)
            }
        },
        mounted() {
            this.changeFlag=false;
            this.getData(1,this.pageSize,'','');
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

    .tips{
        position: absolute;
        top: 50%;
        left: 40%;
    }

    .mavenDependency{
        color: #606266;
        line-height: 2;
    }

</style>