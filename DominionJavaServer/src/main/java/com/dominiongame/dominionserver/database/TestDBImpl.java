/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominiongame.dominionserver.database;

import com.dominiongame.util.DataInitializer;
import com.dominongame.gameengine.GameStateContainer;
import com.dominongame.gameengine.NPC;
import com.dominongame.gameengine.Player;
import com.dominongame.gameengine.WorldObject;
import java.util.ArrayList;

/**
 *
 * @author jwalton
 */
public class TestDBImpl implements DominionDBIF{
    
    private ArrayList<NPC> npcs;
    private ArrayList<WorldObject> worldObjects;
    private ArrayList<Player> players;
    
    public TestDBImpl(){
        npcs = DataInitializer.createNPCs();
        worldObjects = DataInitializer.createWorldObjects();
        players = DataInitializer.createPlayers();
    }

    @Override
    public ArrayList<NPC> queryNPCs() {
        return npcs;
    }

    @Override
    public ArrayList<String> queryNPCsAsString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<WorldObject> queryWorldObjects() {
        return worldObjects;
    }

    @Override
    public ArrayList<String> queryWorldObjectsAsString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Player> queryPlayers() {
        return players;
    }

    @Override
    public ArrayList<String> queryPlayersAsString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GameStateContainer queryGameState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
