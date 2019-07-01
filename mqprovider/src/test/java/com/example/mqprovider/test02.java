package com.example.mqprovider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class test02 {

        @Test
        public void  provider(){
            ConnectionFactory factory=  new ConnectionFactory();
            factory.setVirtualHost("/zq");
            factory.setHost("192.168.129.128");
            factory.setPort(5672);
            factory.setUsername("zq");
            factory.setPassword("zq");
            try {
                //根据工厂对象创建连接
               Connection connection= factory.newConnection();
               //根据连接创建通道
                Channel channel=connection.createChannel();
               //根据通道创建定义队列
                String queueName="queue01";

                // channel.queueDeclare(queueName,false,false,false,null);

                /**
                 * 与简单、工作模式 不同的是 需要定义交换机  、 定义模式
                 * 使用该模式 定义交换机  不需要定义队列
                 */
                String exchangName="exchang01";
                //  fanout  发布/订阅  模式
                channel.exchangeDeclare(exchangName,"fanout");
                //通过通道 发送消息
                String meg="消息以发送至队列中 fanout模式";
                //var1 交换机名   var2路由名(发布订阅模式 不写  等消费者来绑定交换机)
                channel.basicPublish(exchangName,"",null,meg.getBytes());
               System.out.println(meg);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }






}
