var GameEngine = {};
GameEngine.currentPlayer = null;
GameEngine.characters = [];
GameEngine.clickX = -1;
GameEngine.clickY = -1;
GameEngine.serverMessages = {}; //messages from server
GameEngine.clientStates = {}; //history of character states

GameEngine.init = function(){
  Communication.init();
  
  var username = $("#username").html();
  username = username.substring(0,username.length -1);
  var token = $("#token").html();
  console.debug("Requesting client data for "+username+" token: "+token);
  Communication.requestClientData(username, token);
}

GameEngine.getServerMessages = function(){
  return this.serverMessages;
}

GameEngine.getClientStates = function(){
  return this.clientStates;
}

GameEngine.getCurrentPlayer = function(){
  return this.currentPlayer;
}

GameEngine.start = function(){
//  console.debug("Client's player: "+this.currentPlayer.toString());
//  var characters = [];
//  characters.push(this.currentPlayer);
//  console.debug("Calling drawCharacters on "+characters.length);
//  RenderEngine.drawCharacters(characters);
//  console.debug("Everything initialized.");
  var displayRefreshRate = 40;
  var displayInterval = 1000/displayRefreshRate;
  setInterval(RenderEngine.draw, displayInterval);
}

GameEngine.initPlayer = function(data){
  console.debug("Initializing player with: "+JSON.stringify(data));
  var newPlayer = new Character();
  
  var pausedAction = createActionSpriteList("images/knight/knight_pause/", "paused", "PNG", 96, 96, 5, 9, true);
  var movingAction = createActionSpriteList("images/knight/knight_move/", "running", "PNG", 96, 96, 5, 9, true);
  var attackingAction = createActionSpriteList("images/knight/knight_attack/", "attack", "PNG", 96, 96, 5, 9, false);
  var dyingAction = createActionSpriteList("images/knight/knight_dying/", "tipping", "PNG", 96, 96, 5, 9, false);
  var actionMap = {};
  actionMap["pause"] = pausedAction;
  actionMap["move"] = movingAction;
  actionMap["attack"] = attackingAction;
  actionMap["dying"] = dyingAction;

  newPlayer.setUsername(data.name);
  newPlayer.setState(data.state);
  newPlayer.setId(data.id)
  newPlayer.setSprites(actionMap);
  newPlayer.setX(data.x);
  newPlayer.setY(data.y);
  newPlayer.setWidth(64);
  newPlayer.setHeight(64);
  newPlayer.setDirection(data.direction);
  newPlayer.setHP(data.hp);
  newPlayer.setMaxHP(data.maxhp);
  newPlayer.setFaction(data.name)
  newPlayer.setClassName(data.className);
  console.debug("received action: "+data.action);
  newPlayer.setAction(data.action);
  console.debug("Created new player: "+newPlayer.toString());
  this.characters.push(newPlayer);
  return newPlayer;
}

GameEngine.handleClick = function(x, y){
  console.debug("Storing click on map coord: "+x+","+y);
  this.clickX = x;
  this.clickY = y;
  var targetCharacter = this.findCharacter(x,y);
  if(targetCharacter == null){
    console.debug("Did not find a target so action is a move");

  }
}

GameEngine.resetClickCoords = function(){
  console.debug("Resetting click coords");
  this.clickX = -1;
  this.clickY = -1;
}

GameEngine.hasClick = function(){
  if(this.clickX > -1 && this.clickY > -1){
    return true;
  } else {
    return false;
  }
}

GameEngine.characterActionIsInterruptable = function(character){
  if(character.getAction() == "move" || character.getAction() == "pause"){
    return true;
  } else {
    return false;
  }
}

GameEngine.getClick = function(){
  var coords = [];
  coords.push(this.clickX);
  coords.push(this.clickY);
  return coords;
}

GameEngine.findCharacter = function(x, y){
  var foundCharacter = null;
  for(var i = 0; i < this.characters.length; i++){
    var deltaX = this.characters[i].getWidth()/2;
    var deltaY = this.characters[i].getHeight()/2;
    var charX = this.characters[i].getX();
    var charY = this.characters[i].getY();

    var minX = charX - deltaX;
    var maxX = charX + deltaX;

    var minY = charY - deltaY;
    var maxY = charY + deltaY;

    if(x >= minX && x <= maxX && y >= minY && y <= maxY){
      foundCharacter = characters[i];
      break;
    }
  }

  return foundCharacter;
}

GameEngine.createWayPoints = function(initX, initY, destX, destY, speed) {

  /* determine number of steps to get to destination */

  var dx = destX - initX;
  var dy = destY - initY;
  var angle = Math.atan(dy / dx);
  var distToTravel = Math.sqrt(dx*dx+dy*dy);
  var numPointsReq = distToTravel / speed;
  var atDestination = false;
  var deltaX = Math.abs(speed * Math.cos(Math.PI / 180.0 * angle));
  var deltaY = Math.abs(speed * Math.sin(Math.PI / 180.0 * angle));

  var direction = GameEngine.determineDirection(dx, dy);

  if(dx > 0 && dy > 0) {
  //both are pos, good here
  } else if(dx < 0 && dy > 0) {
    deltaX = deltaX * -1;
  } else if(dx < 0 && dx < 0) {
    deltaX = deltaX * -1;
    deltaY = deltaY * -1;
  } else if(dx > 0 && dy < 0) {
    deltaY = deltaY * -1;
  }

  var waypoints = new Array();
  var x = initX;
  var y = initY;
  Math.sqrt()
  var numPoints = 0;
  /* potential rounding issues here - at high speeds won't end
     * up exactly at destination, might have to change later */
  while(numPoints < numPointsReq) {
    x = x+deltaX;
    y = y+deltaY;
    var waypoint = new WayPoint(x,y,direction);
    waypoints.push(waypoint);

    numPoints++;
  }

  waypoints.pop();
  var lastWayPoint = new WayPoint(destX,destY, direction);
  waypoints.push(lastWayPoint);
  /* reverse waypoints so pop can be used when getting them */
  waypoints.reverse();
  return waypoints;
}

/* could get errors or return wrong direction if
 * deltaX and deltaY = 0
 */
GameEngine.determineDirection = function(deltaX, deltaY) {
  var angle = 0;
  if(deltaX != 0) {
    angle = 180.0 / Math.PI * Math.atan(deltaY/deltaX);
  }
  //alert("angle: "+angle);
  //postive x is to the right
  //positive y is down
  var direction = "e";
  if(deltaX > 0 && deltaY > 0) {
    if(angle <= 22.5) {
      direction = "e";
    } else if(angle <= 67.5) {
      direction ="se"
    } else {
      direction = "s";
    }
  } else if(deltaX < 0 && deltaY > 0) {
    if(angle >= -22.5) {
      direction = "w";
    } else if(angle >= -67.5) {
      direction = "sw";
    } else {
      direction = "s";
    }
  } else if(deltaX < 0 && deltaY < 0) {
    if(angle < 22.5) {
      direction = "w";
    } else if(angle < 67.5) {
      direction = "nw";
    } else {
      direction = "n";
    }

  } else if(deltaX > 0 && deltaY < 0) {
    if(angle >= -22.5) {
      direction = "e";
    } else if(angle >= -67.5) {
      direction = "ne";
    } else {
      direction = "n";
    }
  } else if(deltaX == 0){
    if(deltaY > 0){
      direction = "s";
    } else if(deltaY < 0) {
      direction ="n";
    }
  } else if(deltaY == 0){
    if(deltaX > 0) {
      direction = "e";
    } else if(deltaX < 0) {
      direction = "w";
    }
  }

  return direction;
}
