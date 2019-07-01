package com.example.mqprovider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Test;

public class FanoutConsummer02 {
    @Test
    public void consummer01(){
        ConnectionFactory factory=new ConnectionFactory();
        factory.setVirtualHost("/zq");
        factory.setHost("192.168.129.128");
        factory.setPort(5672);
        factory.setUsername("zq");
        factory.setPassword("zq");
        try {
            Connection connection=factory.newConnection();
            Channel channel=connection.createChannel();
            //定义 发布订阅模式的交换机
            String exchangName="exchang01";
            //  fanout  发布/订阅  模式
            channel.exchangeDeclare(exchangName,"fanout");
            //定义绑定队列名称  进行绑定
            String queueName="queue01";
            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueBind(queueName,exchangName,"");
            //设置每次读取信息的条数
            channel.basicQos(1);
            // 创建queueingConsummer读取channel信息
            QueueingConsumer consumer=new QueueingConsumer(channel);
            boolean ack=false;
            channel.basicConsume(queueName,ack,consumer);
            while (true) {
                QueueingConsumer.Delivery delivery=consumer.nextDelivery();
                byte[] body=delivery.getBody();
                String msg=new String(body,"utf-8");
                System.out.println("consummer02 消费了："+msg);
                Long tag=delivery.getEnvelope().getDeliveryTag();
                channel.basicAck(tag,true);
                System.out.println("consummer02 消费了"+tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }












}
