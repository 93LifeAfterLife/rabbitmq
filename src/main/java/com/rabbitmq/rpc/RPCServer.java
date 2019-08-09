package com.rabbitmq.rpc;

import com.rabbitmq.client.*;
import com.rabbitmq.utils.ChannelUtil;

import java.io.IOException;

public class RPCServer {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();
        channel.queueDeclare("rpc_queue", false, false, false, null);

        //清除队列中的内容
        channel.queuePurge("rpc_queue");

        channel.basicQos(1);

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                //使用 低效率的递归方法求斐波那契数 来模拟延迟
                String msg = new String(delivery.getBody());
                int n = Integer.parseInt(msg);
                //求第n个斐波那契数, 第45开始就很缓慢了
                int fbnq = fbnq(n);
                String response = String.valueOf(fbnq);

                //设置发回响应的id, 与请求id一致, 这样客户端可以把该响应与它的请求进行对应
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().correlationId(delivery.getProperties().getCorrelationId()).build();
                /**
                 * 发送响应消息
                 *  1.默认交换机
                 *  2.有客户端指定的, 用来传递响应消息的队列名
                 *  3.关联id
                 *  4.发回的响应消息
                 */
                channel.basicPublish("", delivery.getProperties().getReplyTo(), basicProperties, response.getBytes());

                //发送确认消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
            }
        };
        channel.basicConsume("rpc_queue", false, deliverCallback, cancelCallback);
    }

    private static int fbnq(int n) {
        if (n==1||n==2) return 1;
        return fbnq(n-1)+fbnq(n-2);
    }
}
