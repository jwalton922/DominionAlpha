/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *This class is base class for any object in the game.
 * Has basic information required for any game object
 *
 * @author jwalton
 */
public class DominionEntity {

     private static Logger log = Logger.getLogger(DominionEntity.class);

     private int x;
     private int y;
     private Direction direction;
     private int id;
     private String name;

     public DominionEntity() {
     }

     public DominionEntity(int id, String name, int x, int y, Direction direction) {
          this.id = id;
          this.name = name;
          this.x = x;
          this.y = y;
          this.direction = direction;
     }

     public DominionEntity(Map<String,String> map){
          this.id = Integer.parseInt(map.get("id"));
          if(map.get("name") != null){
               this.name = map.get("name");
          } else if(map.get("username") != null){
               this.name = map.get("username");
          } else {
               log.error("Error creating dominion entity, no name found! "+map.toString());
          }
          this.x = Integer.parseInt(map.get("x"));
          this.y = Integer.parseInt(map.get("y"));
          if(map.get("direction") != null){
               this.direction = Direction.valueOf(map.get("direction"));
          } else if(map.get("dir") != null){
               this.direction = Direction.valueOf(map.get("dir"));
          } else {
               log.error("Error creating dominion entity, no direction found! "+map.toString());
          }

     }


     public HashMap<String,String> toHashMap(){
          HashMap<String,String> map = new HashMap<String,String>();
          map.put("id", ""+id);
          map.put("name", name);
          map.put("x", ""+x);
          map.put("y", ""+y);
          map.put("direction", direction.toString());

          return map;
     }

     public String getEntityType(){
          return "DominionEntity";
     }

     @Override
     public DominionEntity clone(){
          return new DominionEntity(this.id, this.name, this.x, this.y, this.direction);
     }

     public Direction getDirection() {
          return direction;
     }

     public void setDirection(Direction direction) {
          this.direction = direction;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
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


}
