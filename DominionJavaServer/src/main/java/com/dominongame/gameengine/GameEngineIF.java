/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominongame.gameengine;

import java.util.Collection;

/**
 *
 * @author jwalton
 */
public interface GameEngineIF {

     public void updateState(Collection<Action> actions);

     public GameStateContainer getGameState();

     public Player getPlayer(String username);

}
