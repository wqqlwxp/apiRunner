
<template>
    <div class="container">

        <div class="login-con" id="login_container">
            <span style="margin-left:15%;font-weight: bold;">apiRunner接口自动化测试平台</span>
            <el-form ref="form" :model="form" label-width="80px" style="margin-top:35px">
                <el-form-item label="账号"  class="my-font">
                    <el-input v-model="form.userAccount" ></el-input>
                </el-form-item>
                <el-form-item label="密码" class="my-font">
                    <el-input v-model="form.userPassword" show-password ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="login">登录</el-button>
                    <el-popover placement="top" width="400" trigger="click">
                        <el-form :model="registeForm"  ref="registeForm" label-width="100px" >
                            <el-form-item label="账号" prop="pass">
                                <el-input  v-model="registeForm.userAccount" ></el-input>
                            </el-form-item>
                            <el-form-item label="密码" prop="checkPass">
                                <el-input  v-model="registeForm.userPassword"  show-password></el-input>
                            </el-form-item>
                            <el-form-item label="昵称" prop="age">
                                <el-input v-model="registeForm.nickName"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="registerUser" >提交</el-button>
                                <el-button @click="resetForm">重置</el-button>
                            </el-form-item>
                        </el-form>
                        <el-button slot="reference">注册</el-button>
                    </el-popover>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>




<script>

    import {isNotEmpty} from "../../utils/utils.js";
    import {post} from "../../utils/httpUtils.js";

    export default {
        name: "login",
        data(){
            return{
                form:{},
                registeForm:{}
            }
        },
        methods:{
            login(){
                if(!isNotEmpty(this.form.userAccount) || !isNotEmpty(this.form.userPassword)){
                    this.$message({type: 'warning', message: '账户或密码不能为空!'});
                    return;
                }
                post('api/auth/login',JSON.stringify(this.form)).then((response)=> {
                    if(response.data.code==200){
                        this.$router.replace('/');
                        localStorage.setItem("nick_name",response.data.object.nickName);
                        this.$global.setUserPermission(response.data.object.userPermission)
                    }else{
                        this.$message.error(response.data.msg);
                    }
                }).catch( (error)=> {
                    this.$message.error('登录异常，'+error);
                })
            },
            resetForm() {
                this.registeForm={}
            },
            registerUser(){
                post('api/auth/registerUser',JSON.stringify(this.registeForm)).then((response)=> {
                    if(response.data.code==200){
                        this.$message({type: 'success', message: '注册成功，请返回登录'});
                    }else{
                        this.$message.error(response.data.msg);
                    }
                }).catch( (error)=> {
                    this.$message.error('注册异常，'+error);
                })
            }
        }

    }
</script>

<style scoped >

    body{
        margin:0px;
    }

    .container{
        background-image: url("../../../static/login_bg.jpeg");
        width: 100%;
        height: 100%;
        background-size: cover;
        background-position: center;
        position: relative;
        opacity: 0.8;
        z-index: 100;
    }

    .login-con {
        position: absolute;
        left: 50%;
        top: 50%;
        z-index: 200;
        -webkit-transform: translateY(-60%);
        transform: translateY(-60%);
        width: 300px;
        opacity: 1;
        padding-top: 2%;
        border: 1px solid grey;
        padding-right: 2%;
        background: #F5FFFA;
        height: 250px;
    }

    .my-font{
        color: white;
    }



</style>