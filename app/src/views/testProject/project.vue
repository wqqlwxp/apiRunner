<template>
    <el-row >
        <el-row class="pading-20 my-border my-bg">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>首页</el-breadcrumb-item>
                    <el-breadcrumb-item>项目管理</el-breadcrumb-item>
                    <el-breadcrumb-item>项目维护</el-breadcrumb-item>
                </el-breadcrumb>
        </el-row>

        <el-row >
            <div class="operate">
                <el-button  size="small" type="primary" @click="add" >新增</el-button>
                <el-input ref="queryText" v-model="queryText" size="small" placeholder="项目编码查询" style="width: 150px;margin-left: 20px;margin-right: 10px"></el-input>
                <el-button  size="small" type="primary" icon="el-icon-search"  @click="query">查询</el-button>
                <el-button  size="small" type="primary" icon="el-icon-delete"  @click="clear">清空</el-button>
            </div>

        </el-row>

        <el-row style="margin-top: 20px">
            <el-table :data="tableProjectData" style="width: 100%"  >
                <el-table-column  fixed="left" align="center" label="序号" width="100" >
                    <template  slot-scope="{$index}">
                        <span >{{(currentPage-1)*pageSize+$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column v-for="(item,key) in tableProjectTitle" :key="key" :prop="item.prop" align="center" :label="item.label" >
                    <template  slot-scope="{row,$index}">
                        <template v-if="item.prop!='status'" >
                            <el-input v-if="editable[$index] && item.prop!='createTime' && item.prop!='operater'"  v-model="row[item.prop]"></el-input>
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
                    </template>
                </el-table-column>
                <el-table-column  fixed="right" align="center" label="操作" width="200">
                    <template  slot-scope="{row,$index}">
                        <i v-per="'project_edit'"  class="el-icon-edit i_pry"     @click="change($index,true)" ></i>
                        <i v-per="'project_save'"  class="el-icon-check i_pry"     @click="save(row,$index)" ></i>
                        <i v-per="'project_del'"   class="el-icon-delete i_pry"     @click="remove(row)" ></i>
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
    import {queryAuthName} from "../../utils/utils.js";


    export default {
        name: "project",
        data(){
            return{
                tableProjectTitle:templateData.tableProjectTitle,
                tableProjectInitData:templateData.tableProjectInitData,
                tableProjectData:[],
                currentPage:1,
                pageSize: 5,
                totalSize: 100,
                pageCount:1,
                queryText:'',
                editable:[]
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
            getData(offset,limit,projectCode){
                this.editable=[];
                let var1={}, pageObject={};
                var1['projectCode']=projectCode;
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                post('api/project/queryAllProject',JSON.stringify(pageObject)).then((response)=> {
                            this.tableProjectData=response.data.rows;
                            this.totalSize=response.data.total;
                            this.pageCount=Math.ceil(this.totalSize/this.pageSize);
                            this.change(0,false);
                    }).catch( (error)=> {
                        // this.$message.error('操作异常，'+error);
                })
            },
            query(){
                this.getData(1,this.pageSize,this.queryText);
            },
            clear(){
                this.queryText='';
                this.getData(1,this.pageSize,this.queryText)
            },
            change(index,flag){
                this.editable[index] = flag;
                this.$set(this.editable, index, flag);

            },
            save(row,index){
                if(!this.editable[index]) {
                    this.$alert('修改后才能进行保存', '提示', {confirmButtonText: '确定'});
                    return
                }
                if(!isNotEmpty(row.projectName) || !isNotEmpty(row.projectCode)){
                    this.$message({type: 'warning', message: '数据不能为空!'});
                    return
                }

                let  pageObject={};
                row.operater=queryAuthName();
                pageObject["data"]=row;
                post('api/project/editProject',JSON.stringify(pageObject)).then((response)=> {
                    if(response.data.code=='200'){
                        this.getData(1,this.pageSize,this.queryText);
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
                this.$confirm('此操作将删除该数据，是否继续?', '提示', {
                    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
                }).then(() => {
                    post('api/project/deleteProject',JSON.stringify(row)).then((response)=> {
                        if(response.data.code=='200') {
                            this.currentPage=1;
                            this.getData(1,this.pageSize,'');
                            this.$message({type: 'success', message: '删除成功!'});
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
                this.tableProjectData.unshift(JSON.parse(JSON.stringify(this.tableProjectInitData)));
                this.change(0,true)
            }
        },
        mounted() {
            this.getData(1,this.pageSize,'')
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