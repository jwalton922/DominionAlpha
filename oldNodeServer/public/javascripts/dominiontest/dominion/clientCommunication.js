var Communication = {};
Communication.socket = null;
Communication.init = function(){
  console.info("Opening socket to server: "+DominionConfig.serverAddress);
  this.socket = io.connect(DominionConfig.serverAddress);
  this.initSocket();

}

Communication.initSocket = function(){
  this.socket.on('ClientPlayerDataResponse', Communication.handleClientPlayerData);
}

Communication.handleClientPlayerData = function(data){
  console.debug("Received client player data");
  var newPlayer = GameEngine.initPlayer(data);
  console.debug("Created client player object: "+newPlayer.toString());
  GameEngine.currentPlayer = newPlayer;
  RenderEngine.initCanvas(newPlayer.getX(),newPlayer.getY());
  GameEngine.start();
}

Communication.requestClientData = function(clientUsername, token){
  var requestData = {};
  requestData.username = clientUsername;
  requestData.authToken = token;
  requestData.type = "ClientPlayerDataRequest";
  this.socket.emit('ClientPlayerDataRequest', requestData);
}

//socket.on('server data', function (data) {
//
//
// // console.log("server data: "+data);
//  var users = data.players;
//  var server_npcs = data.npcs;
//  var objs = data.worldObjects;
//  var messages = data.messages;
//
//  //console.debug("Received "+objs.length+" world objects "+users.length+" users "+npcs.length+" npcs");
//  for(var i = 0; i < objs.length; i++){
//    if(worldObjects[objs[i].id] == null){
//      //world object doesn't exist, create it'
//      var world_object = loadWorldObjectSprite(objs[i].id,objs[i].name,objs[i].x,objs[i].y, objs[i].name, objs[i].owner, objs[i].resource_left, objs[i].type);
//      worldObjects[objs[i].id]=(world_object);
//    } else {
//      //world object exists, update it
//      worldObjects[objs[i].id].update(objs[i]);
//    }
//
//  }
//
//  for(i = 0; i < server_npcs.length; i++){
//    var server_npc = server_npcs[i];
//    if(npcs[server_npc.id] == null){
//      //npc doesn't exist create it'
//      console.debug("Found new npc calling init");
//      var npc = initNPC(server_npc);
//      npcs[server_npc.id] = npc;
//    } else {
//      //npc exists, update it
//      // console.debug("Updating npc with id "+server_npc.id);
//      npcs[server_npc.id].update(server_npc);
//    }
//  }
//  //console.info("Received "+users.length +" users");
//  for(i = 0; i < users.length; i++){
//    // console.info("user: "+users[i].username+" id = "+users[i].id);
//    if(players[users[i].id] == null){
//      console.info("Initing player: "+users[i].username);
//      players[users[i].id] = initPlayer(users[i]);
//    } else {
//      //if(players[users[i].id].name != thisClientPlayerName){
//      players[users[i].id].update(users[i]);
//      if(players[users[i].id].getName() == thisClientPlayerName){
//        canvasStartX = players[users[i].id].getX()-(canvasWidth/2);
//        canvasStartY = players[users[i].id].getY()-(canvasHeight/2);
//
//        $("#coords").html(players[users[i].id].getX()+","+players[users[i].id].getY()+" canvas: "+canvasStartX+","+canvasStartY);
//      }
//    //}
//    }
//  }
//  //console.log("received "+messages.length+" messages");
//  for(i = 0; i < messages.length; i++){
//    if(messages[i].type == "damage"){
//      var n = new Number(messages[i].message,messages[i].x,messages[i].y,messages[i].timestamp);
//
//      var newNum = true;
//      for(var j = 0; j < numbers.length; j++){
//        if(n.getTimeStamp() == numbers[j].getTimeStamp()){
//          newNum = false;
//          break;
//        }
//      }
//      if(newNum){
//        numbers.push(n);
//      }
//    } else if(messages[i].type == "action"){
//      var player = players[messages[i].playerId];
//      //should only be action messages added to queue
//      console.debug("Player "+player.getName()+" has new action: "+messages[i].action);
//      player.addActionToQueue(messages[i]);
//    }
//  }
//});
//
//function sendActionRequest(action, x, y){
//  console.log("requesting action "+action+" x="+x+" y="+y);
//  var data = {};
//  data.action = action;
//  data.entityType = "player";
//  data.x = x;
//  data.y = y;
//  data.name = thisClientPlayerName;
//  data.entityId = clientId;
//  //console.info("Sending action request for "+data.username+"user id = "+data.userid+" action ="+data.action+" at "+x+","+y);
//  socket.emit('action request', data);
//}
