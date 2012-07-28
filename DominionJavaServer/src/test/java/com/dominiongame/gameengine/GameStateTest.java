/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominiongame.gameengine;

import com.dominiongame.dominionserver.database.DominionDBMongoImpl;
import com.dominongame.gameengine.GameStateContainer;
import org.junit.Test;

/**
 *
 * @author jwalton
 */
public class GameStateTest {

     @Test
     public void testToJSONString(){
          DominionDBMongoImpl db = new DominionDBMongoImpl();

          GameStateContainer state = db.queryGameState();

          System.out.println(state.toJsonStringEasy());


     }

}
