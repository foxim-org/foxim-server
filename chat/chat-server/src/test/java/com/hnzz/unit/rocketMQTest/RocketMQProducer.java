package com.hnzz.unit.rocketMQTest;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class RocketMQProducer {

    public static void main(String[] args) throws MQClientException {
        // 创建一个生产者实例，并指定生产者组名称
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup");
        // 指定NameServer地址
        producer.setNamesrvAddr("47.109.86.70:9876");

        producer.setMaxMessageSize(4194304);

        // 启动生产者实例
        producer.start();

        try {
            // 创建消息对象，包含要发送的消息主题、标签和内容
            Message message = new Message("TopicTest", "TagA", "Hello World".getBytes());
            // 发送消息到RocketMQ服务器，并返回发送结果
            producer.send(message);
            System.out.printf("消息发送成功：%s %n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭生产者实例
            producer.shutdown();
        }
    }
}