/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import com.dominiongame.dominionserver.database.DominionDBIF;
import com.dominiongame.dominionserver.database.DominionDBMongoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author jwalton
 */
public class DataRequestProcessor implements ClientMessageProcessor {

     private static Logger log = Logger.getLogger(DataRequestProcessor.class);
     private Channel outGoingChannel;
     private String ROUTING_KEY = "data-key";
     private String DATA_REQUEST_TOPIC = "DOMINION_DATA_REQUEST_TOPIC";

     private GameEngineIF engine;

     public DataRequestProcessor(GameEngineIF engine){
          this.engine = engine;
          init();
     }

     private void init() {
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

     public void processMessage(String request) {
          try {
               DBObject requestObject = (DBObject) JSON.parse(request);
               String type = (String) requestObject.get("type");
               if (type.equalsIgnoreCase("ClientPlayerDataRequest")) {
                    processClientPlayerDataRequest(requestObject);
               }
          } catch (Exception e) {
               log.error("Error processing data request", e);
          }
     }

     public void processClientPlayerDataRequest(DBObject request) {
          if(log.isDebugEnabled()){
               log.debug("ClientDataRequest object: "+request.toString());
          }

          DBObject response = new BasicDBObject();
          response.put("socketKey", request.get("socketKey").toString());
          response.put("type", "ClientPlayerDataResponse");
          Player p = engine.getPlayer(request.get("username").toString());
          response.putAll(p.toHashMap());

          publishMessage(response);

     }

     public void publishMessage(DBObject message) {
          try {
               log.debug("Publishing message: "+message.toString());
               String messageString = message.toString();

               outGoingChannel.basicPublish(GameEngineController.EXCHANGE_NAME, DATA_REQUEST_TOPIC, null, messageString.getBytes());
          } catch (Exception e) {
               log.error("Error publishing message.", e);
          }
     }

     public static void main(String[] args){
          DominionDBIF database = new DominionDBMongoImpl();
          ArrayList<NPC> npcs = database.queryNPCs();
          ArrayList<Player> players = database.queryPlayers();
          ArrayList<WorldObject> objects = database.queryWorldObjects();

          HashMap<Integer, NPC> npcMap = new HashMap<Integer, NPC>();
          HashMap<Integer, Player> playerMap = new HashMap<Integer, Player>();
          HashMap<Integer, WorldObject> objectMap = new HashMap<Integer, WorldObject>();

          for (NPC npc : npcs) {
               npcMap.put(npc.getId(), npc);
          }

          for (Player player : players) {
               playerMap.put(player.getId(), player);
          }

          for (WorldObject object : objects) {
               objectMap.put(object.getId(), object);
          }

          GameEngine engine = new GameEngine(playerMap, objectMap, npcMap);
          DataRequestProcessor processor = new DataRequestProcessor(engine);
          AMPQListener listener = new AMPQListener("some-exchange4","data.topic", processor);
          listener.startListening();
     }
}
