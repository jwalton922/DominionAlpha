
var DIRECTIONS = ["e", "se", "s", "sw", "w", "nw", "n", "ne"];
var canvas;
var context;
var entities = {}; //static entities like trees, castles
var dynamicEntities = {}; //entities that have sprites - like spell effects
var characters = {}; //PCs and NPCs
var messages = {}; //any combat status text like damage
var canvasStartX=0;
var canvasStartY=0;
var clientId = -1;
var canvasWidth = 1000;
var canvasHeight = 600;

RenderEngine = {};

RenderEngine.initCanvas = function() {
  canvas = document.getElementById("canvas");
  context = canvas.getContext("2d");
  preloadcanvas = document.getElementById("preloadcanvas");
  preloadcontext = preloadcanvas.getContext("2d");
  groundImage = new Image();
  groundImage.src = "images/grasstile.png";
  groundImage.onload = preloadImage(groundImage);
}

RenderEngine.draw = function() {
  drawGround();
  drawEntities();
  drawDynamicEntities();
  drawCharacters();
}

RenderEngine.drawDynamicEntities = function(){
  var minCanX = canvasStartX;
  var maxCanX = canvasStartX+canvasWidth;
  var minCanY = canvasStartY;
  var maxCanY = canvasStartY+canvasHeight;
  for(entity in dynamicEntities){
    drawDynamicEntity(entity, minCanX, maxCanX, minCanY, maxCanY);
  }
}

RenderEngine.drawCharacters = function(){
  var minCanX = canvasStartX;
  var maxCanX = canvasStartX+canvasWidth;
  var minCanY = canvasStartY;
  var maxCanY = canvasStartY+canvasHeight;
  for(character in characters){
    drawDynamicEntity(character, minCanX, maxCanX, minCanY, maxCanY);
    try {
      context.font = "bold 12px sans-serif black";
      //context.fillText(players[i].getName(), drawX-players.getWidth()/2, drawY-players.getHeight()/2-10);
      //var textX = Math.round(dra)
      context.fillText(entity.getName(), drawX-8,drawY-entity.getHeight()/3);
      var pixelsToFill = Math.round(30*(entity.getHP()/player.getMaxHP()));
      if(pixelsToFill < 0){
        pixelsToFill = 0;
      }
      // console.log("PIXELS TO FILL: "+pixelsToFill);
      context.fillRect(drawX-10, drawY+player.getHeight()/2, pixelsToFill,5);
    } catch(err){
      
    }
  }
}

RenderEngine.drawDynamicEntity = function(entity, minCanX, maxCanX, minCanY, maxCanY){
  var x = entity.getX();
  var y = entity.getY();
  var width = entity.getWidth();
  var height = entity.getHeight();
  var minObjX = x-width/2;
  var maxObjX = x+width/2;
  var minObjY = y-height/2;
  var maxObjY = y+height/2;
  //assuming no object larger than the canvas!
  if((minObjX >= minCanX && minObjX <= maxCanX) || (maxObjX >= minCanX && maxObjX <= maxCanX) &&
    (minObjY >= minCanY && minObjY <= maxCanY) || (maxObjY >= minCanY && maxObjY <= maxCanY)){
    //    if(canvasStartX >= minX && canvasStartX <= maxX && canvasStartY >= minY && canvasStartY <= maxY){
    //    if(objX >= canvasStartX && objX <= (canvasStartX+canvasWidth)
    //      && objY >= canvasStartY && objY <= (canvasStartY+canvasHeight)){

    var drawX = x - canvasStartX;
    var drawY = y - canvasStartY;
    context.strokeRect(drawX-width/2,drawY-height/2,width,height);
    // console.log("drawing world object at: "+drawX+","+drawY+" world pos: "+objX+","+objY);
    var direction = entity.getDirection();
    var sprite = entity.getActionSprite();
    var  frame = sprite[direction].getFrame();
    try {
      context.drawImage(sprite[direction].getSpriteImage(), frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), drawX-player.getWidth()/2, drawY-player.getHeight()/2, 96, 96 );

    }catch(err){

    }
  }

  RenderEngine.drawEntities = function(){
    var minCanX = canvasStartX;
    var maxCanX = canvasStartX+canvasWidth;
    var minCanY = canvasStartY;
    var maxCanY = canvasStartY+canvasHeight;

    for(var entity in entities){
      var image = entity.getImg();
      var x = entity.getX();
      var y = entity.getY();
      var width = entity.getWidth();
      var height = entity.getHeight();
      var minObjX = x-width/2;
      var maxObjX = x+width/2;
      var minObjY = y-height/2;
      var maxObjY = y+height/2;
    
      //assuming no object larger than the canvas!
      if((minObjX >= minCanX && minObjX <= maxCanX) || (maxObjX >= minCanX && maxObjX <= maxCanX) &&
        (minObjY >= minCanY && minObjY <= maxCanY) || (maxObjY >= minCanY && maxObjY <= maxCanY)){
        //    if(canvasStartX >= minX && canvasStartX <= maxX && canvasStartY >= minY && canvasStartY <= maxY){
        //    if(objX >= canvasStartX && objX <= (canvasStartX+canvasWidth)
        //      && objY >= canvasStartY && objY <= (canvasStartY+canvasHeight)){

        var drawX = x - canvasStartX;
        var drawY = y - canvasStartY;
        context.strokeRect(drawX-width/2,drawY-height/2,width,height);
        // console.log("drawing world object at: "+drawX+","+drawY+" world pos: "+objX+","+objY);
        try {
          context.drawImage(image, 0, 0, width,height, drawX-width/2,drawY-height/2, width, height);
        }catch(err){

        }
      } //end if in canvas range
    } //end for loop
  } //end function

  RenderEngine.drawGround = function() {
    context.clearRect(0,0,canvas.width,canvas.height);
    context.drawImage(groundImage, 0, 0, 256, 192, 0, 0, 20, 20);
  //console.log("drawing ground");
  }