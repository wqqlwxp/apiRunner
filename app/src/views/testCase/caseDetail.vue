<template>
    <el-row >
        <el-row class="pading-20 my-border my-bg">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>首页</el-breadcrumb-item>
                <el-breadcrumb-item>用例管理</el-breadcrumb-item>
                <el-breadcrumb-item>普通用例明细</el-breadcrumb-item>
            </el-breadcrumb>
        </el-row>

        <el-row class="top30">
            <el-row  class="pading-20">
                <el-button size="small" type="primary" icon="el-icon-back" round class="left" @click="goCase">返回列表</el-button>
                <el-button v-per="'case_detail_save'" size="small" type="primary" icon="el-icon-check" round class="right" @click="saveCaseDetail">保存用例</el-button>
                <el-tooltip class="item" effect="dark" content="将按照哦用例维度环境执行" placement="top">
                    <el-button v-per="'case_detail_run'" size="small" icon="el-icon-loading" type="success" round class="right" @click="runCase">执行用例</el-button>
                </el-tooltip>

                <el-button size="small" type="primary" round @click="dialogFormVisible = true" icon="el-icon-bottom" class="left">导入数据</el-button>
                <el-dialog title="导入同项目和模块下已有用例数据" :visible.sync="dialogFormVisible">
                    <el-form :model="importForm">
                        <el-form-item label="用例id" :label-width="'100px'">
                            <el-input v-model="importForm.importCaseId" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="importCaseData">导 入</el-button>
                    </div>
                </el-dialog>

            </el-row>


            <el-row>
                <el-tag effect="plain" class="left">前置用例</el-tag>
            </el-row>
            <el-row class="my-border my-bg  pading-20" >
                <div>
                    <el-select   filterable clearable value-key="id" v-model="preData" slot="prepend"
                                 placeholder="如执行用例前需要类似登录处理时，请选择需要的前置接口用例，选择后将前置执行。右侧可清除选择"
                                 no-data-text='无可用前置用例数据' style="width:100%">
                        <el-option  v-for="item in caseData"  :key="item.id" :label="item.caseDesc" :value="item">
                            <span >所属项目：{{item.projectName}}</span>
                            <span style="margin-left:30px;" >所属模块：{{ item.moduleName }}</span>
                            <span style="margin-left:30px;color: #8492a6; font-size: 13px">用例描述：{{ item.caseDesc }}</span>
                        </el-option>
                    </el-select>
                </div>
            </el-row>


            <el-row  class="top30">
                <el-tag effect="plain" class="left">正常用例接口</el-tag>
                <el-popover placement="top-start" title="变量替换示例" width="1000" trigger="hover" style="float: left;height:32px">
                    <span>
                        <b>普通用例接口使用上一个接口响应数据：</b>表头参数替换: @接口响应头参数；表单参数替换：jsonPath表达式<br>
                        <b>普通用例接口使用前置用例响应数据：</b>表头参数替换：1、#前置用例接口响应头参数 2、jsonPath表达式； 表单参数替换：使用jsonPath表达式<br>
                        <b>结果校验：</b>校验的json节点可以使用响应的json节点名，也可以使用jsonPath表达式，目前只支持检验响应参数为json格式的数据
                    </span>
                    <el-button type="text" slot="reference"  icon="el-icon-info">友情提示</el-button>
                </el-popover>
                <el-button size="small"  class="right" type="primary" round style="margin-right: 20px" @click="addInterfaceData">添加用例接口</el-button>
            </el-row>



            <template v-for="(obj,index) in allData">
                <el-row class="my-border my-bg  pading-20" :key="index"  style="border-top: 1px dashed #52627C">
                    <div>
                        <span>被测接口：</span>
                        <el-select   filterable clearable value-key="id" v-model="obj.reqInterface" slot="prepend" placeholder="选择常规用例接口。右侧可清除选择" no-data-text='此用例所属项目和模块下无可用接口数据' style="width:85%">
                            <el-option  v-for="item in interfaceData"  :key="item.id" :label="item.interfaceDesc" :value="item">
                                <span >所属项目：{{item.projectName}}</span>
                                <span style="margin-left:30px;" >所属模块：{{ item.moduleName }}</span>
                                <span style="margin-left:30px;color: #8492a6; font-size: 13px">接口地址：{{ item.interfaceUrl }}</span>
                                <span style="margin-left:30px" >接口描述：{{ item.interfaceDesc }}</span>
                            </el-option>
                        </el-select>
                        <i v-per="'zk_del'"   class="el-icon-delete i_pry"    @click="removeCaseInterface(index,allData)" ></i>

                        <el-tabs type="card" v-model="obj.tabIndex" @tab-click="handleClick">
                            <el-tab-pane label="请求头" name="header">
                                <el-table :data="obj.reqHeaders"  >
                                    <el-table-column v-for="(item,key) in tableHeaderTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width" >
                                        <template  slot-scope="{row}">
                                            <template >
                                                <el-input v-model="row[item.prop]" ></el-input>
                                            </template>
                                        </template>
                                    </el-table-column>
                                    <el-table-column   align="center"  width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button icon="el-icon-delete" circle  @click="removeHeader($index,obj.reqHeaders)"></el-button>
                                        </template>
                                    </el-table-column>
                                    <el-table-column   align="center" width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button v-if="$index==obj.reqHeaders.length-1" icon="el-icon-plus" circle @click="addHeader($index,obj.reqHeaders)"></el-button>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </el-tab-pane>

                            <el-tab-pane label="请求参数" name="param">
                                <div class="left">
                                    <el-radio-group v-model="obj.reqBodys.type" >
                                        <el-radio label="none">none</el-radio>
                                        <el-radio  label="formData">表单(form-data)</el-radio>
                                        <el-radio  label="json">json</el-radio>
                                    </el-radio-group>
                                </div>
                                <div v-show="obj.reqBodys.type=='json'" >
                                    <textarea    placeholder="请输入json格式内容" v-model="obj.reqBodys.jsonData"></textarea>
                                </div>
                                <el-table v-show="obj.reqBodys.type=='formData'" :data="obj.reqBodys.data" >
                                    <el-table-column v-for="(item,key) in tableParamTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width" >
                                        <template slot-scope="{row}">
                                            <el-input v-model="row[item.prop]" ></el-input>
                                        </template>
                                    </el-table-column>

                                    <el-table-column   align="center"  width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button icon="el-icon-delete" circle  @click="removeParam($index,obj.reqBodys.data)"></el-button>
                                        </template>
                                    </el-table-column>
                                    <el-table-column   align="center" width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button v-if="$index==obj.reqBodys.data.length-1" icon="el-icon-plus" circle @click="addParam($index,obj.reqBodys.data)"></el-button>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </el-tab-pane>

                            <el-tab-pane label="结果校验" name="verif">
                                <div class="left">
                                    <el-radio-group v-model="obj.reqVerifs.type" >
                                        <el-radio label="none">不校验</el-radio>
                                        <el-radio  label="json">json校验</el-radio>
                                        <el-radio  label="all">完全校验</el-radio>
                                    </el-radio-group>
                                </div>
                                <div v-show="obj.reqVerifs.type=='all'" >
                                    <textarea   placeholder="请输入完整的接口预期响应内容" v-model="obj.reqVerifs.data"></textarea>
                                </div>
                                <el-table v-show="obj.reqVerifs.type=='json'" :data="obj.reqVerifs.jsonData"  >
                                    <el-table-column v-for="(item,key) in tableVerifTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width" >
                                        <template  slot-scope="{row}" >
                                            <template  v-if="item.prop!='verifType'">
                                                <el-input v-model="row[item.prop]" ></el-input>
                                            </template>

                                            <template    v-if="item.prop=='verifType'">
                                                <el-select  v-model="row[item.prop]"  placeholder="请选择" >
                                                    <el-option label="大于" value=">"> </el-option>
                                                    <el-option label="小于" value="<"> </el-option>
                                                    <el-option label="等于" value="="> </el-option>
                                                    <el-option label="包含" value="()"> </el-option>
                                                </el-select>
                                            </template>
                                        </template>

                                    </el-table-column>


                                    <el-table-column   align="center"  width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button icon="el-icon-delete" circle  @click="removeVerif($index,obj.reqVerifs.jsonData)"></el-button>
                                        </template>
                                    </el-table-column>
                                    <el-table-column   align="center" width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button v-if="$index==obj.reqVerifs.jsonData.length-1" icon="el-icon-plus" circle @click="addVerif($index,obj.reqVerifs.jsonData)"></el-button>
                                        </template>
                                    </el-table-column>
                                </el-table>

                            </el-tab-pane>


                            <el-tab-pane label="数据库校验" name="databaseVerif">
                                <div class="left">
                                    <el-radio-group v-model="obj.reqDataVerifs.type" >
                                        <el-radio label="none">不校验</el-radio>
                                        <el-radio  label="sql">sql校验</el-radio>
                                    </el-radio-group>
                                </div>
                                <el-table v-show="obj.reqDataVerifs.type=='sql'" :data="obj.reqDataVerifs.sqlData"  >
                                    <el-table-column v-for="(item,key) in tableDataVerifTitle" :key="key" :prop="item.prop" align="center" :label="item.label" :width="item.width" >
                                        <template  slot-scope="{row}" >
                                            <template  v-if="item.prop!='verifType'">
                                                <el-input v-model="row[item.prop]" ></el-input>
                                            </template>

                                            <template    v-if="item.prop=='verifType'">
                                                <el-select  v-model="row[item.prop]"  placeholder="请选择" >
                                                    <el-option label="大于" value=">"> </el-option>
                                                    <el-option label="小于" value="<"> </el-option>
                                                    <el-option label="等于" value="="> </el-option>
                                                    <el-option label="包含" value="()"> </el-option>
                                                </el-select>
                                            </template>
                                        </template>

                                    </el-table-column>

                                    <el-table-column   align="center"  width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button icon="el-icon-delete" circle  @click="removeVerif($index,obj.reqDataVerifs.sqlData)"></el-button>
                                        </template>
                                    </el-table-column>
                                    <el-table-column   align="center" width="80">
                                        <template  slot-scope="{row,$index}">
                                            <el-button v-if="$index==obj.reqDataVerifs.sqlData.length-1" icon="el-icon-plus" circle @click="addVerif($index,obj.reqDataVerifs.sqlData)"></el-button>
                                        </template>
                                    </el-table-column>
                                </el-table>

                            </el-tab-pane>


                        </el-tabs>
                    </div>

                </el-row>

            </template>






        </el-row>





    </el-row>
    
</template>

<script>
    import templateData from "../../../static/templateData";
    import {post} from "../../utils/httpUtils.js";
    import {queryAuthName} from "../../utils/utils.js";

    export default {
        name: "caseDetail",
        data() {
            return {
                activeName: 'second',
                tableHeaderTitle: templateData.tableHeaderTitle,
                tableHeaderInitData: templateData.tableHeaderInitData,
                tableParamTitle: templateData.tableParamTitle,
                tableParamInitData: templateData.tableParamInitData,
                tableVerifTitle: templateData.tableVerifTitle,
                tableVerifInitData: templateData.tableVerifInitData,
                tableDataVerifTitle: templateData.tableDataVerifTitle,
                tableDataVerifInitData: templateData.tableDataVerifInitData,
                allData:[],
                preData:[],
                caseData: [],
                interfaceData: [],
                dialogFormVisible:false,
                importCaseId:'',
                importForm: {
                    importCaseId: '',
                },

            }
        },
        methods: {
            handleClick(tab, event) {

            },
            filterCaseById(param) {
                let resp='';
                this.caseData.filter(function (obj) {
                    if (obj.id == param) {
                        resp= obj
                    }
                });
                return resp
            },
            init(type,data) {
                data = [];
                if (type == 'header') {
                    data.push(templateData.tableHeaderInitData)
                } else if (type == 'param') {
                    data.push(templateData.tableHeaderInitData)
                } else {
                    data.push(templateData.tableVerifInitData)
                }
            },
            removeHeader(index,headers) {
                if (headers.length <= 1) {
                    this.init('header',headers)
                } else {
                    headers.splice(index, 1)
                }
            },
            addHeader(index,headers) {
                headers.push(JSON.parse(JSON.stringify(templateData.tableHeaderInitData)))
            },
            removeParam(index,params) {
                if (params.length <= 1) {
                    this.init('param',params)
                } else {
                    params.splice(index, 1)
                }
            },
            addParam(index,params) {
                params.push(JSON.parse(JSON.stringify(templateData.tableHeaderInitData)))
            },
            removeVerif(index,verifDatas) {
                if (verifDatas.length <= 1) {
                    this.init('verif',verifDatas)
                } else {
                    verifDatas.splice(index, 1)
                }
            },
            addVerif(index,verifDatas) {
                verifDatas.push(JSON.parse(JSON.stringify(templateData.tableVerifInitData)))
            },
            addInterfaceData(){
                this.allData.push(JSON.parse(JSON.stringify(templateData.tableCaseInterfaceAddData)))
            },
            goCase(){
                this.$router.push({path:'/goCase'})
            },
            removeCaseInterface(index,data){
                data.splice(index, 1)
            },


            importCaseData(){
                if(this.importForm.importCaseId==''){
                    this.$message({type: 'warning', message: '导入用例id不能为空！'});
                    return;
                }
                var flag = Number(this.importForm.importCaseId);
                if (isNaN(flag)) {
                    this.$message({type: 'warning', message: '请输入数值类型的用例id！'});
                    return;
                }
                let pageObject={},var1={};
                var1["projectCode"]=this.$route.query.projectCode;
                var1["moduleCode"]=this.$route.query.moduleCode;
                var1["id"]=this.importForm.importCaseId;
                var1["serviceType"]="0";
                pageObject["data"]=var1;
                post('api/case/importCase',JSON.stringify(pageObject)).then((response)=> {
                    if(response.data.code=='200'){
                        this.dialogFormVisible=false;
                        this.allData=JSON.parse(response.data.object);
                        this.$message({type: 'success', message: response.data.msg});
                    }else{
                        this.$message.error(response.data.msg);
                    }
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                });

            },


            saveCaseDetail(){
                let that=this;
                let flag=false;
                this.allData.forEach(function (item) {
                    if(item.reqInterface.id=='') flag=true
                });

                if(flag){
                    this.$message({type: 'warning', message: '用例被测接口未选择！'});
                    return;
                }

                if(this.allData.length==0){
                    that.$message({type: 'warning', message: '用例被测接口未选择！'});
                    return;
                }

                if(this.allData.length>4096){
                    that.$message({type: 'warning', message: '用例数据过长！'});
                    return;
                }

                let  pageObject={},var1={};
                if(this.preData.id!='undefined'){
                    var1["preData"]=JSON.stringify(this.filterCaseById(this.preData.id));
                }
                if(this.allData.length>0){
                    var1["caseData"]=JSON.stringify(this.allData);
                }
                var1["projectCode"]=this.$route.query.projectCode;
                var1["moduleCode"]=this.$route.query.moduleCode;
                var1["id"]=this.$route.query.caseId;
                var1["operater"]=queryAuthName();
                pageObject["data"]=var1;
                post('api/case/editCase',JSON.stringify(pageObject)).then((response)=> {
                    if(response.data.code=='200'){
                        this.$message({type: 'success', message: response.data.msg});
                    }else{
                        this.$message.error('操作异常，'+response.data.msg);
                    }
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                });

            },
            runCase(){
                let form={};
                form['id']=this.$route.query.caseId;
                form['projectCode']=this.$route.query.projectCode;
                form['moduleCode']=this.$route.query.moduleCode;
                form['env']=this.$route.query.env;
                if(this.preData.id!=null){
                    form['reqPre']=this.preData;
                }
                form['reqCaseDatas']=this.allData;
                post('api/case/runCase',JSON.stringify(form)).then((response)=> {
                    this.$message({type: 'success', message: "用例已执行，请到左侧导航->报告查看 中查看结果"});
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                });
            },
            queryCase(offset,limit,project){
                let var1={}, pageObject={};
                var1['projectCode']=project;
                var1['type']='0';
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                let that=this;
                post('api/case/queryAllCase',JSON.stringify(pageObject)).then((response)=> {
                    this.caseData=response.data.rows;
                    if(this.caseData.length>0){
                        this.caseData.forEach(function (item,index) {
                            if(item.status=='1'){
                                that.caseData.splice(index,1)
                            }
                        })
                    }
                }).catch( (error)=> {
                    // this.$message.error('操作异常，'+error);
                })
            },
            queryInterface(offset,limit,project,module){
                let var1={}, pageObject={};
                var1['projectCode']=project;
                var1['moduleCode']=module;
                pageObject["offset"]=offset;
                pageObject["limit"]=limit;
                pageObject["data"]=var1;
                let that=this;
                post('api/interface/queryAllInterface',JSON.stringify(pageObject)).then((response)=> {
                    this.interfaceData=response.data.rows;
                    if(this.interfaceData.length>0){
                        this.interfaceData.forEach(function (item,index) {
                            if(item.status=='1'){
                                that.interfaceData.splice(index,1)
                            }
                        })
                    }
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                })
            },
            queryCaseDetail(){
                post('api/case/queryDetail',this.$route.query.caseId).then((response)=> {
                    if(response.data.preData!=''){
                        this.preData=JSON.parse(response.data.preData);
                    }
                    if(response.data.caseData!=''){
                        this.allData=JSON.parse(response.data.caseData);
                    }
                }).catch( (error)=> {
                    this.$message.error('操作异常，'+error);
                })
            }
        },

        mounted(){
            this.queryCase(1,50,this.$route.query.projectCode); //初始化查询全部前置用例列表
            this.queryInterface(1,50,this.$route.query.projectCode,this.$route.query.moduleCode); //初始化查询全部接口列表
            this.queryCaseDetail()  //查询用例明细数据
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
        padding: 10px;
    }

    .right{
        float: right;
    }
    .left{
       float: left;
    }

    .top30{
        margin-top:30px
    }

    textarea{
        width: 98%;
        height: 200px;
        font-size: 20px;
        font-family: monospace;
        color: #2a00ff;
    }

</style>