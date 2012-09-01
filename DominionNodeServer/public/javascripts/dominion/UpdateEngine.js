var UpdateEngine = {};
UpdateEngine.lastUpdateTime = -1;

UpdateEngine.get

/**
 *main update loop
 *
 * apply player action
 * check for server messages
 * apply server state
 * apply player actions
 */
UpdateEngine.update = function(){
  var time = Date.now();
  var serverMessages = GameEngine.getServerMessages();
  var currentPlayer = GameEngine.getCurrentPlayer();
  var clientStates = GameEngine.getClientStates();

  var currentPlayerHistory = clientStates[currentPlayer.getId()];
  console.debug("Current player has "+currentPlayerHistory.length + " history states");

  var historyToCheck = [];

  for(var i = 0; i < currentPlayerHistory.length; i++){
    if(currentPlayerHistory[i].time > this.lastUpdatedTime){

    }
  }

  var currentPlayerMessages = serverMessages[currentPlayer.getId()];
  //handle server messages
  var newAction = false;
  if(GameEngine.hasClick()){
    var coord = GameEngine.getClick();
    var clickX = coord[0];
    var clickY = coord[1];
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
  var currentAction = currentPlayer.getAction();
  if(currentAction == "move"){
    var playerWaypoints = currentPlayer.getWayPoints();
    if(playerWaypoints != null && playerWaypoints.size > 0){
      var currWayPoint = playerWaypoints.pop();
      currentPlayer.setX(currWayPoint.getX());
      currentPlayer.setY(currWayPoint.getY());
      currentPlayer.setDirection(currWayPoint.getDirection());
    } else {
      currentAction = "pause"
    }

  }






}


/**
 * Note that npcs are of type Character (npc is just a map though)
 */
UpdateEngine.initNPC = function(npc){
  var actionMap = {};
  var newNpc = new NPC(npc.id, npc.className, npc.faction, npc.x, npc.y, npc.direction,npc.hp, npc.maxhp, npc.state, npc.wood, npc.gold);
  if(newNpc.type == 'lumberjack'){
    console.debug("initalizing lumberjack state = "+newNpc.state);
    newNpc = new Character();
    newNpc.setState(npc.state);
    newNpc.initEntity(npc.id, "NPC", null, 96, 96, npc.x, npc.y, npc.name);
    var pausedAction = createActionSpriteList("images/lumberjack/lumberjack_pause/", "talking", "PNG", 96, 96, 5, 9, true);
    var movingAction = createActionSpriteList("images/lumberjack/lumberjack_move/", "walking_without_axe", "PNG", 96, 96, 5, 9, true);
    actionMap["pause"] = pausedAction;
    actionMap["move"] = movingAction;

    newNpc.initDynamicEntity(actionMap, "pause", player.direction);

  } else if (newNpc.type == 'spider'){
    console.debug("initializing spider state = "+newNpc.state);
    newNpc = new Character();
    newNpc.setState(npc.state);
    newNpc.initEntity(npc.id, "NPC", null, 96, 96, npc.x, npc.y, npc.name);
    var pausedAction = createActionSpriteList("images/red_spider/red_spider_pause/", "spit", "PNG", 96, 96, 5, 9, true);
    var movingAction = createActionSpriteList("images/red_spider/red_spider_walk/", "walking", "PNG", 96, 96, 5, 8, true);
    var attackAction = createActionSpriteList("images/red_spider/red_spider_attack/", "attack", "PNG", 96, 96, 5, 9, false);
    var dyingAction = createActionSpriteList("images/red_spider/red_spider_die/", "tipping_over", "PNG", 96, 96, 5, 11, false);
    var dead = createActionSpriteList("images/red_spider/red_spider_dead/", "tipping_over", "bmp", 96, 96, 5, 1, true);
    actionMap["pause"] = pausedAction;
    actionMap["move"] = movingAction;
    actionMap["attack"] = attackAction;
    actionMap["dying"] = dyingAction;
    actionMap["dead"] = dead;

    newNpc.initDynamicEntity(actionMap, "pause", player.direction);
  }

  return newNpc;
}