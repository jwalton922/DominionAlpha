/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominiongame.dominionserver.database;

import com.dominongame.gameengine.GameStateContainer;
import com.dominongame.gameengine.NPC;
import com.dominongame.gameengine.Player;
import com.dominongame.gameengine.WorldObject;
import java.util.ArrayList;

/**
 *
 * @author jwalton
 */
public interface DominionDBIF {

     public ArrayList<NPC> queryNPCs();
     public ArrayList<String> queryNPCsAsString();
     public ArrayList<WorldObject> queryWorldObjects();
     public ArrayList<String> queryWorldObjectsAsString();
     public ArrayList<Player> queryPlayers();
     public ArrayList<String> queryPlayersAsString();
     public GameStateContainer queryGameState();
}
