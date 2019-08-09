package com.rabbitmq.topics;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.utils.ChannelUtil;

import java.io.IOException;
import java.util.Scanner;

public class TopicClient {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();
        channel.exchangeDeclare("topic_logs", "topic");
        String queueName = channel.queueDeclare().getQueue();

        System.out.println("enter bindingKeys, split with space: ");
        String[] s = new Scanner(System.in).nextLine().split("\\s");

        for (String bindingKey : s) {
            channel.queueBind(queueName, "topic_logs", bindingKey);
        }
        System.out.println("waiting to receive message...");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println("receive-[" + routingKey + "]: " + msg);
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
