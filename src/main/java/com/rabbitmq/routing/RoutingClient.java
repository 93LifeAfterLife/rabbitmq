package com.rabbitmq.routing;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.utils.ChannelUtil;

import java.io.IOException;
import java.util.Scanner;

public class RoutingClient {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();
        channel.exchangeDeclare("direct_logs", "direct");
        String queueName = channel.queueDeclare().getQueue();

        //输入空格隔开的多种日志级别
        System.out.println("which level of logs would you want to receive: ");
        String[] s = new Scanner(System.in).nextLine().split("\\s");

        //绑定
        for (String level : s) {
            channel.queueBind(queueName, "direct_logs", level);
        }

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
