package com.rabbitmq.publishsubscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.ChannelUtil;

import java.util.Scanner;

public class PSServer {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();

        //定义名字为logs, fanout(发散)类型的交换机
        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        while (true) {
            System.out.println("enter a message: ");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".equals(msg)) {
                break;
            }
            /**
             * arg1: 向指定的交换机发送消息
             * arg2: 不指定UI列, 有消费者向交换机绑定队列
             */
            channel.basicPublish("logs", "", null, msg.getBytes());
            System.out.println("published: "+msg);
        }
        channel.close();
    }
}
