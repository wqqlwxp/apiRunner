<template>
  <div id="app" >
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    name: 'App',
    mounted() {
      window.addEventListener('beforeunload', e => this.beforeunloadHandler(e));
      window.addEventListener('load', e => this.loadHandler(e))

    },
    methods: {
      beforeunloadHandler (e) {
        localStorage.setItem("user",JSON.stringify(this.$global.userPermission));
      },
      loadHandler (e) {
        this.$global.setUserPermission(JSON.parse(localStorage.getItem("user")));
        localStorage.removeItem("user")
      },
    },
    created() {
     if(localStorage.getItem('nick_name')==null){
       this.$router.replace('/login')
     }
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



  .el-aside {
    color: #333;
    line-height: 200px;
  }

  .el-main {
    color: #333;
    text-align: center;
    line-height: 160px;
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
