
var DIRECTIONS = ["e", "se", "s", "sw", "w", "nw", "n", "ne"];

RenderEngine = {};

RenderEngine.canvas = null;
RenderEngine.context = null;
RenderEngine.canvasStartX = 0;
RenderEngine.canvasStartY = 0;
RenderEngine.canvasWidth = 1000;
RenderEngine.canvasHeight = 600;
RenderEngine.groundImage = null;
RenderEngine.count = 0;

RenderEngine.initCanvas = function(startX, startY) {
  this.canvasStartX = startX - (this.canvasWidth/2);
  this.canvasStartY = startY - (this.canvasHeight/2);
  console.debug("Canvas start: "+this.canvasStartX+","+this.canvasStartY+" wxh = "+this.canvasWidth+"x"+this.canvasHeight);
  this.canvas = document.getElementById("canvas");
  this.context = this.canvas.getContext("2d");
  this.groundImage = new Image();
  this.groundImage.src = "images/grasstile.png";
  this.groundImage.onload = preloadImage(this.groundImage);
}

RenderEngine.draw = function() {
  RenderEngine.drawGround();
  RenderEngine.drawCharacters(GameEngine.characters);
  this.count++;
  if(this.count > 40){
    console.debug("RenderEngine.draw() called");
    this.count = 0;
  }
}

RenderEngine.drawCharacters = function(characters){
  var minCanX = this.canvasStartX;
  var maxCanX = this.canvasStartX+this.canvasWidth;
  var minCanY = this.canvasStartY;
  var maxCanY = this.canvasStartY+this.canvasHeight;
  for(i = 0; i < characters.length; i++){
    var character = characters[i];
    //console.debug("calling drawCharacter on: "+JSON.stringify(character));
    this.drawCharacter(character, minCanX, maxCanX, minCanY, maxCanY);
    
  }
}

RenderEngine.drawCharacter = function(character, minCanX, maxCanX, minCanY, maxCanY){
  console.debug(minCanX+" "+maxCanX+" "+minCanY+" "+maxCanY);
  var x = character.getX();
  var y = character.getY();
  console.debug("Drawing character "+character.getUsername()+" at "+x+","+y);
  var width = character.getWidth();
  var height = character.getHeight();
  var minObjX = x-width/2;
  var maxObjX = x+width/2;
  var minObjY = y-height/2;
  var maxObjY = y+height/2;
  //assuming no object larger than the canvas!
  if((minObjX >= minCanX && minObjX <= maxCanX) || (maxObjX >= minCanX && maxObjX <= maxCanX) &&
    (minObjY >= minCanY && minObjY <= maxCanY) || (maxObjY >= minCanY && maxObjY <= maxCanY)){
    
    var drawX = x - this.canvasStartX;
    var drawY = y - this.canvasStartY;
    this.context.strokeRect(drawX-width/2,drawY-height/2,width,height);
    // console.log("drawing world object at: "+drawX+","+drawY+" world pos: "+objX+","+objY);
    
    var sprite = character.getSprite();
    var frame = sprite.getFrame();
    console.debug("Sprite image: "+sprite.getSpriteImage());
    try {
      this.context.drawImage(sprite.getSpriteImage(), frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(), drawX-character.getWidth()/2, drawY-character.getHeight()/2, 96, 96 );
      this.context.fillText(character.getName(), drawX-8,drawY-character.getHeight()/3);
      var pixelsToFill = Math.round(30*(character.getHP()/character.getMaxHP()));
      if(pixelsToFill < 0){
        pixelsToFill = 0;
      }
      // console.log("PIXELS TO FILL: "+pixelsToFill);
      this.context.fillRect(drawX-10, drawY+character.getHeight()/2, pixelsToFill,5);
    }catch(err){
      console.error("Error: "+err);
    }
  }
}

RenderEngine.drawGround = function() {
  this.context.clearRect(0,0,this.canvas.width,this.canvas.height);
  this.context.drawImage(this.groundImage, 0, 0, 256, 192, 0, 0, 20, 20);
//console.log("drawing ground");
}