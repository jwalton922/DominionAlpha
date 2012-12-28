/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominongame.gameengine;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.util.ArrayList;
import org.apache.log4j.Logger;


/**
 *
 * @author jwalton
 */
public class AMPQListener {

     private static Logger log = Logger.getLogger(AMPQListener.class);

     private String exchangeName;
     private String routingKey;
     private ClientListenerThread clt;
     private ClientMessageProcessor messageProcessor;

     public AMPQListener(String exchangeName, String routingKey, ClientMessageProcessor messageProcessor){
          this.exchangeName = exchangeName;
          this.routingKey = routingKey;
          this.clt = new ClientListenerThread();
          this.messageProcessor = messageProcessor;
     }

     public void startListening() {
          log.info("Starting AMPQ client listener");
          clt = new ClientListenerThread();
          Thread t = new Thread(clt);
          t.start();
     }


     private class ClientListenerThread implements Runnable {

          private boolean continueRunning = true;
          private QueueingConsumer consumer;

          public ClientListenerThread() {
          }

          public void stopRunning() {
               this.continueRunning = false;
          }

          public void run() {
               try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.exchangeDelete(exchangeName);
                    channel.exchangeDeclare(exchangeName, "direct", false);
//        channel.
                    String queueName = channel.queueDeclare("dominion-java-server-data-request-queue", false, false, false, null).getQueue();
                    System.out.println("QUEUE NAME = " + queueName);
                    channel.queueBind(queueName, exchangeName, routingKey);

                    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                    consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, true, consumer);

                    while (true) {
                         QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                         String message = new String(delivery.getBody());

                         System.out.println(" [x] Received '" + message + "'");
                         messageProcessor.processMessage(message);
                    }
               } catch (Exception e) {
                    e.printStackTrace();
               }

          }
     }
}
