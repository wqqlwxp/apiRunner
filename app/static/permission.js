import Vue from 'vue'

let userPermission=[];

export default {
    userPermission , //维护全局用户权限变量
    setUserPermission(data) {
        this.userPermission = data;
    }
}


const hasPermission=function(value){
    return (userPermission.indexOf(value)!=-1)?true:false
};


// 指令
Vue.directive('per', {
    bind: function(el, binding,vnode) {
        userPermission=vnode.context.$global.userPermission;
        if (!hasPermission(binding.value)) {
            el.style.display='none'
        }
    }
});


// 全局判断方法
Vue.prototype.$_has = hasPermission;



