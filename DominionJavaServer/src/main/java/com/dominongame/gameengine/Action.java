/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominongame.gameengine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jwalton
 */
public class Action {

     
     private String actionName;
     private int x;
     private int y;
     private Direction direction;
     private String entityType;
     private int entityId;
     private ArrayList<Waypoint> waypoints;
     private Player target;

     public Action(String entityType, int entityId, String name, int x, int y, Direction dir){
          this.entityType = entityType;
          this.entityId = entityId;
          this.actionName = name;
          this.x = x;
          this.y = y;
          this.direction = dir;
     }

     public GameStateMessage createGameStateMessage(){
          HashMap<String,String> messageInfo = new HashMap<String,String>();
          messageInfo.put("x",""+this.x);
          messageInfo.put("y",""+this.y);
          messageInfo.put("type", "action");
          messageInfo.put("action", this.actionName);
          messageInfo.put("playerId",""+this.entityId);
          messageInfo.put("direction", this.direction.toString().toLowerCase());

          return new GameStateMessage(messageInfo);
     }

     public Player getTarget() {
          return target;
     }

     public void setTarget(Player target) {
          this.target = target;
     }
     
     public ArrayList<Waypoint> getWaypoints() {
          return waypoints;
     }

     public void setWaypoints(ArrayList<Waypoint> waypoints) {
          this.waypoints = waypoints;
     }
     
     public int getEntityId() {
          return entityId;
     }

     public void setEntityId(int entityId) {
          this.entityId = entityId;
     }

     public String getEntityType() {
          return entityType;
     }

     public void setEntityType(String entityType) {
          this.entityType = entityType;
     }

     public String getActionName() {
          return actionName;
     }

     public void setActionName(String actionName) {
          this.actionName = actionName;
     }

     public Direction getDirection() {
          return direction;
     }

     public void setDirection(Direction direction) {
          this.direction = direction;
     }

     public int getX() {
          return x;
     }

     public void setX(int x) {
          this.x = x;
     }

     public int getY() {
          return y;
     }

     public void setY(int y) {
          this.y = y;
     }
     
     @Override
     public Action clone(){
          return new Action("Action: "+this.entityType, this.entityId,this.actionName, this.x, this.y, this.direction);
     }
}
