package com.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.ChannelUtil;

public class SimpleServer {
    public static void main(String[] args) throws Exception {
        Channel channel = ChannelUtil.initChannel();
        /**
         * 声明队列
         *  会在rabbitmq中创建一个队列
         *  如果已经创建过该队列, 则不能在使用其他参数来创建
         *
         * 参数含义
         *  -queue: 队列名称
         *  -durable: 队列持久化, true表示RabbitMQ重启后队列仍存在
         *  -exclusive: 排他, true表示限制队列使用, 仅当前连接可用
         *  -autoDelete: 当最后一个消费者断开后, 是否删除队列, true表示自动删除
         *  -arguments: 其他参数
         */
        String channelName = "helloworld";

        channel.queueDeclare(channelName, false, false , false, null);

        /**
         * 发布消息
         *  这里把消息向默认交换机发送
         *  默认交换机隐含与所有队列绑定, routingkey表示队列名称
         * 参数含义
         *  -exchange: 交换机名称, 空串表示默认交换机"(AMQP default)", 注意不能用null
         *  -routingKey: 对于默认交换机, 路由键就是目标队列名称
         *  -props: 其他参数, 例如头信息等
         *  -body: 消息内容byte[]数组
         *
         */
        channel.basicPublish("", channelName, null, "Hello RabbitMQ! I'm helloworld-channel".getBytes());

        System.out.println("message had delivered!");
        channel.close();
    }
}
