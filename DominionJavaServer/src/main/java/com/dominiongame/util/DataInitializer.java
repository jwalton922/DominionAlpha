/*
 * This class will load some basic data to start around with.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominiongame.util;

import com.dominiongame.dominionserver.database.DominionDBMongoImpl;
import com.dominongame.gameengine.Action;
import com.dominongame.gameengine.CharacterState;
import com.dominongame.gameengine.Direction;
import com.dominongame.gameengine.NPC;
import com.dominongame.gameengine.Player;
import com.dominongame.gameengine.WorldObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jwalton
 */
public class DataInitializer {

     public static ArrayList<Player> createPlayers() {
          ArrayList<Player> players = new ArrayList<Player>();

          Player p1 = new Player(1, "laidback", "knight", CharacterState.NORMAL, 200, 200, Direction.s, null, 100, 100, 8, 40, 1);
          p1.setCurrentAction(p1.getNextAction());
          Player p2 = new Player(2, "dubs", "knight", CharacterState.NORMAL,300, 150, Direction.se, null, 100, 100, 8, 40, 1);
          p2.setCurrentAction(p2.getNextAction());
          players.add(p1);
          players.add(p2);

          return players;
     }

     public static ArrayList<WorldObject> createWorldObjects() {
          ArrayList<WorldObject> worldObjects = new ArrayList<WorldObject>();
          HashMap<String, Integer> treeResourceMap = new HashMap<String, Integer>();
          treeResourceMap.put("lumber", 1000);
          HashMap<String, Integer> emptyMap = new HashMap<String, Integer>();
          WorldObject w1 = new WorldObject(1, "tree_1", 40, 80, Direction.n, true, treeResourceMap);
          WorldObject w2 = new WorldObject(2, "tree_1", 300, 150, Direction.n, true, treeResourceMap);
          WorldObject w3 = new WorldObject(3, "castle", 400, 600, Direction.n, false, emptyMap);
          WorldObject w4 = new WorldObject(4, "foresters_lodge", 50, 800, Direction.s, false, emptyMap);

          worldObjects.add(w1);
          worldObjects.add(w2);
          worldObjects.add(w3);
          worldObjects.add(w4);

          return worldObjects;
     }

     public static ArrayList<NPC> createNPCs() {
          ArrayList<NPC> npcs = new ArrayList<NPC>();
          
          NPC npc1 = new NPC(1, "Lumberjack 1", "lumberjack", CharacterState.NORMAL,765, 100, Direction.sw, null, 75, 75, 8, 40, 1, "laidback", new HashMap<String, Integer>());
          npc1.setCurrentAction(npc1.getNextAction());
          NPC npc2 = new NPC(2, "Lumberjack 2", "lumberjack", CharacterState.NORMAL,700, 200, Direction.sw, null, 75, 75, 8, 40, 1, "laidback", new HashMap<String, Integer>());
          npc2.setCurrentAction(npc2.getNextAction());
          NPC npc3 = new NPC(3, "Spider", "spider", CharacterState.NORMAL,700, 700, Direction.sw, null, 75, 75, 8, 40, 1, "none", new HashMap<String, Integer>());
          npc3.setCurrentAction(npc3.getNextAction());
          NPC npc4 = new NPC(4, "Spider", "spider",CharacterState.NORMAL, 765, 300, Direction.sw, null, 75, 75, 8, 40, 1, "none", new HashMap<String, Integer>());
          npc4.setCurrentAction(npc4.getNextAction());

          npcs.add(npc1);
          npcs.add(npc2);
          npcs.add(npc3);
          npcs.add(npc4);

          return npcs;
     }


     public static void loadData() {
          ArrayList<NPC> npcs = createNPCs();
          ArrayList<Player> players = createPlayers();
          ArrayList<WorldObject> worldObjects = createWorldObjects();
          try {
               Mongo m = new Mongo("localhost", 27017);
               DB db = m.getDB("dominion");
               DBCollection npcDB = db.getCollection(DominionDBMongoImpl.NPC_DATABASE);
               DBCollection woDB = db.getCollection(DominionDBMongoImpl.WORLD_OBJECT_DATABASE);
               DBCollection playerDB = db.getCollection(DominionDBMongoImpl.PLAYER_DATABASE);

               for(Player player : players){
                    HashMap<String,String> playerMap = player.toHashMap();
                    BasicDBObject playerObject = new BasicDBObject();
                    for(String key : playerMap.keySet()){
                         String val = playerMap.get(key);
                         System.out.println("Inserting player info key = "+key+" val = "+val);
                         playerObject.append(key, val);
                         
                    }
                    playerDB.insert(playerObject);
               }

               for(NPC npc : npcs){
                    HashMap<String,String> npcMap = npc.toHashMap();
                    BasicDBObject npcObject = new BasicDBObject();
                    for(String key : npcMap.keySet()){
                         String val = npcMap.get(key);
                         System.out.println("Inserting npc info key = "+key+" val = "+val);
                         npcObject.append(key, val);
                         
                    }
                    npcDB.insert(npcObject);
               }

               for(WorldObject wo : worldObjects){
                    HashMap<String,String> woMap = wo.toHashMap();
                    BasicDBObject woObject = new BasicDBObject();
                    for(String key : woMap.keySet()){
                         String val = woMap.get(key);
                         System.out.println("Inserting wo info key = "+key+" val = "+val);
                         woObject.append(key, val);
                         
                    }
                    woDB.insert(woObject);
               }

          } catch (Exception e) {
               e.printStackTrace();
          }



     }

     public static void main(String[] args){
          loadData();
     }
}
