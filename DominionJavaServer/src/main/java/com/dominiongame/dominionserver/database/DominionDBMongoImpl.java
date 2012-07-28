/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominiongame.dominionserver.database;

import com.dominongame.gameengine.GameStateContainer;
import com.dominongame.gameengine.GameStateMessage;
import com.dominongame.gameengine.NPC;
import com.dominongame.gameengine.Player;
import com.dominongame.gameengine.WorldObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 *
 * @author jwalton
 */
public class DominionDBMongoImpl implements DominionDBIF {

     private static Logger log = Logger.getLogger(DominionDBMongoImpl.class);
     private Mongo mongo;
     private DB db;
     public static String NPC_DATABASE = "npcs";
     public static String WORLD_OBJECT_DATABASE = "world_objects";
     public static String PLAYER_DATABASE = "players";


     public DominionDBMongoImpl() {
          try {
               mongo = new Mongo("localhost", 27017);
               db = mongo.getDB("dominion");
          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     public GameStateContainer queryGameState(){

          ArrayList<NPC> npcs = queryNPCs();
          ArrayList<Player> players = queryPlayers();
          ArrayList<WorldObject> objects = queryWorldObjects();
          Collection<GameStateMessage> messages = new ArrayList<GameStateMessage>();
          GameStateContainer state = new GameStateContainer(players, npcs, objects, messages);

          return state;
     }

     @Override
     public ArrayList<NPC> queryNPCs(){
          ArrayList<NPC> npcs = new ArrayList<NPC>();
          DBCollection collection = db.getCollection(NPC_DATABASE);
          DBCursor cursor = collection.find();
          while(cursor.hasNext()){
               DBObject npcObject = cursor.next();
               NPC npc = new NPC(npcObject.toMap());
               log.debug("Retrieved NPC from mongo: "+npc.toString());
               npcs.add(npc);
               //NPC npc = new NPC(npcObject.get("id"), NPC_DATABASE, NPC_DATABASE, CharacterState.DEAD, x, y, Direction.n, null, hp, maxhp, speed, attackDistance, attackSpeed, NPC_DATABASE, null);
          }
          return npcs;
     }

     public ArrayList<String> queryNPCsAsString() {
          ArrayList<String> npcs = new ArrayList<String>();
          DBCollection collection = db.getCollection(NPC_DATABASE);
          DBCursor cursor = collection.find();
          while(cursor.hasNext()){
               DBObject npc = cursor.next();
               npcs.add(npc.toString());
          }
          return npcs;
     }

     @Override
     public ArrayList<WorldObject> queryWorldObjects(){
          ArrayList<WorldObject> worldObjects = new ArrayList<WorldObject>();
          DBCollection collection = db.getCollection(WORLD_OBJECT_DATABASE);
          DBCursor cursor = collection.find();
          while(cursor.hasNext()){
               DBObject wo = cursor.next();
               worldObjects.add(new WorldObject(wo.toMap()));
          }
          return worldObjects;
     }

     public ArrayList<String> queryWorldObjectsAsString() {
          ArrayList<String> worldObjects = new ArrayList<String>();
          DBCollection collection = db.getCollection(WORLD_OBJECT_DATABASE);
          DBCursor cursor = collection.find();
          while(cursor.hasNext()){
               DBObject worldObject = cursor.next();
               worldObjects.add(worldObject.toString());
          }
          return worldObjects;
     }

     @Override
     public ArrayList<Player> queryPlayers(){
          ArrayList<Player> players = new ArrayList<Player>();
          DBCollection collection = db.getCollection(PLAYER_DATABASE);
          DBCursor cursor = collection.find();
          while(cursor.hasNext()){
               DBObject player = cursor.next();
               players.add(new Player(player.toMap()));
          }
          return players;
     }

     public ArrayList<String> queryPlayersAsString() {
          ArrayList<String> players = new ArrayList<String>();
          DBCollection collection = db.getCollection(PLAYER_DATABASE);
          DBCursor cursor = collection.find();
          while(cursor.hasNext()){
               DBObject player = cursor.next();
               players.add(player.toString());
          }
          return players;
     }
}
