### 简介：Springboot应用搭建所需常用功能-按需使用其中代码
### 功能：
- Application Context 启动完成，添加需要执行的初始化任务，相关类ServiceStarter
- 配置定时任务，相关类ScheduledTask
- 配置 Swagger2，相关类SwaggerConfig
- RestTemple 配置，引入httpclient, 可配置多项参数，相关类RestTemplateConfig
- 加入RestTemple post请求示例，get请求带上body字段的示例（该用法比较特殊,相关类RestTemplateInvoker
- 使用注解方式读取配置文件中的配置项，相关类ConfigReader
