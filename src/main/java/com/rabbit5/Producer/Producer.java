package com.rabbit5.Producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/18 0018 上午 10:22
 * @DESC : Topic 匹配模式 类似于数据库中的 like关键字
 */
public class Producer {

    private static String exchange_name = "topicExchange";
    private static String exchange_type = "topic";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchange_name,exchange_type);

        // 待发送的消息
        String[] routingKeys = new String[]{"quick.orange.rabbit",
                "lazy.orange.elephant",
                "quick.orange.fox",
                "lazy.brown.fox",
                "quick.brown.fox",
                "quick.orange.male.rabbit",
                "lazy.orange.male.rabbit"};

        for (String routingKey : routingKeys){
            String message = "From "+routingKey+" routingKey' s message!";
            channel.basicPublish(exchange_name,routingKey,null,routingKey.getBytes("UTF-8"));
            System.out.println("TopicSend [x] Sent '" + routingKey + "':'" + message + "'");
        }

        channel.close();
        connection.close();
    }

}
