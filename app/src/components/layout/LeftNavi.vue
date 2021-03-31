<template>
    <el-menu  router    class="el-menu-vertical-demo" style="height: 100%;width: 230px"
              default-active="/main"
              background-color="#545c64"
              text-color="#fff"
              active-text-color="#ffd04b"
              unique-opened>
        <template v-for="(item,index) in navMenu" >
            <el-menu-item  :index="item.path"   :key="index"  v-if="!item.children" >
                <i :class="item.icon"></i>
                <span slot="title">{{item.title}}</span>
            </el-menu-item>


            <el-submenu  :key="index"  :index="item.path" v-if="item.children">
                <template slot="title">
                    <i :class="item.icon"></i>
                    <span> {{item.title}}</span>
                </template>

                <el-menu-item  :key="child.path" :index="child.path" v-for="child in item.children" >
                    <template slot="title">
                        <i :class="child.icon"></i>
                        <span>{{child.title}}</span>
                    </template>
                </el-menu-item>
            </el-submenu>
        </template>
    </el-menu>
</template>

<script>
    import templateData from "../../../static/templateData";
    export default {
        name: 'LeftNavi',
        data(){
            return{
                navMenu:templateData.navMenu
            }
        }
    }

</script>


<style>

</style>
