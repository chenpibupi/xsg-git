## 新时光在线投稿平台

本项目基于RuoYi-Cloud进行搭建，构建了一个期刊投稿系统，包含投稿、审稿、上传稿件等多个功能。





## RuoYi平台简介

<p align="center">
	<img alt="logo" src="https://oscimg.oschina.net/oscnet/up-b99b286755aef70355a7084753f89cdb7c9.png">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">RuoYi v3.6.3</h1>
<h4 align="center">基于 Vue/Element UI 和 Spring Boot/Spring Cloud & Alibaba 前后端分离的分布式微服务架构</h4>
<p align="center">
	<a href="https://gitee.com/y_project/RuoYi-Cloud/stargazers"><img src="https://gitee.com/y_project/RuoYi-Cloud/badge/star.svg?theme=dark"></a>
	<a href="https://gitee.com/y_project/RuoYi-Cloud"><img src="https://img.shields.io/badge/RuoYi-v3.6.3-brightgreen.svg"></a>
	<a href="https://gitee.com/y_project/RuoYi-Cloud/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

若依是一套全部开源的快速开发平台，毫无保留给个人及企业免费使用。

* 采用前后端分离的模式，微服务版本前端(基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue))。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。

#### 友情链接 [若依/RuoYi-Cloud](https://gitee.com/zhangmrit/ruoyi-cloud) 版本。

## 系统模块

~~~
com.ruoyi     
├── ruoyi-ui              // 前端框架 [80]
├── ruoyi-gateway         // 网关模块 [8080]
├── ruoyi-auth            // 认证中心 [9200]
├── ruoyi-api             // 接口模块
│       └── ruoyi-api-system                          // 系统接口
├── ruoyi-common          // 通用模块
│       └── ruoyi-common-core                         // 核心模块
│       └── ruoyi-common-datascope                    // 权限范围
│       └── ruoyi-common-datasource                   // 多数据源
│       └── ruoyi-common-log                          // 日志记录
│       └── ruoyi-common-redis                        // 缓存服务
│       └── ruoyi-common-seata                        // 分布式事务
│       └── ruoyi-common-security                     // 安全模块
│       └── ruoyi-common-swagger                      // 系统接口
├── ruoyi-modules         // 业务模块
│       └── ruoyi-system                              // 系统模块 [9201]
│       └── ruoyi-gen                                 // 代码生成 [9202]
│       └── ruoyi-job                                 // 定时任务 [9203]  未使用
│       └── ruoyi-file                                // 文件服务 [9300] 
├── ruoyi-visual          // 图形化管理模块
│       └── ruoyi-visual-monitor                      // 监控中心 [9100]  未使用
├──pom.xml                // 公共依赖
~~~

## 架构图

<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 在线构建器：拖动表单元素生成相应的HTML代码。
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 演示图

![image-20240821201651825](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212016958.png)

​																										登录界面图

![image-20240821201637088](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212016206.png)

​																										注册界面图

![image-20240821201726213](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212017266.png)

​																								稿件列表信息展示图

![image-20240821201751740](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212017788.png)

​																								新增稿件界面展示图

![image-20240821201821966](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212018026.png)

​																							稿件内容编写页面展示图

![image-20240821202012868](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212020918.png)

​																									稿件投递展示图

![image-20240821202040154](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212020205.png)

​																								上传终稿文件展示图

![image-20240821202104956](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212021001.png)

​																								终稿文件列表展示图

![image-20240821202124810](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212021859.png)

​																									审批意见展示图

![image-20240821202142087](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212021132.png)

​																								稿件审批展示图

![image-20240821202227533](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212022585.png)

​																							审批意见管理展示图

![image-20240821202305919](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212023964.png)

​																							终稿文件管理展示图

![image-20240821202340192](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212023240.png)

​																							稿件分配专家展示图

![image-20240821202422079](https://cdn.jsdelivr.net/gh/chenpibupi/pic_cloud_img/picgo/202408212024126.png)

​																						稿件发布与管理展示图