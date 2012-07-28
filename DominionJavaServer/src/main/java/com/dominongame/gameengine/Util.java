/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dominongame.gameengine;

import java.util.ArrayList;

/**
 *
 * @author jwalton
 */
public class Util {

     public static Direction determineDirection(double deltaX, double deltaY) {
          Direction dir = Direction.e;
          double angle = 0;
          if (deltaX != 0) {
               angle = 180.0 / Math.PI * Math.atan(deltaY / deltaX);
          }


          if (deltaX > 0 && deltaY > 0) {
               if (angle <= 22.5) {
                    dir = Direction.e;
               } else if (angle <= 67.5) {
                    dir = Direction.se;
               } else {
                    dir = Direction.s;
               }
          } else if (deltaX < 0 && deltaY > 0) {
               if (angle >= -22.5) {
                    dir = Direction.w;
               } else if (angle >= -67.5) {
                    dir = Direction.sw;
               } else {
                    dir = Direction.s;
               }
          } else if (deltaX < 0 && deltaY < 0) {
               if (angle < 22.5) {
                    dir = Direction.w;
               } else if (angle < 67.5) {
                    dir = Direction.nw;
               } else {
                    dir = Direction.n;
               }

          } else if (deltaX > 0 && deltaY < 0) {
               if (angle >= -22.5) {
                    dir = Direction.e;
               } else if (angle >= -67.5) {
                    dir = Direction.ne;
               } else {
                    dir = Direction.n;
               }
          } else if (deltaX == 0) {
               if (deltaY > 0) {
                    dir = Direction.s;
               } else if (deltaY < 0) {
                    dir = Direction.n;
               }
          } else if (deltaY == 0) {
               if (deltaX > 0) {
                    dir = Direction.e;
               } else if (deltaX < 0) {
                    dir = Direction.w;
               }
          }
          return dir;
     }

     public static ArrayList<Waypoint> createWaypoints(int startX, int startY, int endX, int endY, int speed) {
          
          ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
          int dx = endX - startX;
          int dy = endY - startY;
          double distance = Math.sqrt(dx * dx + dy * dy);
          //TODO might have to return a waypoint with a direction in case direction is different...
          if(distance <= 0){
               return waypoints;
          }
          Direction dir = determineDirection(dx,dy);
          
          int numPointsRequired = (int) Math.floor(distance / (1.0 * speed));

          double stepX = dx / (1.0 * numPointsRequired);
          double stepY = dy / (1.0 * numPointsRequired);

          double x = startX;
          double y = startY;

          for (int i = 0; i < numPointsRequired; i++) {
               x += stepX;
               y += stepY;
               if(x < 0 || y < 0){
                    break;
               }
               waypoints.add(new Waypoint((int)x, (int)y, dir));
          }
          
          if(endX >= 0 && endY >= 0){
               Waypoint endPoint = new Waypoint(endX,endY,dir);
               waypoints.set(waypoints.size()-1, endPoint);
          }
          
          return waypoints;
     }
}
