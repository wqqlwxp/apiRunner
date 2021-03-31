# apiRunner接口自动化测试平台

#### 介绍

apiRunner是一个简单易用接口自动化测试平台，基于前后端分离vue+springboot，测试人员无需任何代码基础，即可轻松完成复杂的接口自动化测试工作

#### 软件架构

前端技术栈：vue+router+echarts+elementui+axios<br>
后端技术栈：springboot+mybatis+mysql+jfreechart+httpclient


#### 功能有啥？

- **请求方式多样化**：支持http/https/dubbo三种方式，dubbo接口方法参数自动加载，响应可视化
- **高效用例管理**：支持多种数据请求方式和结果校验方式，并且避免重复录入支持用例维度数据整体导入
- **用户权限管理**：用户可以独立注册，可赋予权限，权限控制到按钮级别
- **多线程执行**：在测试计划和定时调度维度执行时，会触发多线程，单独在用例模块执行不会触发
- **定时任务调度**：支持根据项目维度配置定时任务，扫描项目维度下的测试计划数据
- **数据维度清晰**：平台数据以项目或者项目+模块维度维护和管理，数据维护低耦合
- **变量便捷引用**：用例维度下的多接口串行执行时，变量支持响应头和响应json数据传递引用，并且支持前置用例(类似登录操作)响应头和响应数据在用例维度全局引用
- **请求加解密**：支持项目+模块维度配置加解密，支持加解密方式包括AES/DES/MD5/BASE64等，支持响应加密json配置解析表达式，解密后再供用例校验，配置后对应项目+模块下的用例执行时，将自动对请求参数加密，响应数据解密
- **邮件报告**：在测试计划和定时调度维度执行时，会根据项目维度配置的邮件收件人和邮件标题，发送测试报告


#### 瞅一下吧

![输入图片说明](https://images.gitee.com/uploads/images/2021/0309/113558_ea755431_8773734.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0309/113712_03f1ab74_8773734.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0309/154936_448c9932_8773734.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0309/155013_83f78c40_8773734.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0309/154847_5d52c83b_8773734.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0329/155347_df10cfab_8773734.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0309/155305_a8707b5a_8773734.png "屏幕截图.png")

#### 平台部署
1、在将代码拉取到本地后，在IDE软件中配置npm和springboot运行环境<br>
2、配置数据库，根据apiRunner/server/src/main/resources/static/sql/api_runner.sql初始化数据表<br>
3、在application.yml中配置邮件发件人<br>
4、在application-local.yml和application-prod.yml中配置数据库连接、pic_path(邮件报告饼图临时地址)<br>
5、前端本地运行使用npm run serve，部署发布使用npm run prod进行打包，打包后生成dist目录，需要注意根据自己的服务器在env.prod中配置服务端地址



