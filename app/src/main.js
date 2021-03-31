import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import router from "./router/index"
import  axios from 'axios'
import 'element-ui/lib/theme-chalk/index.css';
import VueCookies from 'vue-cookies'
import JsonViewer from 'vue-json-viewer'
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/pie'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/title'
import 'echarts/lib/component/legend'
import 'echarts/lib/chart/line'
import  '../static/permission.js'
import  CronParser from  'cron-parser';

import _global from '../static/permission.js';
Vue.prototype.$global = _global;



Vue.prototype.$cronParser= CronParser;
Vue.prototype.$axios = axios;
Vue.prototype.$cookies=VueCookies;
Vue.config.productionTip = false;


Vue.use(JsonViewer);
Vue.use(ElementUI);
Vue.use(VueCookies);
Vue.component('v-chart', ECharts);




axios.interceptors.response.use(
    res => { // 全局接口响应拦截
      if(res.data.code ==401 ){ // 登录失效时重定向
        router.replace('/login');
        // ElementUI.Message({showClose: true, message: '登录失效，请重新登录', type: 'error'});
        // return Promise.reject(res.data.msg);
      }
      return  res
    }
    ,error => {
      if(error.response.status==401 ){ // 登录失效时重定向
        router.replace('/login');
        // ElementUI.Message({showClose: true, message: '登录失效，请重新登录', type: 'error'});
        // return Promise.reject(error.response.data.msg);
      }
    }
);



new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
