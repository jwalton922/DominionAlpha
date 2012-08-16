/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author jwalton
 */
public class GameEngine implements GameEngineIF {

     public HashMap<Integer, Player> playerMap;
     public HashMap<Integer, WorldObject> worldObjectMap;
     public HashMap<Integer, NPC> npcMap;
     private Logger logger = Logger.getLogger(GameEngine.class);
     private ArrayList<GameStateMessage> messages;

     public GameEngine(HashMap<Integer, Player> players, HashMap<Integer, WorldObject> worldObjects, HashMap<Integer,NPC> npcs){
          this.playerMap = players;
          this.worldObjectMap = worldObjects;
          this.npcMap = npcs;
     }

     @Override
     public Player getPlayer(String username){
          logger.debug("getPlayer called for username = "+username);
          Player retPlayer = null;
          for(Player player: playerMap.values()){
               logger.debug("checkign player: "+player.getName());
               if(player.getName().equalsIgnoreCase(username)){
                    retPlayer = player;
                    break;
               }
          }

          return retPlayer;
     }

     /**
      * This method will update state for the next time step
      * and process any new actions passed in
      * @param actions
      */
     @Override
     public void updateState(Collection<Action> actions) {
          ArrayList<GameStateMessage> newMessages = new ArrayList<GameStateMessage>();
          //add actions to queues
          for (Action action : actions) {
               if (action.getEntityType().equalsIgnoreCase("player")) {
                    playerMap.get(action.getEntityId()).addActionToQueue(action);
               } else if (action.getEntityType().equalsIgnoreCase("npc")) {
                    npcMap.get(action.getEntityId()).addActionToQueue(action);
               }
          }

          for (Player p : playerMap.values()) {
               newMessages.addAll(updatePlayer(p));
          }
          for (NPC npc : npcMap.values()) {
               //basic updates the same
               updatePlayer(npc);
               //update specific npc stuff...not sure if this the way
               //this should be done
               updateNPC(npc);
          }

          this.messages = newMessages;
     }

     /**
      * returns copy of current game state objects
      * 
      * @return
      */
     public GameStateContainer getGameState() {
          Collection<Player> playersCopy = new ArrayList<Player>();
          Collection<WorldObject> worldObjectsCopy = new ArrayList<WorldObject>();
          Collection<NPC> npcsCopy = new ArrayList<NPC>();
          Collection<GameStateMessage> messagesCopy = new ArrayList<GameStateMessage>(messages);
          for (Player p : playerMap.values()) {
               playersCopy.add(p.clone());
          }

          for (WorldObject wo : worldObjectMap.values()) {
               worldObjectsCopy.add(wo.clone());
          }

          for (NPC npc : npcMap.values()) {
               npcsCopy.add(npc.clone());
          }
          return new GameStateContainer(playersCopy, npcsCopy, worldObjectsCopy, messagesCopy);
     }

     /**
      * returns list of game state messages
      *
      * @param player
      * @return
      */
     private ArrayList<GameStateMessage> updatePlayer(Player player) {
          ArrayList<GameStateMessage> messages = new ArrayList<GameStateMessage>();
          if (player.getState() == CharacterState.DEAD) {
               return messages;
          }

          if (player.isHasNewAction()) {
               logger.debug(player.getName() + " has new action!");
               if (!player.getCurrentAction().getActionName().equalsIgnoreCase("attack")) {
                    //means current action is pause or mvoe
                    //so we will update to next action
                    Action action = player.getNextAction();
                    double deltaX = action.getX() - player.getX();
                    double deltaY = action.getY() - player.getY();
                    action.setDirection(Util.determineDirection(deltaX, deltaY));
                    messages.add(action.createGameStateMessage());
                    player.setHasNewAction(false);
                    player.setCurrentAction(action);
                    logger.debug(player.getName() + "'s new action is: " + action.getActionName());
                    player.setActionStartTime(System.currentTimeMillis());
                    if (player.getCurrentAction().getActionName().equalsIgnoreCase("move")) {
                         player.setWaypoints(player.getCurrentAction().getWaypoints());
                         player.setDirection(player.getCurrentAction().getDirection());
                    }
               }
          }

          if (player.hasWaypoints()) {
               //this removes waypoint as well
               Waypoint waypoint = player.getNextWaypoint();
               if (waypoint != null) {
                    player.setX(waypoint.getX());
                    player.setY(waypoint.getY());
                    player.setDirection(waypoint.getDir());
                    logger.debug("setting player location: "+waypoint.toString());
                    if (!player.hasWaypoints()) {
                         logger.debug("No more waypoints for " + player.getName());
                         //we'll swap actions next time through
                         player.setHasNewAction(true);
                    }
               }
          } else if (player.getCurrentAction().getActionName().equalsIgnoreCase("attack")) {
               //probably should make this for non move or pause actions
               //instead of just attack
               logger.debug("Checking timestamp for " + player.getName() + "'s attack");
               long time = System.currentTimeMillis();
               long timeSinceActionStart = time - player.getActionStartTime();
               if (timeSinceActionStart > 1000) {
                    logger.debug(player.getName() + "'s attack is finished");
                    GameStateMessage message = doAttack(player);
                    messages.add(message);
                    player.setHasNewAction(true);

               }
          }
          return messages;
     }

     private GameStateMessage doAttack(Player player) {
          logger.debug("Player "+player.getName()+" is attacking!");
          int damage = (int) (Math.round(Math.random() * 8) + 1);
          Player target = player.getCurrentAction().getTarget();
          logger.debug("Target is : "+target.getName());
          target.setHp(target.getHp() - damage);
          if (target.getHp() <= 0) {
               target.setHp(0);
               target.setState(CharacterState.DEAD);
          }
          HashMap<String, String> messageInfo = new HashMap<String, String>();
          messageInfo.put("x", "" + target.getX());
          messageInfo.put("y", "" + target.getY());
          messageInfo.put("type", "damage");
          messageInfo.put("source", player.getName());
          messageInfo.put("time", "" + System.currentTimeMillis());
          messageInfo.put("message", "" + damage);

          return new GameStateMessage(messageInfo);
     }

     private void updateNPC(NPC npc) {
     }

     public void addNewPlayer(Player p) {
          playerMap.put(p.getId(), p);
     }

     public void addWorldObject(WorldObject wo) {
          worldObjectMap.put(wo.getId(), wo);
     }

     public void addNPC(NPC npc) {
          npcMap.put(npc.getId(), npc);
     }

     public static void main(String[] args) {
          System.out.println(CharacterState.DEAD.toString());
     }
}
