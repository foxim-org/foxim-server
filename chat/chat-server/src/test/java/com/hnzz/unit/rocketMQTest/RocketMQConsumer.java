package com.hnzz.unit.rocketMQTest;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class RocketMQConsumer {

    public static void main(String[] args) throws Exception {
        // 创建一个消费者实例，并指定消费者组名称
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ProducerGroup");
        // 指定NameServer地址
        consumer.setNamesrvAddr("47.109.86.70:9876");
        // 从消息队列尾部开始消费消息
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // 订阅要消费的消息主题和标签
        consumer.subscribe("TopicTest",  "*");
        // 注册消息监听器，处理接收到的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                // 消息处理成功后返回消费状态
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}