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
public class NPC extends Player{

     private String faction;
     private HashMap<String,Integer> resourceMap;

     public NPC(int id, String username, String className, CharacterState state, int x, int y, Direction dir, Action currentAction, int hp, int maxhp, int speed, int attackDistance, int attackSpeed, String faction, HashMap<String,Integer> resourceMap){
          super(id, username, className, state, x, y, dir, currentAction, hp, maxhp, speed, attackDistance, attackSpeed);
          this.faction = faction;
          this.resourceMap = resourceMap;
          if(this.resourceMap.get("lumber") == null){
               this.resourceMap.put("lumber", 0);
          }
          if(this.resourceMap.get("gold") == null){
               this.resourceMap.put("gold", 0);
          }
          if(this.resourceMap.get("iron") == null){
               this.resourceMap.put("iron", 0);
          }
     }

     public NPC(Map<String,String> npcMap){
          super(npcMap);
          this.faction = (String) npcMap.get("faction");
          this.resourceMap = new HashMap<String,Integer>();
          if(npcMap.get("lumber") != null){
               this.resourceMap.put("lumber",Integer.parseInt(npcMap.get("lumber")));
          } else {
               this.resourceMap.put("lumber",0);
          }

          if(npcMap.get("gold") != null){
               this.resourceMap.put("gold",Integer.parseInt(npcMap.get("gold")));
          } else {
               this.resourceMap.put("gold",0);
          }

          if(npcMap.get("iron") != null){
               this.resourceMap.put("iron",Integer.parseInt(npcMap.get("iron")));
          } else {
               this.resourceMap.put("iron",0);
          }

     }

     public NPC clone(){
          HashMap<String,Integer> resourceCopy = new HashMap<String,Integer>();
          resourceCopy.putAll(resourceMap);
          Player p = super.clone();
          return new NPC(p.getId(), p.getName(), p.getClassName(), p.getState(), p.getX(), p.getY(), p.getDirection(), p.getCurrentAction(), p.getHp(), p.getMaxhp(), p.getSpeed(), p.getAttackDistance(), p.getAttackSpeed(), faction, resourceCopy);
     }

     @Override
     public String getEntityType(){
          return "npc";
     }

     public String getFaction() {
          return faction;
     }

     public void setFaction(String faction) {
          this.faction = faction;
     }

     public HashMap<String, Integer> getResourceMap() {
          return resourceMap;
     }

     public void setResourceMap(HashMap<String, Integer> resourceMap) {
          this.resourceMap = resourceMap;
     }

     public HashMap<String,String> toHashMap(){
          HashMap map = new HashMap<String,String>();
          map.putAll(super.toHashMap());
          for(String key : resourceMap.keySet()){
               map.put(key, ""+resourceMap.get(key));
          }
          //map.putAll(resourceMap);
          map.put("faction", faction);

          return map;
     }


}
