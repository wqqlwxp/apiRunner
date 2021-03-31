<template>
    <div id="app" >
        <el-container style="height: 100%">
            <el-header   style="padding: 0" >
                <PageTop></PageTop>
            </el-header>
            <el-container >
                <el-aside width="230px;height: 100%">
                    <LeftNavi></LeftNavi>
                </el-aside>
                <el-main style="padding: 0;height: 100%" >
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>

    import PageTop from "@/components/layout/PageTop";
    import LeftNavi from "@/components/layout/LeftNavi";
    export default {
        name: 'Index',
        components: {
            LeftNavi,
            PageTop,
        }
    }
</script>

<style>

    html,body,#app{
        height: 100%;
    }


    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;

    }
    body{
        margin-top: 0;
    }

    .el-header, .el-footer {
        color: #333;
        text-align: center;
        line-height: 60px;
        padding: 0;
        color: white;
        background: rgb(60, 141, 188);
    }

    .el-main{
        line-height: 40px;
    }


    body > .el-container {
        margin-bottom: 40px;
    }

    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
        line-height: 260px;
    }

    .el-container:nth-child(7) .el-aside {
        line-height: 320px;
    }



</style>
