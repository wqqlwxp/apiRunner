<template>
    <el-row>
        <el-row>
            <span class="summary">数据汇总</span>
        </el-row>

        <el-row style="margin-top:15px">
            <el-col :span="4" class="info-box" style="border:1px solid   #00c0ef">
                    <span class="info-box-icon" style="background: #00c0ef !important"><i style="font-size:50px" class="el-icon-collection"></i></span>
                    <div class="info-box-content">
                        <span>被测项目数：</span><br>
                        <span style="font-size:20px;font-weight: bold;">{{projectCount}}</span>
                    </div>
            </el-col>
            <el-col :span="4" class="info-box" style="margin-left:30px;border:1px solid   #67C23A">
                <span class="info-box-icon" style="background: #67C23A !important"><i style="font-size:50px" class="el-icon-s-grid"></i></span>
                <div class="info-box-content">
                    <span>被测模块数：</span><br>
                    <span style="font-size:20px;font-weight: bold;">{{moduleCount}}</span>
                </div>
            </el-col>
            <el-col :span="4" class="info-box"  style="margin-left:30px;border:1px solid   #E6A23C">
                <span class="info-box-icon" style="background:#E6A23C !important"><i style="font-size:50px" class="el-icon-s-operation"></i></span>
                <div class="info-box-content">
                    <span>被测接口数：</span><br>
                    <span style="font-size:20px;font-weight: bold;">{{interfaceCount}}</span>
                </div>
            </el-col>
            <el-col :span="4" class="info-box"  style="margin-left:30px;border:1px solid   #F56C6C">
                <span class="info-box-icon" style="background: #F56C6C !important"><i style="font-size:50px" class="el-icon-notebook-2"></i></span>
                <div class="info-box-content">
                    <span>被测用例数：</span><br>
                    <span style="font-size:20px;font-weight: bold;">{{caseCount}}</span>
                </div>
            </el-col>
            <el-col :span="4" class="info-box"  style="margin-left:30px;border:1px solid   #409EFF">
                <span class="info-box-icon" style="background: #409EFF !important"><i style="font-size:50px" class="el-icon-s-unfold"></i></span>
                <div class="info-box-content">
                    <span>用例执行数：</span><br>
                    <span style="font-size:20px;font-weight: bold;">{{reportCount}}</span>
                </div>
            </el-col>

        </el-row>

        <el-row style="margin-top: 30px">
            <el-col :span="12" >
                <div style="height: 350px" >
                    <v-chart :options="options"  style="height: 350px"/>
                </div>
            </el-col>
            <el-col :span="12" >
                <div style="height: 450px" >
                    <v-chart :options="moduleOptions"  style="height: 350px"/>
                </div>
            </el-col>
        </el-row>



    </el-row>
</template>

<script>
    import {post} from "../../utils/httpUtils.js";


    export default {
        name: "reportSummary",
        data() {
            return {
                projectCount:0,
                moduleCount:0,
                interfaceCount:0,
                caseCount:0,
                reportCount:0,
                summary:[],
                moduleOptions: {
                    title: {
                        text: '模块维度成功率(15天)'
                    },
                    tooltip: {
                        trigger: 'axis',
                        formatter: function (params, ticket, callback) {
                            var showHtm=params[0].name+"<br>";
                            for(var i=0;i<params.length;i++){
                                showHtm+=params[i].seriesName+"："+params[i].data +"%<br>"
                            }
                            return showHtm;
                        }
                    },

                    legend: {
                        data: []
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: [],
                        axisLabel : {
                            formatter: function (value, index) {
                                var date = new Date(value);
                                var texts = [(date.getMonth() + 1), date.getDate()];
                                return texts.join('/');
                            }
                        }
                    },
                    yAxis: {
                        type: 'value',
                    },
                    series: []
                },
                options: {
                    title: {
                        text: '项目维度成功率(15天)'
                    },
                    tooltip: {
                        trigger: 'axis',
                        formatter: function (params, ticket, callback) {
                            var showHtm=params[0].name+"<br>";
                            for(var i=0;i<params.length;i++){
                                showHtm+=params[i].seriesName+"："+params[i].data +"%<br>"
                            }
                            return showHtm;
                        }
                    },

                    legend: {
                        data: []
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: [],
                        axisLabel : {
                            formatter: function (value, index) {
                                var date = new Date(value);
                                var texts = [(date.getMonth() + 1), date.getDate()];
                                return texts.join('/');
                            }
                        }
                    },
                    yAxis: {
                        type: 'value',
                    },
                    series: []
                }
            }
        },
        methods: {
            querySummary(){
                post('api/report/queryReportSummary',null).then((response)=> {
                    this.projectCount=response.data.projectCount;
                    this.moduleCount=response.data.moduleCount;
                    this.interfaceCount=response.data.interfaceCount;
                    this.caseCount=response.data.caseCount;
                    this.reportCount=response.data.reportCount;
                    this.options.xAxis.data=JSON.parse(response.data.dateList);
                    this.options.legend.data=response.data.projectList;
                    let arr=[],arr1=[];
                    let summary=eval(response.data.projectSummary);
                    for(var i=0; i<summary.length; i++){
                        let map={};
                        map['name']=summary[i].key;
                        map['data']=summary[i].value;
                        map['type']='line';
                        arr.push(map)
                    }
                    this.options.series=arr;
                    this.moduleOptions.legend.data=response.data.moduleList;
                    this.moduleOptions.xAxis.data=JSON.parse(response.data.dateList);
                    let moduleSummary=eval(response.data.moduleSummary);
                    for(var j=0; j<moduleSummary.length; j++){
                        let map={};
                        map['name']=moduleSummary[j].key;
                        map['data']=moduleSummary[j].value;
                        map['type']='line';
                        arr1.push(map)
                    }
                    this.moduleOptions.series=arr1;
                }).catch( (error)=> {
                    console.error(error)
                    // this.$message.error('操作异常，'+error);
                });
            }

        },mounted() {
            this.querySummary();
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

    .info-box {
        display: block;
        min-height: 90px;
        background: #fff;
        box-shadow: 0 1px 1px rgba(0,0,0,0.1);
        border-radius: 2px;
        margin-bottom: 15px;
    }

    .info-box-content{
        margin-left: 90px;
    }

    .info-box-icon {
        border-top-left-radius: 2px;
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
        border-bottom-left-radius: 2px;
        display: block;
        float: left;
        height: 90px;
        width:90px;
        text-align: center;
        font-size: 45px;
        line-height: 90px;
        background: rgba(0,0,0,0.2);
        color: #fff !important;
    }

    .summary{
        font-size: 18px;
        font-weight: bold;
        float: left;
    }
</style>