package main

import (
	"fmt"
	"strconv"
	"time"

	MQTT "github.com/eclipse/paho.mqtt.golang"
)

// 订阅回调
func subCallBackFunc(client MQTT.Client, msg MQTT.Message) {
	m := string(msg.Payload())
	end := time.Now().UnixNano()
	start, err := strconv.ParseInt(m, 10, 64)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(end - start)
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
	for {
		time.Sleep(time.Second)
	}
}

// 发布消息
func publish() {
	// pub的用户名和密码
	b, mc := connMQTT("tcp://localhost:9051", "", "")
	if !b {
		fmt.Println("pub connMQTT failed")
		return
	}

	for {
		t := time.Now().UnixNano()
		msg := strconv.FormatInt(t, 10)
		mc.Publish("demo1", 0x01, true, msg)
		fmt.Println(msg)
		time.Sleep(time.Second)
	}
}

func main() {
	subscribe()
	// publish()
}
