### 简介：资源注册组件
### 功能及说明：
- 需要连接redis，使用Jedis作为客户端
- 可作为基础jar包加入到应用中
- 可通过startDataMonitor开启数据监控，当redis宕机且数据丢失时，等Redis重启后可恢复数据，此功能针对redis服务不稳定的情况
- 
