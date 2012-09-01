function Character(){
  this.id = -1; //unique id of entity
  this.img = null; //image of entity
  this.x = -1; //x coord of entity
  this.y = -1; //y coord of entity
  this.width = -1; //width of img
  this.height = -1; //height of img
  this.name = null; //name of entity
  this.className = null; //type or class of character
  this.sprites = {}; //TODO figure out sprites
  this.frameIndex = -1; //the current frame of the sprite animation
  this.direction = null; //direction entity is facing
  this.action = null; //name of current action - action names are keys in actions map
  this.queuedAction = null;
  this.queuedActionX = null;
  this.queuedActionY = null;
  this.actionX = -1;
  this.actionY = -1;
  this.state = null;
  this.attackDistance = 40;
  this.hp = -1;
  this.maxhp = -1;
  this.attackSpeed = 10 //one attack per this many frames
  this.attackFrameCount = 0;
  this.faction = null;
  this.wood = 0;
  this.gold = 0;
  this.iron = 0;
  this.username = null;
  this.waypoints = [];

  this.toString = function(){
    return "name: "+this.name+" username: "+this.username+" id: "+this.id+" x: "+this.x+" y: "+this.y+" dir: "+this.direction+" hp: "+this.hp+" maxhp: "+this.maxhp;
  }

  this.getClassName = function(){return this.className;}
  this.setClassName = function(className){this.className = className;}

  this.getId = function(){return this.id;};
  this.setId = function(id){this.id = id;};

  this.getImg = function(){return this.img;}
  this.setImg = function(img){this.img = img;}

  this.getX = function(){return this.x;}
  this.setX = function(x){this.x = x;}

  this.getY = function(){return this.y;}
  this.setY = function(y){this.y = y;}

  this.getWidth = function(){return this.width;}
  this.setWidth = function(width){this.width = width;}

  this.getHeight = function(){return this.height;}
  this.setHeight = function(height){this.height = height;}

  this.getName = function(){return this.name;}
  this.setName = function(name){this.name = name;}

  this.getType = function(){return this.type;}
  this.setType = function(type){this.type = type;}

  this.getSprites = function(){return this.sprites;}
  this.setSprites = function(sprites){this.sprites = sprites}

  this.getFrameIndex = function(){return this.frameIndex};
  this.setFrameIndex = function(frameIndex){this.frameIndex = frameIndex;}

  this.getDirection = function(){return this.direction;}
  this.setDirection = function(direction){this.direction = direction;}
  
  this.getAction = function(){return this.action;}
  this.setAction = function(action){this.action = action;}

  this.getActionX = function(){return this.actionX;}
  this.setActionX = function(actionX){this.actionX = actionX;}

  this.getSprite = function(){
    var sprite = null;
    var actionToDirectionMap = this.sprites[this.action];
    if(actionToDirectionMap != null){
      sprite = actionToDirectionMap[this.direction];
      if(sprite == null){
        console.error("Could not find sprite for direction: "+this.direction+" action: "+this.action+" for entity "+this.getInfo());
      }
    } else {
      console.error("Could not find action: "+this.action+" for entity: "+this.toString());
    }

    return sprite;
  }

  this.getState = function(){return this.state;}
  this.setState = function(state){this.state = state;}

  this.getAttackSpeed = function(){return this.attackSpeed;}
  this.setAttackSpeed = function(attackSpeed){this.attackSpeed = attackSpeed;}

  this.getHP = function(){return this.hp;}
  this.setHP = function(hp){this.hp = hp;}

  this.getMaxHP = function(){return this.maxhp;}
  this.setMaxHP = function(maxhp){this.maxhp = maxhp;}

  this.getAttackDistance = function() {return this.attackDistance;}
  this.setAttackDistance = function(attackDistance){this.attackDistance = attackDistance;}

  this.getSpeed = function() {return this.speed;}
  this.setSpeed = function(speed) {this.speed = speed;}

  this.getFaction = function(){return this.faction;}
  this.setFaction = function(faction){this.faction = faction;}

  this.getGold = function(){return this.gold;}
  this.setGold = function(gold){this.gold = gold;}

  this.getWood = function(){return this.wood;}
  this.setWood = function(wood){this.wood = wood;}

  this.getUsername = function(){return this.username;}
  this.setUsername = function(username){this.username = username;}

  this.setWayPoints = function(waypoints){this.waypoints = waypoints;}
  this.getWayPoints = function(){return this.waypoints;}

  this.getQueuedAction = function(){return this.queuedAction;}
  this.setQueuedAction = function(queuedAction){this.queuedAction = queuedAction;}

  this.getQueuedActionX = function(){return this.queuedActionX;}
  this.setQueuedActionX = function(queuedActionX){ this.queuedActionX = queuedActionX;}

  this.getQueuedActionY = function(){return this.queuedActionY;}
  this.setQueuedActionY = function(queuedActionY){ this.queuedActionY = queuedActionY;}

}

function WorldObject(){
  this.id = -1; //unique id of entity
  this.img = null; //image of entity
  this.x = -1; //x coord of entity
  this.y = -1; //y coord of entity
  this.width = -1; //width of img
  this.height = -1; //height of img
  this.owner = owner = null;
  this.resource_left = -1;
 
  this.getResourceLeft = function(){return this.resource_left;}
  this.setResourceLeft = function(resourceLeft){this.resoruce_left = resourceLeft;}

  this.getOwner = function(){return this.owner;}
  this.setOwner = function(owner){this.owner = owner;}
  
}

function Message(message, x, y, timestamp){
  this.message = message;
  this.x = x;
  this.y = y;
  this.origY = y;
  this.timestamp = timestamp;

  this.getTimeStamp = function(){return this.timestamp;}
  this.getMessage = function(){return this.message}
  this.getOrigY = function(){return this.origY;}
  this.getX = function(){return this.x;}
  this.setX = function(x){this.x = x;}
  this.getY = function(){return this.y;}
  this.setY = function(y){this.y = y;}
}

function WayPoint(x, y, direction){
  this.x = x;
  this.y = y;
  this.direction = direction;

  this.getX = function(){return this.x;}
  this.getY = function(){return this.y;}
  this.getDirection = function(){return this.direction;}
}





