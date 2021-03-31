<template>
    <el-row >
        <el-row class="pading-20 my-border my-bg">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>首页</el-breadcrumb-item>
                    <el-breadcrumb-item>权限管理</el-breadcrumb-item>
                    <el-breadcrumb-item>用户维护</el-breadcrumb-item>
                </el-breadcrumb>
        </el-row>

        <el-row >
            <div class="operate">
                <el-button  size="small" type="primary" @click="add" >新增</el-button>
                <el-input ref="queryProjectText" v-model="queryUserAccount" size="small" placeholder="账号查询" style="width: 150px;margin-left: 20px;margin-right: 10px"></el-input>
                <el-button  size="small" type="primary" icon="el-icon-search"  @click="query">查询</el-button>
                <el-button  size="small" type="primary" icon="el-icon-delete"  @click="clear">清空</el-button>
            </div>
        </el-row>

        <el-row style="margin-top: 20px">
            <el-table :data="tableUserData" style="width: 100%"  >
                <el-table-column  fixed="left" align="center" label="序号" width="100" >
                    <template  slot-scope="{$index}">
                        <span >{{(currentPage-1)*pageSize+$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column v-for="(item,key) in tableUsersTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width" >
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

                        <template v-if="item.prop=='userPassword'" >
                            <el-input v-if="editable[$index]" show-password   v-model="row[item.prop]"></el-input>
                            <span v-else>{{row[item.prop]}}</span>
                        </template>

                        <template  v-if="item.prop=='permission'">
                            <el-popover  placement="top" title="用户权限维护" width="100%" trigger="click">
                                <el-transfer v-model="row[item.prop]" :data="data"   :button-texts="['移除权限', '赋予权限']" :titles="['可赋权限', '已有权限']"></el-transfer>
                                <el-button icon="el-icon-user" type="text" slot="reference">权限</el-button>
                            </el-popover>
                        </template>
                    </template>
                </el-table-column>

                <el-table-column  fixed="right" align="center" label="操作" width="200" >
                    <template  slot-scope="{row,$index}">
                        <el-button v-per="'user_edit'"  size="small"  type="primary" icon="el-icon-edit" @click="change(row,$index,true)" circle></el-button>
                        <el-button v-per="'user_save'"  size="small" type="success" icon="el-icon-check" @click="save(row,$index)" circle></el-button>
                        <el-button v-per="'user_del'"  size="small" type="danger" icon="el-icon-delete" @click="remove(row)" circle></el-button>
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
    import {queryAuthName} from "../../utils/utils.js";
    import {post} from "../../utils/httpUtils.js";

    export default {
        name: "user",
        data() {
            return {
                tableUsersTitle: templateData.tableUsersTitle,
                tableUsersInitData: templateData.tableUsersInitData,
                tableUserData: [],
                currentPage: 1,
                pageSize: 5,
                totalSize: 100,
                pageCount: 1,
                queryUserAccount: '',
                editable: [],
                changeFlag: false,
                filterList: ['status', 'permission','userPassword'],
                data: templateData.allPermission
            }
        },
        methods:{
            handleSizeChange(size){
                this.currentPage = 1;//每次改变每页多少条都会重置当前页码为1
                this.pageSize = size;
                this.getData(this.currentPage,this.pageSize,'')
            },
            handleCurrentChange(size){
                this.currentPage = size;
                this.getData(this.currentPage,this.pageSize,'')
            },
            getData(offset,limit,account){
                this.editable=[];
                this.changeFlag=false;
                let var1={}, pageObject={};
                var1['userAccount']=account;
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                post('api/auth/queryAllUser',JSON.stringify(pageObject)).then((response)=> {
                    let obj=response.data.rows;
                    obj.forEach(function (item,index) {
                        if(item.permission!='' && item.permission!=null){
                            item.permission=JSON.parse(item.permission);
                        }else{
                            item.permission=[]
                        }
                    });
                    this.tableUserData=obj;
                    this.totalSize=response.data.total;
                    this.pageCount=Math.ceil(this.totalSize/this.pageSize);
                    this.change(null,0,false);
                }).catch( (error)=> {
                        // this.$message.error('操作异常，'+error);
                })
            },
            query(){
                this.getData(1,this.pageSize,this.queryUserAccount);
            },
            clear(){
                this.queryUserAccount='';
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
            save(row,index){
                if(!this.editable[index]) {
                    this.$alert('修改后才能进行保存', '提示', {confirmButtonText: '确定'});
                    return
                }
                if(!isNotEmpty(row.userAccount) || !isNotEmpty(row.userPassword) ){
                    this.$message({type: 'warning', message: '数据不能为空!'});
                    return
                }

                if(row.userPassword.length>15){
                    this.$message({type: 'warning', message: '密码过长!'});
                    return
                }

                this.changeFlag=false;
                let  pageObject={};
                row.operater=queryAuthName();
                row.permission=JSON.stringify(row.permission);
                pageObject["data"]=row;
                post('api/auth/editUser',JSON.stringify(pageObject)).then((response)=> {
                    if(response.data.code=='200'){
                        this.getData(1,this.pageSize,this.queryUserAccount);
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
                    post('api/auth/deleteUser',row.id).then((response)=> {
                        if(response.data.code=='200'){
                            this.currentPage=1;
                            this.getData(1,this.pageSize,this.queryUserAccount);
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
                this.tableUserData.unshift(JSON.parse(JSON.stringify(this.tableUsersInitData)));
                this.change(null,0,true)
            },
        },

        mounted() {
            this.changeFlag=false;
            this.getData(1,this.pageSize,'');
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