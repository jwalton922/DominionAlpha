InputHandler = {};

InputHandler.processMouseClick = function(e){
  var x;
  var y;
  if (e.pageX != undefined && e.pageY != undefined) {
    x = e.pageX;
    y = e.pageY;
  }
  else {
    x = e.clientX + document.body.scrollLeft +
    document.documentElement.scrollLeft;
    y = e.clientY + document.body.scrollTop +
    document.documentElement.scrollTop;
  }

  x -= RenderEngine.canvas.offsetLeft;
  y -= RenderEngine.canvas.offsetTop;
  //    console.debug("x: "+x+", y: "+y);
  x+= RenderEngine.canvasStartX;
  y+= RenderEngine.canvasStartY;

  GameEngine.handleClick(x, y);
}

function processAttackRequest(player, x, y) {
  sendActionRequest("attack", x, y)
}

function processMoveRequest(player, x, y) {
  console.debug("Move request from "+player.getX()+","+player.getY()+" to "+x+","+y);
  sendActionRequest("move", x, y);
}

function findTarget(x, y){
  //check npcs first
  x+=canvasStartX;
  y+=canvasStartY;
  for(var key in npcs){
    var npc = npcs[key];
    var minX = npc.getX() - 48;
    var maxX = npc.getX() + 48;
    var minY = npc.getY() - 48;
    var maxY = npc.getY() + 48;
    //console.debug("checking "+minX+" "+maxX+" "+minY+" "+maxY);
    if(x >= minX && x <= maxX && y >= minY && y <= maxY){
      isTargetSelectMode = true;
      canvas.style.cursor="crosshair";
      //console.info("Moused over target "+npc.getType());
      return npc;
    }
  }
  //console.debug("Moused over no target at "+x+","+y);
  isTargetSelectMode = false;
  canvas.style.cursor="default";
  return null;
}

function processMouseMove(e){
  var x;
  var y;
  if (e.pageX != undefined && e.pageY != undefined) {
    x = e.pageX;
    y = e.pageY;
  }
  else {
    x = e.clientX + document.body.scrollLeft +
    document.documentElement.scrollLeft;
    y = e.clientY + document.body.scrollTop +
    document.documentElement.scrollTop;
  }

  x -= canvas.offsetLeft;
  y -= canvas.offsetTop;
  //console.debug("Move move: "+x+","+y);
  var target = findTarget(x,y);

}
/*
 *process mouse click
 */
function processClick(e){

  var x;
  var y;
  if (e.pageX != undefined && e.pageY != undefined) {
    x = e.pageX;
    y = e.pageY;
  }
  else {
    x = e.clientX + document.body.scrollLeft +
    document.documentElement.scrollLeft;
    y = e.clientY + document.body.scrollTop +
    document.documentElement.scrollTop;
  }

  x -= canvas.offsetLeft;
  y -= canvas.offsetTop;
  //    console.debug("x: "+x+", y: "+y);
  x+= canvasStartX;
  y+= canvasStartY;
  //console.debug("Clicked on: "+x+", "+y);

  if(buildBuildingMode){
    sendBuildingRequest(buildingToBuild.getName(), x, y);
    buildBuildingClicked();
  } else {

    var username = thisClientPlayerName;
    console.info("processing click for "+thisClientPlayerName+" who clicked at "+x+","+y);
    //    var worldObject = findWorldObject(x,y);
    //
    //
    //
    //    if(worldObject != null){
    //      console.debug("Displaying info for "+worldObject.getName());
    //      showWorldObjectInfo(worldObject);
    //      clickedWorldObject = worldObject;
    //      console.info("Clicked world object owner: "+clickedWorldObject.getOwner());
    //
    //    }
    var player = null;
    console.info("2. processing click for "+thisClientPlayerName+" who clicked at "+x+","+y);
    if(isTargetSelectMode){
      player = findCharacter(username);
      processAttackRequest(player, x, y);
      canvas.style.cursor="default";

    } else {
      player = findCharacter(username);
      processMoveRequest(player, x, y);
    //sendMoveRequest(player.getName(),x,y);
    }

  }

}
