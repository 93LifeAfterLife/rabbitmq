package com.rabbitmq.publishsubscribe;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.utils.ChannelUtil;

import java.io.IOException;

public class PSClient {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();
        channel.exchangeDeclare("logs", "fanout");

        //不向queueDeclare()提供任何参数时，
        // 会创建一个具有生成名称的、非持久的、独占的、自动删除队列
        String queueName = channel.queueDeclare().getQueue();

        //将该队列绑定到logs交换机
        //channel.queueBind(QUEUR_NAME, EXCHANGE_NAME, ROUTING_KEY);
        //'routingKey' must be non-null.
        channel.queueBind(queueName, "logs", "");

        System.out.println("waiting to receive message...");
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                System.out.println("receive: "+msg);
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
