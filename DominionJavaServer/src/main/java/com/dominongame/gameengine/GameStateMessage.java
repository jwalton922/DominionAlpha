/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominongame.gameengine;

import java.util.HashMap;

/**
 * This contains information to send to clients about
 * changes in game state so that they can render them
 *
 * @author jwalton
 */
public class GameStateMessage {

     private HashMap<String,String> messageInfo;

     public GameStateMessage(HashMap<String,String> messageInfo){
          this.messageInfo = messageInfo;
     }

     public HashMap<String, String> getMessageInfo() {
          return messageInfo;
     }

     public void setMessageInfo(HashMap<String, String> messageInfo) {
          this.messageInfo = messageInfo;
     }
     
}
