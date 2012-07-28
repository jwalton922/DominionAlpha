/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominiongame.dominionserver.database;

import com.dominongame.gameengine.NPC;
import com.dominongame.gameengine.Player;
import com.dominongame.gameengine.WorldObject;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jwalton
 */
public class MongoDbTest {

     @Test
     public void testNPCQuery() {
          DominionDBMongoImpl db = new DominionDBMongoImpl();
          ArrayList<NPC> npcs = db.queryNPCs();
          Assert.assertTrue(npcs.size() > 0);
          for (int i = 0; i < npcs.size(); i++) {
               NPC npc = npcs.get(i);
               Assert.assertTrue(npc.getName() != null);
               Assert.assertTrue(npc.getEntityType() != null);
               Assert.assertTrue(npc.getFaction() != null);
               Assert.assertTrue(npc.getAttackDistance() > 0);
               Assert.assertTrue(npc.getAttackSpeed() > 0);
               Assert.assertTrue(npc.getCurrentAction() != null);
               Assert.assertTrue(npc.getDirection() != null);
               Assert.assertTrue(npc.getHp() > 0);
               Assert.assertTrue(npc.getId() > 0);
               Assert.assertTrue(npc.getMaxhp() > 0);
               Assert.assertTrue(npc.getNextAction() != null);
               Assert.assertTrue(npc.getResourceMap() != null);
               Assert.assertTrue(npc.getResourceMap().get("lumber") != null && npc.getResourceMap().get("lumber") >= 0);
               Assert.assertTrue(npc.getResourceMap().get("iron") != null && npc.getResourceMap().get("iron") >= 0);
               Assert.assertTrue(npc.getResourceMap().get("gold") != null && npc.getResourceMap().get("gold") >= 0);
               Assert.assertTrue(npc.getSpeed() > 0);
               Assert.assertTrue(npc.getX() >= 0);
               Assert.assertTrue(npc.getY() >= 0);

          }

     }

     @Test
     public void testPlayerQuery(){
          DominionDBMongoImpl db = new DominionDBMongoImpl();
          ArrayList<Player> players = db.queryPlayers();
          Assert.assertTrue(players.size() > 0);
          for (int i = 0; i < players.size(); i++) {
               Player player = players.get(i);
               Assert.assertTrue(player.getName() != null);
               Assert.assertTrue(player.getEntityType() != null);
//               Assert.assertTrue(player.getFaction() != null);
               Assert.assertTrue(player.getAttackDistance() > 0);
               Assert.assertTrue(player.getAttackSpeed() > 0);
               Assert.assertTrue(player.getCurrentAction() != null);
               Assert.assertTrue(player.getDirection() != null);
               Assert.assertTrue(player.getHp() > 0);
               Assert.assertTrue(player.getId() > 0);
               Assert.assertTrue(player.getMaxhp() > 0);
               Assert.assertTrue(player.getNextAction() != null);
//               Assert.assertTrue(player.getResourceMap() != null);
//               Assert.assertTrue(player.getResourceMap().get("lumber") != null && npc.getResourceMap().get("lumber") >= 0);
//               Assert.assertTrue(player.getResourceMap().get("iron") != null && npc.getResourceMap().get("iron") >= 0);
//               Assert.assertTrue(player.getResourceMap().get("gold") != null && npc.getResourceMap().get("gold") >= 0);
               Assert.assertTrue(player.getSpeed() > 0);
               Assert.assertTrue(player.getX() >= 0);
               Assert.assertTrue(player.getY() >= 0);

          }
     }

     @Test
     public void testWorldObjectQuery(){
          DominionDBMongoImpl db = new DominionDBMongoImpl();
          ArrayList<WorldObject> objects = db.queryWorldObjects();
          Assert.assertTrue(objects.size() > 0);
          for (int i = 0; i < objects.size(); i++) {
               WorldObject wo = objects.get(i);
               Assert.assertTrue(wo.getName() != null);
               Assert.assertTrue(wo.getEntityType() != null);
               Assert.assertTrue(wo.getDirection() != null);
               Assert.assertTrue(wo.getResourceMap() != null);
               Assert.assertTrue(wo.getResourceMap().get("lumber") != null && wo.getResourceMap().get("lumber") >= 0);
               Assert.assertTrue(wo.getResourceMap().get("iron") != null && wo.getResourceMap().get("iron") >= 0);
               Assert.assertTrue(wo.getResourceMap().get("gold") != null && wo.getResourceMap().get("gold") >= 0);
               Assert.assertTrue(wo.getX() >= 0);
               Assert.assertTrue(wo.getY() >= 0);

          }
     }
}
