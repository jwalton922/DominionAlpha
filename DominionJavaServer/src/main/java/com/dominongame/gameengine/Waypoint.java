/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dominongame.gameengine;

/**
 *
 * @author jwalton
 */
public class Waypoint {

     private int x;
     private int y;
     private Direction dir;

     public Waypoint(int x, int y, Direction dir){
          this.x = x;
          this.y = y;
          this.dir = dir;
     }

     public Direction getDir() {
          return dir;
     }

     public void setDir(Direction dir) {
          this.dir = dir;
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

     public String toString(){
          return "Waypoint: "+x+","+y+" "+dir.toString();
     }
}
