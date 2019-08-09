package com.rabbitmq.workqueues;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.utils.ChannelUtil;

import java.io.IOException;

public class WorkClient {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();

        //channel.queueDeclare("helloworld", false , false , false, null);

        channel.queueDeclare("task_queue", true, false, false, null);

        System.out.println("waiting to receive message...");

        //setQos(1) 服务质量保证方法, 这告诉rabbitmq一次只向消费者发送一条消息,
        // 在返回确认回执前, 不要向消费者发送新消息.
        // 而是把消息发给下一个空闲的消费者
        channel.basicQos(1);

        //void BasicQos(uint prefetchSize, ushort prefetchCount, bool global)
        //  prefetchSize：0
        //  prefetchCount：会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，
        //      即一旦有N个消息还没有ack，则该consumer将block掉，直到有消息ack
        //  global：true\false 是否将上面设置应用于channel，
        //      简单点说，就是上面限制是channel级别的还是consumer级别
        //  备注：据说prefetchSize 和global这两项，rabbitmq没有实现，暂且不研究
        //channel.basicQos(0, 3, false);

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                System.out.println("Receive: "+msg);

                //遍历字符串中的字符, 每个点使进程暂停一秒
                for (int i = 0; i < msg.length(); i++) {
                    if (msg.charAt(i)=='.') {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                System.out.println("ensure Tag: "+delivery.getEnvelope().getDeliveryTag());
                System.out.println("message handle OK!");

                //发送回执!!!
                //basicAck 方法的第二个参数 multiple 取值为 false 时，
                // 表示通知 RabbitMQ 当前消息被确认；
                // 如果为 true，则额外将比第一个参数指定的
                // delivery tag 小的消息一并确认。
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
            }
        };

        //autoAck 是否自动确认消息, true表示自动(这样会丢失消息, 应设置成false手动)
        channel.basicConsume("task_queue", false, deliverCallback, cancelCallback);
    }
}
