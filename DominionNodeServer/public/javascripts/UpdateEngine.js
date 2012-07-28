var UpdateEngine = {};
UpdateEngine.npcs = {};
UpdateEngine.worldObjects = {};
UpdateEngine.pcs = {};

// used by updatePCs and updateNPCs
UpdateEngine.updateCharacter = function(character){

}

UpdateEngine.updatePCs = function(pcs){

}

UpdateEngine.updateNPCs = function(npcs){

}

UpdateEngine.updateWorldObjects = function(worldObjects){

}

UpdateEngine.updateDynamicEntities = function(entities){
  
}

UpdateEngine.initPlayer = function(player){
  console.debug("Initializing player "+player.name+" direction = "+player.direction+" state = "+player.state);
  var newPlayer = new PlayerCharacter();
  newPlayer.setUsername(player.name);
  newPlayer.setState(player.state);
  newPlayer.initEntity(player.id, "PlayerCharacter", null, 96, 96, player.x, player.y, player.name);
  var pausedAction = createActionSpriteList("images/knight/knight_pause/", "paused", "PNG", 96, 96, 5, 9, true);
  var movingAction = createActionSpriteList("images/knight/knight_move/", "running", "PNG", 96, 96, 5, 9, true);
  var attackingAction = createActionSpriteList("images/knight/knight_attack/", "attack", "PNG", 96, 96, 5, 9, false);
  var dyingAction = createActionSpriteList("images/knight/knight_dying/", "tipping", "PNG", 96, 96, 5, 9, false);
  var actionMap = {};
  actionMap["pause"] = pausedAction;
  actionMap["move"] = movingAction;
  actionMap["attack"] = attackingAction;
  actionMap["dying"] = dyingAction;

  newPlayer.initDynamicEntity(actionMap, "pause", player.direction);

  //newPlayer.addActionToQueue("pause");
  if(player.name == thisClientPlayerName){
    clientId = player.id;
  }

  return newPlayer;

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