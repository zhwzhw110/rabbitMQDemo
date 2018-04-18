package com.RpcRabbit.RPCClient;

import com.rabbitmq.client.*;


/**
 * @author: zhangHaiWen
 * @date : 2018/4/18 0018 下午 2:03
 * @DESC : RPC远程消息队列客户端
 */
public class RPCClient {

    private  Connection connection;
    private Channel channel;
    private String callBackQueueName;
    private static final String RPC_QUEUE_NAME = "rpc_queue";
    private QueueingConsumer  consumer;

    public RPCClient() throws  Exception{
        ConnectionFactory factory = new ConnectionFactory();
        connection = factory.newConnection();
        channel = connection.createChannel();
        //创建一个回调队列
        callBackQueueName = channel.queueDeclare().getQueue();
        //创建一个消息接受者
        consumer = new QueueingConsumer(channel);
        //消费者与队列关联
        channel.basicConsume(callBackQueueName, true, consumer);
    }

    //设置一个回调函数
    public String call(String message) throws Exception{
        String response = null;
        String corrId = java.util.UUID.randomUUID().toString();
        //设置replyTo和correlationId属性值
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder().correlationId(corrId).replyTo(callBackQueueName).build();
        //发送消息到rpc_queue队列
        channel.basicPublish("", RPC_QUEUE_NAME, props, message.getBytes());
        while (true) { //监听回调
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(),"UTF-8");
                break;
            }
        }
        return response;
    }


    public static void main(String[] args) throws  Exception{
        RPCClient fibonacciRpc = new RPCClient();
        String result = fibonacciRpc.call("4");
        System.out.println( "fib(4) is " + result);
    }

}
