/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author jwalton
 */
public class ClientListener {

     private static Logger log = Logger.getLogger(ClientListener.class);
     private ClientListenerThread clt;
     private String EXCHANGE_NAME;
     private String ROUTING_KEY;
     private ArrayList<String> actionMessages = new ArrayList<String>();

     public ClientListener(String exchangeName, String routingKey) {
          EXCHANGE_NAME = exchangeName;
          ROUTING_KEY = routingKey;
     }

     public void startListening() {
          log.info("Starting client listener");
          clt = new ClientListenerThread();
          Thread t = new Thread(clt);
          t.start();
     }

     public ArrayList<String> getMessages() {
          ArrayList<String> messagesCopy = new ArrayList<String>(actionMessages);
          actionMessages.clear();
          //log.debug("Action Messages cleared, returning "+messagesCopy.size()+" messages");
          return messagesCopy;
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

                    channel.exchangeDeclare(EXCHANGE_NAME, "direct", false);
//        channel.
                    String queueName = channel.queueDeclare("dominion-queue", false, false, false, null).getQueue();
                    System.out.println("QUEUE NAME = " + queueName);
                    channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);

                    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, true, consumer);

                    while (true) {
                         QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                         String message = new String(delivery.getBody());

                         System.out.println(" [x] Received '" + message + "'");
                         actionMessages.add(message);
                    }
               } catch (Exception e) {
                    e.printStackTrace();
               }

//               try {
//                    ConnectionFactory factory = new ConnectionFactory();
//                    factory.setHost("localhost");
//                    Connection connection = factory.newConnection();
//                    Channel channel = connection.createChannel();
//
//                    channel.exchangeDeclare(EXCHANGE_NAME, "direct", false);
//
//                    String queueName = channel.queueDeclare("dominion--client-action-queue", false, false, false, null).getQueue();
//                    System.out.println("QUEUE NAME = " + queueName);
//                    channel.queueBind(queueName, EXCHANGE_NAME, "action-topic");
//
//                    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//                    consumer = new QueueingConsumer(channel);
//                    //channel.basicConsume(queueName, true, consumer);
//               } catch (Exception e) {
//                    log.error("Error setting up ClientListener", e);
//               }
//               log.info("ClientListenerThread running");
//               while (continueRunning) {
//                    try {
//                         QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//                         String message = new String(delivery.getBody());
//
//                         System.out.println(" [x] Received '" + message + "'");
//                         log.debug("received message: " + message);
//                         actionMessages.add(message);
//                    } catch (Exception e) {
//                         log.error("Error with receiving message: ", e);
//                    }
//               }
//               log.info("ClientLinstener thread stopped");
          }
     }
}
