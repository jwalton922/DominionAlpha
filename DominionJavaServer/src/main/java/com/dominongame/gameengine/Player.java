/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author jwalton
 */
public class Player extends DominionEntity {

     private static Logger logger = Logger.getLogger(Player.class);
     private Action currentAction;
     /*
      * This queue is only for non-pause actions
      * if getNextAction is called on queue, and it's empty
      * it will return pause - the default action
      */
     private ArrayList<Action> actionQueue = new ArrayList<Action>();
     private CharacterState state;
     private int speed;
     private int attackDistance;
     private int attackSpeed;
     private int hp;
     private int maxhp;
     private boolean actionComplete = false;
     private boolean hasNewAction = false;
     private Long actionStartTime;
     private String className;
     protected ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

     public Player() {
     }

     public Player(Map<String,String> playerMap){
          super(playerMap);
          this.state = CharacterState.valueOf(playerMap.get("state"));
          int actionX = Integer.parseInt(playerMap.get("x"));
          int actionY = Integer.parseInt(playerMap.get("y"));
          String actionName = playerMap.get("action");
          this.currentAction = new Action("player", super.getId(), actionName, actionX, actionY, super.getDirection());
          this.hp = Integer.parseInt(playerMap.get("hp"));
          this.maxhp = Integer.parseInt(playerMap.get("maxhp"));
          this.attackDistance = Integer.parseInt(playerMap.get("attackDistance"));
          this.attackSpeed = Integer.parseInt(playerMap.get("attackSpeed"));
          this.speed = Integer.parseInt(playerMap.get("speed"));
          this.className = playerMap.get("className");
     }

     public Player(int id, String username, String className, CharacterState state, int x, int y, Direction dir, Action currentAction, int hp, int maxhp, int speed, int attackDistance, int attackSpeed) {
          super(id, username, x, y, dir);
          this.state = state;
          this.currentAction = currentAction;
          this.className = className;
          this.hp = hp;
          this.maxhp = maxhp;
          this.attackDistance = attackDistance;
          this.attackSpeed = attackSpeed;
          this.speed = speed;
     }

     public void addActionToQueue(Action action) {
          logger.debug("Player " + super.getName() + " id: " + super.getId() + " adding action to queue: " + action.toString());
          if (action.getActionName().equalsIgnoreCase("move")) {
               ArrayList<Waypoint> actionWaypoints = Util.createWaypoints(super.getX(), super.getY(), action.getX(), action.getY(), speed);
               action.setWaypoints(actionWaypoints);
               this.actionQueue.add(action);
          } else if (action.getActionName().equalsIgnoreCase("attack")) {
               ArrayList<Waypoint> actionWaypoints = Util.createWaypoints(super.getX(), super.getY(), action.getX(), action.getY(), speed);
               if (!actionWaypoints.isEmpty()) {
                    //move actions x,y, and direction should not matter
                    Action moveAction = new Action("player", super.getId(), "move", -1, -1, this.getDirection());
                    moveAction.setWaypoints(actionWaypoints);
                    this.actionQueue.add(moveAction);
               }
               this.actionQueue.add(action);
          } else {
               throw new RuntimeException("Unknown action: " + action.getActionName());
          }
          this.hasNewAction = true;
     }

     /**
      * returns pause action if queue is empty
      * 
      * @return
      */
     public Action getNextAction() {
          if (actionQueue.isEmpty()) {
               return new Action(this.getEntityType(), this.getId(), "pause", this.getX(), this.getY(), this.getDirection());
          } else {
               Action nextAction = actionQueue.remove(0);
               logger.debug(this.getName() + "'s next action: " + nextAction.toString());

               return nextAction;
          }
     }

     @Override
     public String getEntityType(){
          return "player";
     }

     /**
      * returns null if no more waypoints
      *
      * @return
      */
     public Waypoint getNextWaypoint() {
          if (waypoints.isEmpty()) {
               return null;
          } else {
               Waypoint waypoint = waypoints.remove(0);
               return waypoint;
          }
     }

     public boolean hasWaypoints(){
          if(waypoints != null && waypoints.size() != 0){
               return true;
          } else {
               return false;
          }
     }

     @Override
     public HashMap<String,String> toHashMap(){
          HashMap<String,String> map = new HashMap<String,String>();
          map.putAll(super.toHashMap());
          map.put("state", state.toString());
          map.put("speed", ""+speed);
          map.put("attackDistance", ""+attackDistance);
          map.put("attackSpeed", ""+attackSpeed);
          map.put("hp", ""+hp);
          map.put("maxhp",""+maxhp);
          map.put("actionStartTime", ""+actionStartTime);
          map.put("action", currentAction.getActionName());
          map.put("className", className);

          return map;
     }

     @Override
     public Player clone() {
          DominionEntity de = super.clone();
          return new Player(de.getId(), de.getName(), this.className, this.state, de.getX(), de.getY(), de.getDirection(), this.currentAction.clone(), this.hp, this.maxhp, this.speed, this.attackDistance, this.attackSpeed);
     }

     public String getClassName() {
          return className;
     }

     public void setClassName(String className) {
          this.className = className;
     }

     public void setActionStartTime(long time){
          this.actionStartTime = time;
     }

     public Long getActionStartTime(){
          return this.actionStartTime;
     }

     public boolean isActionComplete() {
          return actionComplete;
     }

     public void setActionComplete(boolean actionComplete) {
          this.actionComplete = actionComplete;
     }

     public int getAttackDistance() {
          return attackDistance;
     }

     public void setAttackDistance(int attackDistance) {
          this.attackDistance = attackDistance;
     }

     public int getAttackSpeed() {
          return attackSpeed;
     }

     public void setAttackSpeed(int attackSpeed) {
          this.attackSpeed = attackSpeed;
     }

     public Action getCurrentAction() {
          return currentAction;
     }

     public void setCurrentAction(Action currentAction) {
          this.currentAction = currentAction;
     }

     public boolean isHasNewAction() {
          return hasNewAction;
     }

     public void setHasNewAction(boolean hasNewAction) {
          this.hasNewAction = hasNewAction;
     }

     public int getHp() {
          return hp;
     }

     public void setHp(int hp) {
          this.hp = hp;
     }

     public int getMaxhp() {
          return maxhp;
     }

     public void setMaxhp(int maxhp) {
          this.maxhp = maxhp;
     }

     public int getSpeed() {
          return speed;
     }

     public void setSpeed(int speed) {
          this.speed = speed;
     }

     public CharacterState getState() {
          return state;
     }

     public void setState(CharacterState state) {
          this.state = state;
     }

     public void setWaypoints(ArrayList<Waypoint> waypoints) {
          this.waypoints = waypoints;
     }
}
