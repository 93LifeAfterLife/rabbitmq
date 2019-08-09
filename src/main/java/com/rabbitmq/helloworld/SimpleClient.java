package com.rabbitmq.helloworld;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.utils.ChannelUtil;
import sun.plugin2.message.Message;

import java.io.IOException;

public class SimpleClient {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();

        //声明队列, 如果该队列已经创建过, 则不再重复创建
        channel.queueDeclare("helloworld", false, false, false, null);
        System.out.println("waiting to receive message...");

        /**
         * DeliverCallback: 收到消息后用来处理消息的回调对象
         * CancelCallback: 消费者取消时的回调对象
         */
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                System.out.println("receive: "+msg +" [from channel: "+"helloworld"+"]");
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        //消费消息
        channel.basicConsume("helloworld", true, deliverCallback, cancelCallback);
    }
}
