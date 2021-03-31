<template>
    <div >
        <el-col :span="4">
            <span >
                <b>apiRunner接口自动化测试平台</b>
            </span>
        </el-col>
        <el-col :span="20" class="userinfo">
            <el-dropdown trigger="hover"  style="color: white">
                <span class="el-icon-caret-bottom"></span>
                <span class="el-dropdown-link userinfo-inner">当前用户：{{userName}}</span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item >
                        <a @click="signOut()">注销登录</a>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-col>
    </div>
</template>

<script>
    export default {
        name: 'PageTop',
        data(){
            return{
                userName:localStorage.getItem("nick_name")
            }
        },
        methods:{
            signOut(){
                let that=this;
                that.$axios({
                    method:'post',
                    url:'/api/auth/signOut',
                    withCredentials:true,
                })
                    .then((response)=> {
                        if(response.data.code==200){
                            that.$cookies.remove("nick_name");
                            that.$router.replace('/login')
                        }
                    })
                    .catch( (error)=> {
                        console.log(error);
                        that.$message({showClose: true, message: '操作异常，请重试', type: 'error'})
                    })
            }
        }

    }
</script>

<style>
    .userinfo{
        text-align: right;
        padding-right: 30px;
    }
    .dropdown{
        color: white;
    }

</style>


