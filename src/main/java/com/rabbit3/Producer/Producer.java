package com.rabbit3.Producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/17 0017 下午 4:05
 * @DESC : 发布订阅模式
 */
public class Producer {

    private static String exchange_name="publicSend";
    private static String exchange_type="fanout";

    public static void main(String[] args) throws  Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //设置交换机的名称模式
        channel.exchangeDeclare(exchange_name,exchange_type);
        //声明一个队列 因为是广发，所有不许需要声明特定队列
        //channel.queueDeclare(Queue_name,true,false,false,null);
        //发送消息到队列中
        /**
        *@author: zhanghHaiWen
        *@Desc:
        *@params:
         *  * @param exchange exchage的名称
         *  * @param routingKey 路由键，#匹配0个或多个单词，*匹配一个单词，在topic exchange做消息转发用 如果exchange_type 是广播，那么该属性无效
         *  * @param BasicProperties 基础配置信息
         *  * @param body the message body
         *
        *@Date: 2018/4/17 0017 下午 4:44
        */
        for(int i = 0;i<10;i++){
            //设置要发送的消息
            String message = "hello world"+i;
            channel.basicPublish(exchange_name,"",null,message.getBytes("UTF-8"));
            System.out.println("Producer [x] Sent '" + message + "'");
        }
        //关闭频道和连接
        channel.close();
        connection.close();
    }


}
