### 简介：Redis作为中间件实现任务分发功能，确保服务高可用（多实列，同一个应用可有多个生产者，消费者）
### 工作原理：
- 生产者将接入设备信息注册到redis中
- 消费者从redis读取当前接入设备列表，并再redis中查询该列表的设备数据是否被其他服务处理，若没有，标记处理该设备的数据，并定时刷新标记，若程序崩溃，则标记会过期失效，被其他服务重新标记处理
