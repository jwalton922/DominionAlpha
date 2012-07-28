/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominiongame.util;

import com.dominiongame.dominionserver.database.DominionDBMongoImpl;
import com.dominongame.gameengine.Action;
import com.dominongame.gameengine.Direction;
import com.dominongame.gameengine.GameEngine;
import com.dominongame.gameengine.GameStateContainer;
import com.dominongame.gameengine.GameStateMessage;
import com.dominongame.gameengine.NPC;
import com.dominongame.gameengine.Player;
import com.dominongame.gameengine.WorldObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * This will use server data to create the messages the client would
 * receive while the game is running. The data is stored in javascript
 * objects written to file for use in client javascript code testing
 * 
 *
 *
 * @author jwalton
 */
public class ClientJSDataCreator {

     // root home of project minus
     private static final String PROJECT_DIR = "/home/jwalton/workspace/Dominion_";
     private static final String VERSION = "0.5";
     private static final String TEST_DIR = "public/javascripts/jasmineTests/";
     private static final String OUTPUT_DIR = PROJECT_DIR+VERSION+"/"+TEST_DIR;

     private static GameStateContainer readData(){
          DominionDBMongoImpl db = new DominionDBMongoImpl();
          GameStateContainer gameState = db.queryGameState();

          return gameState;
     }

     public static void createTestData(){
          File outdir = new File(OUTPUT_DIR);
          if(!outdir.exists()){
               outdir.mkdirs();
          }
          GameStateContainer gameData = readData();
          Collection<WorldObject> worldObjects = gameData.getWorldObjects();
          Collection<NPC> npcs = gameData.getNpcs();
          Collection<Player> players = gameData.getPlayers();

          HashMap<Integer, NPC> npcMap = new HashMap<Integer, NPC>();
          HashMap<Integer, Player> playerMap = new HashMap<Integer, Player>();
          HashMap<Integer, WorldObject> objectMap = new HashMap<Integer, WorldObject>();

          for (NPC npc : npcs) {
               npcMap.put(npc.getId(), npc);
          }

          for (Player player : players) {
               playerMap.put(player.getId(), player);
          }

          for (WorldObject object : worldObjects) {
               objectMap.put(object.getId(), object);
          }



          WorldObject wo = worldObjects.iterator().next();
          NPC npc = npcs.iterator().next();
          Player player = players.iterator().next();
          //now make up some actions
          GameEngine engine = new GameEngine(playerMap, objectMap, npcMap);

          int actionX = player.getX()+200;
          int actionY = player.getY()+200;
          Direction actionDir = Direction.se;
          Action a = new Action(player.getEntityType(), player.getId(), "move", actionX, actionY, actionDir);
          List<Action> actions = new ArrayList<Action>();
          actions.add(a);
          engine.updateState(actions);

          GameStateContainer state = engine.getGameState();
          GameStateMessage playerMoveMessage = state.getMessages().iterator().next();

          writeMapToFile(wo.toHashMap(), new File(OUTPUT_DIR+"/WorldObjectData.js"), "WorldObjectData");
          writeMapToFile(player.toHashMap(), new File(OUTPUT_DIR+"/PlayerData.js"), "PlayerData");
          writeMapToFile(npc.toHashMap(), new File(OUTPUT_DIR+"/NPCData.js"), "NPCData");
          writeMapToFile(playerMoveMessage.getMessageInfo(), new File(OUTPUT_DIR+"/MessageData.js"), "PlayerMoveData");





     }

     private static void writeMapToFile(HashMap<String,String> data, File f, String scope){
          System.out.println("Creating file "+f.getAbsolutePath()+"/"+f.getName());
          try {
               if(f.exists()){
                    f.delete();
               }
               f.createNewFile();
               BufferedWriter bw = new BufferedWriter(new FileWriter(f));
               String scopeLine = "var "+scope+" = {}";
               System.out.println(scopeLine);
               bw.append(scopeLine);
               bw.newLine();
               for(String key : data.keySet()){
                    String value = data.get(key);
                    boolean number = false;
                    try {
                         Double.parseDouble(value);
                         number = true;
                    } catch(Exception e){

                    }
                    if(!number){
                         value = "\""+value+"\"";
                    }
                    String line = scope+"."+key+" = "+value;
                    bw.append(line);
                    System.out.println(line);
                    bw.newLine();
               }

               bw.flush();
               bw.close();

          } catch(Exception e){
               e.printStackTrace();
          }
     }

     public static void main(String[] args){
          ClientJSDataCreator.createTestData();
     }
}
