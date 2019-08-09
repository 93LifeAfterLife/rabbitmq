package com.rabbitmq.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.ChannelUtil;

import java.util.Scanner;

public class TopicServer {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();
        channel.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);


        while (true) {
            System.out.println("enter a message: ");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".contentEquals(msg)){
                break;
            }
            System.out.println("enter a routingKey: ");
            String routingKey = new Scanner(System.in).nextLine();

            channel.basicPublish("topic_logs", routingKey, null, msg.getBytes());
            System.out.println("published-["+routingKey+"]: "+msg);
        }
        channel.close();
    }
}
