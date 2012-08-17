/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.browsernodejava;

import com.dominongame.gameengine.Player;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.log4j.Logger;

/**
 *
 * @author jwalton
 */
public class BrowserNodeJavaExample {

     private static Logger log = Logger.getLogger(BrowserNodeJavaExample.class);
     private String exchangeName;
     private String incomingRoutingKey;
     private ClientListenerThread clt;

     public BrowserNodeJavaExample(String exchangeName, String incomingRoutingKey) {
          this.exchangeName = exchangeName;
          this.incomingRoutingKey = incomingRoutingKey;
          this.clt = new ClientListenerThread();
     }

     public void startListening() {
          log.info("Starting client listener");
          clt = new ClientListenerThread();
          Thread t = new Thread(clt);
          t.start();
     }

     private class ClientListenerThread implements Runnable {

          private boolean continueRunning = true;
          private QueueingConsumer consumer;
          private int countMessagesReceived = 0;
          private Channel outGoingChannel;

          public ClientListenerThread() {
               try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    Connection connection = factory.newConnection();
                    outGoingChannel = connection.createChannel();

                    outGoingChannel.exchangeDeclare("some-exchange4", "direct", false);
               } catch (Exception e) {
                    log.error("Error initializing GameEngineController", e);
               }
          }

          public void stopRunning() {
               this.continueRunning = false;
          }

          public void publishMessage(DBObject message) {
               try {

                    String messageString = message.toString();
                    log.debug("Publishing message: " + messageString);
                    outGoingChannel.basicPublish("DOMINION_CLIENT_EXCHANGE", "DOMINION_DATA_REQUEST_TOPIC", null, messageString.getBytes());
               } catch (Exception e) {
                    log.error("Error publishing message.", e);
               }
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
                    String queueName = channel.queueDeclare("dominion-queue", false, false, false, null).getQueue();
                    System.out.println("QUEUE NAME = " + queueName);
                    channel.queueBind(queueName, exchangeName, incomingRoutingKey);

                    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                    consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, true, consumer);

                    while (true) {
                         QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                         String requestMessage = new String(delivery.getBody());

                         System.out.println(" [x] Received '" + requestMessage + "'");
                         countMessagesReceived++;
                         DBObject requestObject = (DBObject) JSON.parse(requestMessage);
                         DBObject response = new BasicDBObject();
                         response.put("socketKey", requestObject.get("socketKey").toString());
                         response.put("type", "ClientDataResponse");
                         response.put("message", "The java process has received " + countMessagesReceived + " messages so far");

                         publishMessage(response);

                    }
               } catch (Exception e) {
                    e.printStackTrace();
               }

          }
     }

     public static void main(String[] args) {
          BrowserNodeJavaExample listener = new BrowserNodeJavaExample("some-exchange4", "data.topic");
          listener.startListening();
     }
}
