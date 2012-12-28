var GameEngine = {};
GameEngine.currentPlayer = null;
GameEngine.characters = [];
GameEngine.init = function(){
  Communication.init();
  
  var username = $("#username").html();
  username = username.substring(0,username.length -1);
  var token = $("#token").html();
  console.debug("Requesting client data for "+username+" token: "+token);
  Communication.requestClientData(username, token);
  
  
  
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

GameEngine.gameLoop = function(){
    UpdateEngine.update();
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
  
}