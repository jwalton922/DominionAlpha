function DominionEntity(){
  this.id = -1; //unique id of entity
  this.type = null; //type of entity
  this.img = null; //image of entity
  this.x = -1; //x coord of entity
  this.y = -1; //y coord of entity
  this.width = -1; //width of img
  this.height = -1; //height of img
  this.name = null; //name of entity

  this.getId = function(){return this.id;};
  this.setId = function(id){this.id = id;};

  this.getImg = function(){return this.img;  }
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

  

  //non setter getter methods
  this.initEntity = function(id, type, img, width, height, x, y, name){
    this.setId(id);
    this.setType(type);
    this.setImg(img);
    this.setWidth(width);
    this.setHeight(height);
    this.setX(x);
    this.setY(y);
    this.setName(name);
  }

  this.getInfo = function(){
    return " name = "+this.getName()+" Entity ID = "+this.getId()+" type = "+this.getType();
  }

  this.update = function(entity){
    this.setX(entity.x);
    this.setY(entity.y);
  }

}

/*
 * Dynamic entity is a Dominion entity that can change
 * Its img will be updated depending on its current action and direction
 *
 */
function DynamicEntity() {
  this.actions = {}; //TODO figure out sprites
  this.frameIndex = -1; //the current frame of the sprite animation
  this.direction = null; //direction entity is facing
  this.actionName = null; //name of current action - action names are keys in actions map
  this.actionX = -1;
  this.actionY = -1;

  this.getActions = function(){return this.actions;}
  this.setActions = function(actions){this.actions = actions}

  this.getFrameIndex = function(){return this.frameIndex};
  this.setFrameIndex = function(frameIndex){this.frameIndex = frameIndex;}

  this.getDirection = function(){return this.direction;}
  this.setDirection = function(direction){this.direction = direction;}
  
  this.getActionName = function(){return this.actionName;}
  this.setActionName = function(actionName){this.actionName = actionName;}

  this.getActionX = function(){return this.actionX;}
  this.setActionX = function(actionX){this.actionX = actionX;}

  //non getters setters
  this.initDynamicEntity = function(actionSprites, actionName, direction){
    this.actions = actionSprites;
    this.actionName = actionName;
    this.direction = direction;
  }

  this.getActionSprite = function(){
    var sprite = null;
    var actionToDirectionMap = this.actions[this.actionName];
    if(actionToDirectionMap != null){
      sprite = actionToDirectionMap[this.direction];
      if(sprite == null){
        console.error("Could not find sprite for direction: "+this.direction+" action: "+this.actionName+" for entity "+this.getInfo());
      }
    } else {
      console.error("Could not find action: "+this.actionName+" for entity: "+this.getInfo());
    }

    return sprite;
  }

  this.updateState = function(){
    var actionSprite = this.getActionSprite();
    if(actionSprite.isComplete()){
      this.setActionName("pause");
    } else {
      actionSprite.updateFrame();
    }
  
  }

}

function WorldObject(){
  
  this.owner = owner = null;
  this.resource_left = -1;
 
  this.getResourceLeft = function(){return this.resource_left;}
  this.setResourceLeft = function(resourceLeft){this.resoruce_left = resourceLeft;}

  this.getOwner = function(){return this.owner;}
  this.setOwner = function(owner){this.owner = owner;}
  
}

function Character()
{  
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

  this.update = function(dynamicEntity){
    this.hp = dynamicEntity.hp;
    this.maxhp = dynamicEntity.maxhp;
    this.prototype.update(dynamicEntity);
  }
}

function Number(num, x, y, timestamp){
  this.num = num;
  this.x = x;
  this.y = y;
  this.origY = y;
  this.timestamp = timestamp;

  this.getTimeStamp = function(){return this.timestamp;}
  this.getNum = function(){return this.num}
  this.getOrigY = function(){return this.origY;}
  this.getX = function(){return this.x;}
  this.setX = function(x){this.x = x;}
  this.getY = function(){return this.y;}
  this.setY = function(y){this.y = y;}
}

function PlayerCharacter(){
  this.username = null;

  this.getUsername = function(){return this.username;}
  this.setUsername = function(username){this.username = username;}
}

//setup prototype chains
DynamicEntity.prototype = DominionEntity;
WorldObject.prototype = DominionEntity;
Character.prototype = DynamicEntity;
PlayerCharacter.prototype = Character;




