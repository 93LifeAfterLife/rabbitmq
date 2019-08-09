package com.rabbitmq.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.ChannelUtil;

import java.util.Random;
import java.util.Scanner;

public class RoutingServer {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();

        channel.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);

        //日志级别的数组
        String[] a ={"warning", "info", "error"};

        while (true) {
            System.out.println("enter a message: ");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".equals(msg)){
                break;
            }

            //随机产生日志级别
            String level = a[new Random().nextInt(a.length)];

            channel.basicPublish("direct_logs", level, null, msg.getBytes());
            System.out.println("published-["+level+"]: "+msg);
        }
        channel.close();
    }
}
