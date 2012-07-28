/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominongame.gameengine;

import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author jwalton
 */
public class GameStateContainer {

     private static Logger log = Logger.getLogger(GameStateContainer.class);

     private Collection<Player> players;
     private Collection<NPC> npcs;
     private Collection<WorldObject> worldObjects;
     private Collection<GameStateMessage> messages;

     public GameStateContainer(Collection<Player> players, Collection<NPC> npcs, Collection<WorldObject> worldObjects, Collection<GameStateMessage> messages){
          this.players = players;
          this.npcs = npcs;
          this.worldObjects = worldObjects;
          this.messages = messages;
     }

     public String toJsonStringEasy(){
          StringBuilder string = new StringBuilder();
          BasicDBObject masterObject = new BasicDBObject();
          ArrayList<BasicDBObject> playerObjects = new ArrayList<BasicDBObject>();
          for(Player player : this.players){
               BasicDBObject playerObject = new BasicDBObject();
               HashMap<String,String> playerMap = player.toHashMap();
               for(String key : playerMap.keySet()){
                    playerObject.put(key, playerMap.get(key));
               }
               playerObjects.add(playerObject);
          }

          masterObject.put("players", playerObjects);

          ArrayList<BasicDBObject> npcObjects = new ArrayList<BasicDBObject>();
          for(NPC npc : this.npcs){
               BasicDBObject npcObject = new BasicDBObject();
               HashMap<String,String> npcMap = npc.toHashMap();
               for(String key : npcMap.keySet()){
                    npcObject.put(key, npcMap.get(key));
               }
               npcObjects.add(npcObject);
          }

          masterObject.put("npcs", npcObjects);

          ArrayList<BasicDBObject> woObjects = new ArrayList<BasicDBObject>();
          for(WorldObject wo : this.worldObjects){
               BasicDBObject woObject = new BasicDBObject();
               HashMap<String,String> woMap = wo.toHashMap();
               for(String key : woMap.keySet()){
                    woObject.put(key, woMap.get(key));
               }
               woObjects.add(woObject);
          }

          masterObject.put("worldObjects", woObjects);

          ArrayList<BasicDBObject> messageObjects = new ArrayList<BasicDBObject>();
          for(GameStateMessage message : this.messages){
               BasicDBObject messageObject = new BasicDBObject();
               HashMap<String,String> messageMap = message.getMessageInfo();
               for(String key : messageMap.keySet()){
                    messageObject.put(key, messageMap.get(key));
               }
               messageObjects.add(messageObject);
          }

          masterObject.put("messages", messageObjects);

          //log.debug("Game state: "+masterObject.toString());
          return masterObject.toString();
     }

     public String toJsonString(){
         long start = System.currentTimeMillis();
         StringBuilder string = new StringBuilder();
         
         string.append("{players:["); //start of player array
         for(Player player : players){
              string.append("{");
              HashMap<String,String> playerMap = player.toHashMap();
              int count = 0;
              for(String key : playerMap.keySet()){
                   String value = playerMap.get(key);
                   
                   string.append(key);
                   string.append(":");
                   string.append(value);
                   count++;
                   if(count < (playerMap.size()-1)){
                        string.append(",");
                   }

                   
              }
              string.append("}"); //end of player
         }
         string.append("],"); //end of player array

         string.append("npcs:["); //start of npc array
         for(NPC npc : npcs){
              string.append("{");
              HashMap<String,String> npcMap = npc.toHashMap();
              int count = 0;
              for(String key : npcMap.keySet()){
                   String value = npcMap.get(key);
                   
                   string.append(key);
                   string.append(":");
                   string.append(value);
                   count++;
                   if(count < (npcMap.size()-1)){
                        string.append(",");
                   }

                   
              }
              string.append("}"); //end of npc
         }
         string.append("],"); //end of npc array

         string.append("worldObjects:["); //start of worldObject array
         for(WorldObject wo : worldObjects){
              string.append("{");
              HashMap<String,String> woMap = wo.toHashMap();
              int count = 0;
              for(String key : woMap.keySet()){
                   String value = woMap.get(key);

                   string.append(key);
                   string.append(":");
                   string.append(value);
                   count++;
                   if(count < (woMap.size()-1)){
                        string.append(",");
                   }


              }
              string.append("}"); //end of wo
         }
         string.append("],"); //end of world object array

         string.append("messages:["); //start of messages array
         for(GameStateMessage message : messages){
              string.append("{");
              HashMap<String,String> messageMap = message.getMessageInfo();
              int count = 0;
              for(String key : messageMap.keySet()){
                   String value = messageMap.get(key);

                   string.append(key);
                   string.append(":");
                   string.append(value);
                   count++;
                   if(count < (messageMap.size()-1)){
                        string.append(",");
                   }


              }
              string.append("}"); //end of message
         }
         string.append("]}"); //end of messages array

         long end = System.currentTimeMillis();
         long time = end - start;
         log.debug("Took "+time+" ms to create json string of Game State");
         return string.toString();
     }

     public Collection<GameStateMessage> getMessages() {
          return messages;
     }

     public void setMessages(Collection<GameStateMessage> messages) {
          this.messages = messages;
     }
     
     public Collection<NPC> getNpcs() {
          return npcs;
     }

     public void setNpcs(Collection<NPC> npcs) {
          this.npcs = npcs;
     }

     public Collection<Player> getPlayers() {
          return players;
     }

     public void setPlayers(Collection<Player> players) {
          this.players = players;
     }

     public Collection<WorldObject> getWorldObjects() {
          return worldObjects;
     }

     public void setWorldObjects(Collection<WorldObject> worldObjects) {
          this.worldObjects = worldObjects;
     }

     
}
