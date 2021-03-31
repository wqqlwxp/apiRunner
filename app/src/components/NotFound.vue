<template>
    <div class="error-box">
        <div class="error-body text-center">
            <h1>404</h1>
            <h4>很抱歉，但是那个页面看起来已经不存在了。</h4>
            <a href="/" class="btn btn-primary ">返回首页</a>
        </div>
    </div>
</template>

<script>
    export default {
        name: "NotFound"
    }
</script>

<style>
    body{
        background-color: #fff;
    }

    .error-body {
        padding-top: 5%;
        text-align: center;
    }
    .error-body h1 {
        font-size: 180px;
        font-weight: 700;
        text-shadow: 4px 4px 0 #f5f6fa, 6px 6px 0 #33cabb;
        line-height: 210px;
        color: #409eff;
    }
    .error-body h4 {
        margin: 30px 0px;
    }
</style>