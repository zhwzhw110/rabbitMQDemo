package com.RpcRabbit.RPCServer;

import com.rabbitmq.client.*;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/18 0018 下午 2:40
 * @DESC :  RPC远程消息队列服务器
 */
public class RPCServer {

    private Connection connection;
    private Channel channel;
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private QueueingConsumer consumer;

    public RPCServer() throws  Exception{
        ConnectionFactory factory = new ConnectionFactory();
        connection = factory.newConnection();
        channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        //限制：每次最多给一个消费者发送1条消息
        channel.basicQos(1);

        //为rpc_queue队列创建消费者，用于处理请求
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
        System.out.println(" [x] Awaiting RPC requests");

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            //获取请求中的correlationId属性值，并将其设置到结果消息的correlationId属性中
            AMQP.BasicProperties props = delivery.getProperties();
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(props.getCorrelationId()).build();
            //获取回调队列名字
            String callQueueName = props.getReplyTo();
            String message = new String(delivery.getBody(),"UTF-8");

            System.out.println(" [.] fib(" + message + ")");
            //获取结果
            String response = "" + fib(Integer.parseInt(message));

            //先发送回调结果
            channel.basicPublish("", callQueueName, replyProps, response.getBytes());
            //后手动发送消息反馈
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    /**
     * 计算斐波列其数列的第n项
     * @param n
     * @return
     * @throws Exception
     */
    private static int fib(int n) throws Exception {
        if (n < 0){
            throw new Exception("参数错误，n必须大于等于0");
        }
        if (n == 0){
            return 0;
        }
        if (n == 1){
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) throws  Exception{
        RPCServer server = new RPCServer();
    }

}
