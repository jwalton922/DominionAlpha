/*
 * This Update engine simply sets all state to that received by server.
 * There is zero lag compensation, client prediction, etc.
 */

SimpleUpdateEngine = {};

/*
 * method sends client action request to server,
 * updates state based on server information
 * updates sprites
 */
SimpleUpdateEngine.update = function(){
  //grab client input
  if(GameEngine.hasClick()){
    var coord = GameEngine.getClick();
    var clickX = coord[0];
    var clickY = coord[1];
    this.processAction(clickX, clickY);
  }
}

SimpleUpdateEngine.processAction(x, y){
    console.debug("Determining action for click: "+clickX+","+clickY);
    if(GameEngine.characterActionIsInterruptable(GameEngine.currentPlayer)){
      var target = GameEngine.findTarget(clickX,clickY);
      if(target == null){
        console.debug("Did not find target. Action is a move");

        var waypoints = GameEngine.createWayPoints(currentPlayer.getX(), currentPlayer.getY(), clickX, clickY);
        currentPlayer.setWayPoints(waypoints);
        currentPlayer.setAction("move");
        newAction = true;

      } else {
        console.debug("Found target: "+target.toString());
      }
    }
}

SimpleUpdateEngine.processMoveRequest(x, y){

}

SimpleUpdateEngine.processActionRequest(x, y, target){

}