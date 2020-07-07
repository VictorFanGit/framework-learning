package main

import (
	"fmt"
	//"time"
	//"strconv"
	MQTT "github.com/eclipse/paho.mqtt.golang"
)

// 订阅回调
func subCallBackFunc(client MQTT.Client, msg MQTT.Message) {
	fmt.Printf("Subscribe: Topic is [%s]; msg is [%s]\n", msg.Topic(), string(msg.Payload()))
	// end := time.Now().UnixNano()
	// string(msg.Payload())
}

// 连接MQTT服务
func connMQTT(broker, user, passwd string) (bool, MQTT.Client) {
	opts := MQTT.NewClientOptions()
	opts.AddBroker(broker)
	opts.SetUsername(user)
	opts.SetPassword(passwd)

	mc := MQTT.NewClient(opts)
	if token := mc.Connect(); token.Wait() && token.Error() != nil {
		return false, mc
	}

	return true, mc
}

// 订阅消息
func subscribe() {
	// sub的用户名和密码
	b, mc := connMQTT("tcp://localhost:9051", "", "")
	if !b {
		fmt.Println("sub connMQTT failed")
		return
	}
	mc.Subscribe("demo1", 0x01, subCallBackFunc)
}

// 发布消息
func publish() {
	// pub的用户名和密码
	b, mc := connMQTT("tcp://localhost:9051", "", "")
	if !b {
		fmt.Println("pub connMQTT failed")
		return
	}
	var i uint64 = 0
	for {
		//t := time.Now().UnixNano()
		//msg := strconv.FormatInt(t, 10)
		//mc.Publish("demo1", 0x01, false, msg)
		mc.Publish("demo1", 0x01, false, "this is a test message")
		//fmt.Println(msg)
		//time.Sleep(time.Second)
		i++
		if i % 10000 == 0 {
			fmt.Println(i)
		}

	}
}

func main() {
	// subscribe()
	publish()
}
