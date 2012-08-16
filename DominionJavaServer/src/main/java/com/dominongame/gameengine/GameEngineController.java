/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import com.dominiongame.dominionserver.database.DominionDBIF;
import com.dominiongame.dominionserver.database.DominionDBMongoImpl;
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
public class GameEngineController {

     private static Logger log = Logger.getLogger(GameEngineController.class);
     private static int STATUS_UPDATE_PERIOD = 17;
     private boolean continueRunning = true;
     private GameEngineIF gameEngine;
     private ClientListener clientListener;
     public static String EXCHANGE_NAME = "DOMINION_CLIENT_EXCHANGE";
     private String OUTGOING_TOPIC = "DOMINION_GAME_STATE_TOPIC"; //is this an exchange, queue, or topic?
     private Channel outGoingChannel;

     public GameEngineController() {
          init();
     }

     public void setGameEngine(GameEngine engine) {
          this.gameEngine = engine;
     }

     private void init() {

          clientListener = new ClientListener("some-exchange4", "action-topic");
          log.info("Starting clientListener");
          clientListener.startListening();
          try {
               ConnectionFactory factory = new ConnectionFactory();
               factory.setHost("localhost");
               Connection connection = factory.newConnection();
               outGoingChannel = connection.createChannel();

               outGoingChannel.exchangeDeclare(EXCHANGE_NAME, "direct", false);
          } catch (Exception e) {
               log.error("Error initializing GameEngineController", e);
          }
     }

     /**
      * parses and converts string messages to actions
      * @param message
      * @return
      */
     private ArrayList<Action> convertActionMessage(ArrayList<String> messages) {
          ArrayList<Action> actions = new ArrayList<Action>();
          for (int i = 0; i < messages.size(); i++) {
               try {
                    DBObject parsedMessage = (DBObject) JSON.parse(messages.get(i));
                    String entityType = (String) parsedMessage.get("entityType");
                    int entityId = Integer.parseInt((String) parsedMessage.get("entityId"));
                    String actionName = (String) parsedMessage.get("action");
                    int x = (Integer)parsedMessage.get("x");
                    int y = (Integer)parsedMessage.get("y");
                   // Direction dir = Direction.valueOf((String) parsedMessage.get("direction"));
                    Action a = new Action(entityType, entityId, actionName, x, y, null);
                    log.debug("New action received: " + a.toString());
                    actions.add(a);
               } catch(Exception e){
                    log.error("Error parsing message: "+e,e);
               }
          }
          return actions;
     }

     /**
      * Sends out game state over rabbitmq
      */
     private void publishGameState(GameStateContainer gameState) {
          try {
               //log.debug("publishing game state");
               String outgoingJson = gameState.toJsonStringEasy();
//               outgoingJson = "{players:[{id:1},{id:2}]}";
               byte[] outGoingBytes = outgoingJson.getBytes();
               //log.debug("publishing " + outGoingBytes.length + " bytes of data");
               //log.debug("outgoing json: "+outgoingJson);
               outGoingChannel.basicPublish(EXCHANGE_NAME, OUTGOING_TOPIC, null, outGoingBytes);
          } catch (Exception e) {
               log.error("Error publishing game state", e);
          }
     }

     public void run() {
          long start = System.currentTimeMillis();
          long end = System.currentTimeMillis();
          while (continueRunning) {

               start = System.currentTimeMillis();
               //log.debug("Time since last loop: " + (start - end));
               //get actions
               ArrayList<String> actionMessageStrings = clientListener.getMessages();
               long getActionTime = System.currentTimeMillis();
               //convert actions from strings to objects
               ArrayList<Action> actions = convertActionMessage(actionMessageStrings);
               long convertActionTime = System.currentTimeMillis();
               //update game state for next time step
               gameEngine.updateState(actions);
               //get game state
               GameStateContainer gameState = gameEngine.getGameState();
               //publish game state
               publishGameState(gameState);
               end = System.currentTimeMillis();
               long time = end - start;
               //log.debug("Took " + time + " ms for main game loop");
               if (time < STATUS_UPDATE_PERIOD) {
                    try {
                         Thread.currentThread().sleep(STATUS_UPDATE_PERIOD - time);
                    } catch (Exception e) {
                         log.error("Error sleeping");
                    }
               }

          }
     }

     public static void main(String[] args) {
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
          GameEngineController gec = new GameEngineController();
          gec.setGameEngine(engine);
          gec.run();
     }
}
