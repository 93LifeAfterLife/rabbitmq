package com.rabbitmq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.utils.ChannelUtil;

import java.util.Scanner;

public class WorkServer {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();

        //channel.queueDeclare("helloworld", false, false, false, null);

        //对已存在的队列, rabbitmq不允许对其定义不同的参数
        channel.queueDeclare("task_queue", true, false, false, null);

        while (true) {
            System.out.println("Please enter a new message (1 dot means pause for a second): ");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".equals(msg)) {
                break;
            }
            //channel.basicPublish("", "helloworld", null, msg.getBytes());
            channel.basicPublish("", "task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
            System.out.println("message had delivered!");
        }
        channel.close();
    }
}
