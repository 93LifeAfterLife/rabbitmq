package com.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class ChannelUtil {
    private static String host = "192.168.72.130";
    private static int port = 5672;    //默认端口
    private static String username = "admin";
    private static String password = "admin";

    private ChannelUtil(){};

    /**
     * 初始化信道
     * @return
     * @throws Exception
     */
    public static Channel initChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        System.out.println("init Channel ok!");
        return channel;
    }
}
