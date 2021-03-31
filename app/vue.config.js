module.exports = {
    devServer: {
        disableHostCheck: false,
        host: "localhost",
        port: 8009,
        https: false,
        hotOnly: false,
        proxy: {     //axios跨域处理，配置变更时，需要手动重启服务
            '/api_dingding': {
                target:'https://oapi.dingtalk.com/',
                changeOrigin:true, //允许跨域
                pathRewrite:{
                    '^/api_dingding': ''
                }
            },
            '/api': {
                target:process.env.VUE_APP_BASE_API,
                changeOrigin:true, //允许跨域
                pathRewrite:{
                    '^/api': ''
                }
            }
        }

    }
};