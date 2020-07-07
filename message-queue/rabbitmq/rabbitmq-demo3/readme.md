### 简介：RabbitMQ消息发布订阅方式应用示例
### 功能及测试：
- 修改pom文件中mainClass值，编译发送和接收的包，运行时需要拷贝lib目录
- 测试环境：阿里云 Ubuntu 1cpu 2G
- 一个发布者可对应多个订阅者,每个订阅者可都读取发布者的同一条消息
- 测试发现订阅者的数量会影响发布消息的速度，实测结果为（单cpu，2G内存，docker方式，单线程发）：

发布者(个) | 发布消息速度(个/秒) | 订阅者(个) | 订阅消费速度(个/秒)
-|-|-|-
1 | 13000-14000 | 1 | 13000-14000 
1 | 8000-8500 | 2 | 8000-8500 
1 | 5500-6000 | 3 | 5500-6000 
1 | 4300-4500 | 4 | 4300-4500 
1 | 2700-3300 | 5 | 2700-3300 