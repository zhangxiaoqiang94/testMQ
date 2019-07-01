package com.example.mqprovider;

import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MqproviderApplicationTests {

  //  @Autowired
  //  AmqpTemplate amqpTemplate;
    /**
     *   rabbitMQ简单模式的   提供者 与消费者  测试
     */
    @Test
    //向队列添加 消息
    public void contextLoads() {
        /**
         * 在准备连接 服务前必须得在 (:15672)  给user授权
         * 否则服务端会连接不通，报 IO异常……
         */
        //创建连接工厂
        ConnectionFactory factory= new ConnectionFactory();
        factory.setVirtualHost("/zq");
        factory.setHost("192.168.129.128");
        factory.setPort(5672);
        factory.setUsername("zq");
        factory.setPassword("zq");
        try {
            //根据工厂创建连接
           Connection connection= factory.newConnection();
            //根据连接创建channel
            Channel channel=connection.createChannel();
            //3,定义队列
            String queueName="test01";
            //false:队列不保存到硬盘
            boolean durable=false;
            //true:别的程序不能访问这个队列
            //false:别的程序能访问这个队列
            boolean exclusive=false;
            //不要删除这个队列
            boolean autoDelete=false;
            HashMap<String, Object> arguments=null;
            channel.queueDeclare(queueName,
                    durable,
                    exclusive,
                    autoDelete,
                    arguments);
            //var1 交换机名称 "" 为默认交换机    var2 定义 对列名
            //var3 pro用于定义队列设置  不设置 默认    var4 存入队列中的 消息 转换为  .getbytes()   格式
            String exchangName="";

              for(int i=0;i<10;i++){
                  String message="简单模式   "+(i+1);
                  channel.basicPublish(exchangName,queueName,null,message.getBytes());

              }


                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    //获取队列消息  并处理   工作模式1
    public void consummer() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/zq");
        factory.setHost("192.168.129.128");
        factory.setPort(5672);
        factory.setUsername("zq");
        factory.setPassword("zq");
        try {
            Connection connection=factory.newConnection();
            Channel channel=connection.createChannel();
            //3,定义队列
            String queueName="test01";
            //false:队列不保存到硬盘
            boolean durable=false;
            //true:别的程序不能访问这个队列
            //false:别的程序能访问这个队列
            boolean exclusive=false;
            //不要删除这个队列
            boolean autoDelete=false;
            HashMap<String, Object> arguments=null;
            channel.queueDeclare(queueName,
                    durable,
                    exclusive,
                    autoDelete,
                    arguments);
            //获取队列 信息   （消费队列）
           QueueingConsumer consumer= new QueueingConsumer(channel);

            while (true) {
                //读取消息
                channel.basicConsume(queueName,true,consumer);

                QueueingConsumer.Delivery delivery= consumer.nextDelivery();
                byte[] body=  delivery.getBody();
                System.out.println(new String(body));
                connection.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Test
    //获取队列消息  并处理   工作模式02
    public void consummer2() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/zq");
        factory.setHost("192.168.129.128");
        factory.setPort(5672);
        factory.setUsername("zq");
        factory.setPassword("zq");
        try {
            Connection connection=factory.newConnection();
            Channel channel=connection.createChannel();
            //3,定义队列
            String queueName="test01";
            //false:队列不保存到硬盘
            boolean durable=false;
            //true:别的程序不能访问这个队列
            //false:别的程序能访问这个队列
            boolean exclusive=false;
            //不要删除这个队列
            boolean autoDelete=false;
            HashMap<String, Object> arguments=null;
            channel.queueDeclare(queueName,
                    durable,
                    exclusive,
                    autoDelete,
                    arguments);
            //获取队列 信息   （消费队列）
            QueueingConsumer consumer= new QueueingConsumer(channel);
                //读取消息
                channel.basicConsume(queueName,true,consumer);
            while (true) {
                QueueingConsumer.Delivery delivery= consumer.nextDelivery();
                byte[] body=  delivery.getBody();
                System.out.println(new String(body));
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
