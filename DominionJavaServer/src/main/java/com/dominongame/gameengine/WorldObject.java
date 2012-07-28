/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jwalton
 */
public class WorldObject extends DominionEntity {

     private boolean mineable; //true if it has resources
     private HashMap<String, Integer> resourceMap = new HashMap<String,Integer>();

     public WorldObject() {
     }

     public WorldObject(int id, String name, int x, int y, Direction dir, boolean isMineable, HashMap<String, Integer> resourceMap) {
          super(id, name, x, y, dir);
          this.mineable = isMineable;
          this.resourceMap = resourceMap;
     }

     public WorldObject(Map<String, String> worldObjectMap) {
          super(worldObjectMap);

          if (worldObjectMap.get("lumber") != null) {
               this.resourceMap.put("lumber", Integer.parseInt(worldObjectMap.get("lumber")));
          } else {
               this.resourceMap.put("lumber", 0);
          }

          if (worldObjectMap.get("gold") != null) {
               this.resourceMap.put("gold", Integer.parseInt(worldObjectMap.get("gold")));
          } else {
               this.resourceMap.put("gold", 0);
          }

          if (worldObjectMap.get("iron") != null) {
               this.resourceMap.put("iron", Integer.parseInt(worldObjectMap.get("iron")));
          } else {
               this.resourceMap.put("iron", 0);
          }
          mineable = false;
          for (String key : resourceMap.keySet()) {
               if (resourceMap.get(key) > 0) {
                    mineable = true;
                    break;
               }
          }

     }

     public WorldObject clone() {
          return new WorldObject(super.getId(), super.getName(), super.getX(), super.getY(), super.getDirection(), this.mineable, this.resourceMap);
     }

     public boolean isMineable() {
          return mineable;
     }

     public void setMineable(boolean mineable) {
          this.mineable = mineable;
     }

     public HashMap<String, Integer> getResourceMap() {
          return resourceMap;
     }

     public void setResourceMap(HashMap<String, Integer> resourceMap) {
          this.resourceMap = resourceMap;
     }
}
